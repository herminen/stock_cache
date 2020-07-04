package com.lh.stock.stockcache.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Sets;
import com.lh.stock.stockcache.domain.HotProdInfo;
import com.lh.stock.stockcache.domain.KafkaMsgContext;
import com.lh.stock.stockcache.domain.ZKHotProdCacheData;
import com.lh.stock.stockcache.kafka.Producer;
import com.lh.stock.stockcache.zk.ZookeeperSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.storm.utils.Utils;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

import static com.lh.stock.stockcache.constant.CacheKeyConstants.*;
import static com.lh.stock.stockcache.constant.ComConstants.DEFAULT_LOCK_VALUE;
import static com.lh.stock.stockcache.constant.ComConstants.SEP_UNDERLINE;
import static com.lh.stock.stockcache.constant.KafkaMsgConstants.PROD_BASE_INFO;
import static com.lh.stock.stockcache.constant.KafkaMsgConstants.SHOP_BASE_INFO;

/**
 * Created with IntelliJ IDEA.
 * User: liuhai
 * Date: 2020/7/4
 * Time: 20:49
 * Description: No Description
 */
@Component
public class PutIntoRedisCacheListener {

    Logger LOGGER = LoggerFactory.getLogger(PutIntoRedisCacheListener.class);

    @Autowired
    private ZookeeperSession zookeeperSession;

    @Autowired
    private Producer producer;

    @EventListener(classes={ApplicationStartedEvent.class})
    public void setHotDataListener(){
        new Thread(() ->{
            while (true){
                String taskId = "0";
                try {
                    zookeeperSession.acquireDistributeLock(ZK_CHACH_PROD_LOCK);
                    String nodeValue = zookeeperSession.getNodeValue(ZK_CACHE_LIST_NODE);
                    if(DEFAULT_LOCK_VALUE.equals(nodeValue) || StringUtils.isBlank(nodeValue)){
                        zookeeperSession.releaseDistributeLock(ZK_CHACH_PROD_LOCK);
                        continue;
                    }
                    HashSet<ZKHotProdCacheData> zkHotProdCacheDatas = Sets.newHashSet(JSONArray.parseArray(nodeValue, ZKHotProdCacheData.class));
                    for (ZKHotProdCacheData hotProdCacheData : zkHotProdCacheDatas) {
                        if(hotProdCacheData.getHasCached()){
                            continue;
                        }
                        String cacheNode = hotProdCacheData.getCacheNode();
                        taskId = cacheNode.substring(cacheNode.lastIndexOf(SEP_UNDERLINE) + 1);
                        zookeeperSession.acquireDistributeLock(ZK_HOT_CACHE_NODE_LOCK + taskId);
                        String hotProdCache = zookeeperSession.getNodeValue(ZK_CACHE_HOT_PROD_NODE + taskId);
                        if(StringUtils.isBlank(hotProdCache)){
                            zookeeperSession.releaseDistributeLock(ZK_HOT_CACHE_NODE_LOCK + taskId);
                            break;
                        }
                        List<HotProdInfo> hotProdInfos = JSONArray.parseArray(hotProdCache, HotProdInfo.class);
                        //refresh cache system
                        //查询数据库，更新缓存
                        for (HotProdInfo hotProdInfo : hotProdInfos) {
                            LOGGER.warn("put hot product into cache system, hotprodinfo:{}",hotProdInfo );
                            KafkaMsgContext prodMsgContext = new KafkaMsgContext(String.valueOf(hotProdInfo.getProdId()), PROD_BASE_INFO);
                            producer.sendMessage(prodMsgContext);
                            KafkaMsgContext shopMsgContext = new KafkaMsgContext(String.valueOf(hotProdInfo.getShopId()), SHOP_BASE_INFO);
                            producer.sendMessage(shopMsgContext);
                        }
                        hotProdCacheData.setHasCached(true);
                        zookeeperSession.releaseDistributeLock(ZK_HOT_CACHE_NODE_LOCK + taskId);
                    }
                    zookeeperSession.acquireDistributeLock(ZK_HOT_CHACH_LOCK);
                    zookeeperSession.setNodeValue(ZK_CACHE_LIST_NODE, JSONArray.toJSONString(zkHotProdCacheDatas, SerializerFeature.WRITE_MAP_NULL_FEATURES));
                    zookeeperSession.releaseDistributeLock(ZK_HOT_CHACH_LOCK);
                    zookeeperSession.releaseDistributeLock(ZK_CHACH_PROD_LOCK);
                } catch (Exception e) {
                    LOGGER.error("precache hot data error", e);
                    zookeeperSession.releaseDistributeLock(ZK_HOT_CHACH_LOCK);
                    zookeeperSession.releaseDistributeLock(ZK_HOT_CACHE_NODE_LOCK + taskId);
                    zookeeperSession.releaseDistributeLock(ZK_CHACH_PROD_LOCK);
                }
                Utils.sleep(10000L);
            }
        }).start();
    }
}
