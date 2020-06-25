package com.lh.stock.stockcache.storm.bolt.hotprod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lh.stock.stockcache.domain.HotProdInfo;
import com.lh.stock.stockcache.storm.bolt.CountBolt;
import com.lh.stock.stockcache.storm.hotcache.IMakeHotCache;
import com.lh.stock.stockcache.storm.hotcache.impl.MakeHotProductCache;
import com.lh.stock.stockcache.zk.ZookeeperSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.trident.util.LRUMap;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.utils.Utils;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.lh.stock.stockcache.constant.CacheKeyConstants.ZK_HOT_PROD_CACHE;
import static com.lh.stock.stockcache.constant.CacheKeyConstants.ZK_STOCK_CACHE;
import static com.lh.stock.stockcache.constant.ComConstants.SEP_COMA;

/**
 * @Author: liuhai
 * @Date: 2020/6/22 11:44
 */
@Component
public class SplitHotProdBolt extends BaseRichBolt {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountBolt.class);
    private static final long serialVersionUID = 7386047123242242362L;

    private OutputCollector collector;

    private LRUMap<Long, HotProdInfo> hotProdInfoCache = new LRUMap<Long, HotProdInfo>(1000);

    private String hotProdCacheTaskId;

    @Autowired
    private ZookeeperSession zookeeperSession;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.hotProdCacheTaskId = ZK_HOT_PROD_CACHE + context.getThisTaskId();
        recordHotProdCacheId();
        startRefreshHotProdCacheThread();
    }

    /**
     * 启动更新热点缓存线程
     */
    private void startRefreshHotProdCacheThread() {
        new Thread(() ->{
            IMakeHotCache<HotProdInfo> makeHotCache = new MakeHotProductCache(hotProdInfoCache);
            while(true) {
                try {
                    zookeeperSession.createPersistNode(hotProdCacheTaskId, false);
                    zookeeperSession.setNodeValue(hotProdCacheTaskId, JSONArray.toJSONString(makeHotCache.makeCache()));
                } catch (KeeperException | InterruptedException e) {
                    LOGGER.error("record cache to zookeeper error", e);
                }
                Utils.sleep(5000);
            }
        }).start();
    }

    /**
     * 记录缓存任务id
     */
    private void recordHotProdCacheId() {
        //在zk上创建热点缓存列表信息
        try {
            String hotProdLock = ZK_STOCK_CACHE + "/hot_prod_lock";
            zookeeperSession.acquireDistributeLock(hotProdLock);
            String hotProdList = ZK_STOCK_CACHE + "/hot_prod_list";
            zookeeperSession.createPersistNode(hotProdList, true);
            String nodeValue = zookeeperSession.getNodeValue(hotProdList);
            if(nodeValue.indexOf(hotProdList) > -1){
                return;
            }
            if(StringUtils.isBlank(nodeValue)){
                nodeValue += hotProdCacheTaskId;
            }else{
                nodeValue += SEP_COMA + hotProdCacheTaskId;
            }
            zookeeperSession.setNodeValue(hotProdList, nodeValue);
            zookeeperSession.releaseDistributeLock(hotProdLock);
        } catch (KeeperException | InterruptedException e) {
            LOGGER.error("create cache period error", e);
        }
    }

    @Override
    public void execute(Tuple input) {
        String hotProdInfo = input.getStringByField("hotProdInfo");
        LOGGER.warn("get hotprodcache: {}", hotProdInfo);
        HotProdInfo hotProdCache = JSONObject.parseObject(hotProdInfo, HotProdInfo.class);
        hotProdInfoCache.put(hotProdCache.getProdId(), hotProdCache);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
