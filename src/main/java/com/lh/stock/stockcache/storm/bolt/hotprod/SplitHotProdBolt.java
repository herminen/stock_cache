package com.lh.stock.stockcache.storm.bolt.hotprod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Sets;
import com.lh.stock.stockcache.domain.HotProdInfo;
import com.lh.stock.stockcache.domain.ZKHotProdCacheData;
import com.lh.stock.stockcache.storm.bolt.CountBolt;
import com.lh.stock.stockcache.storm.hotcache.IMakeHotCache;
import com.lh.stock.stockcache.storm.hotcache.impl.MakeHotProductCache;
import com.lh.stock.stockcache.util.SpringContextUtil;
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

import java.util.Map;
import java.util.Set;

import static com.lh.stock.stockcache.constant.CacheKeyConstants.*;
import static com.lh.stock.stockcache.constant.ComConstants.DEFAULT_LOCK_VALUE;

/**
 * @Author: liuhai
 * @Date: 2020/6/22 11:44
 */
public class SplitHotProdBolt extends BaseRichBolt{
    private static final long serialVersionUID = 7386047123242242362L;

    private static final Logger LOGGER = LoggerFactory.getLogger(CountBolt.class);

    private OutputCollector collector;

    private TopologyContext context;

    private LRUMap<Long, HotProdInfo> hotProdInfoCache = new LRUMap<Long, HotProdInfo>(1000);

    private String hotProdCacheNodeLock;

    private String hotProdCacheNode;

    private ZookeeperSession zookeeperSession;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.context = context;
        this.hotProdCacheNode = ZK_CACHE_HOT_PROD_NODE + context.getThisTaskId();
        this.hotProdCacheNodeLock = ZK_HOT_CACHE_NODE_LOCK + context.getThisTaskId();
        zookeeperSession = SpringContextUtil.getBean(ZookeeperSession.class);
        recordHotProdCacheId();
        startRefreshHotProdCacheThread();
    }

    /**
     * 记录缓存任务id
     */
    private void recordHotProdCacheId() {
        //在zk上创建热点缓存列表信息
        new Thread(()->{
            while ((true)) {
                try {
                    zookeeperSession.acquireDistributeLock(ZK_HOT_CHACH_LOCK);
                    zookeeperSession.createNode(ZK_CACHE_LIST_NODE, false);
                    zookeeperSession.acquireDistributeLock(hotProdCacheNodeLock);
                    String nodeValue = zookeeperSession.getNodeValue(ZK_CACHE_LIST_NODE);
                    Set<ZKHotProdCacheData> zkHotProdCacheDatas;
                    if (DEFAULT_LOCK_VALUE.equals(nodeValue) || StringUtils.isBlank(nodeValue)) {
                        zkHotProdCacheDatas = Sets.newHashSetWithExpectedSize(10);
                    } else {
                        zkHotProdCacheDatas = Sets.newHashSet(JSONArray.parseArray(nodeValue, ZKHotProdCacheData.class));
                    }
                    zkHotProdCacheDatas.add(new ZKHotProdCacheData(hotProdCacheNode, false, context.getThisTaskId()));

                    zookeeperSession.setNodeValue(ZK_CACHE_LIST_NODE, JSONArray.toJSONString(zkHotProdCacheDatas, SerializerFeature.WRITE_MAP_NULL_FEATURES));
                    zookeeperSession.releaseDistributeLock(hotProdCacheNodeLock);
                    zookeeperSession.releaseDistributeLock(ZK_HOT_CHACH_LOCK);
                } catch (KeeperException | InterruptedException e) {
                    LOGGER.error("create cache period error", e);
                    zookeeperSession.releaseDistributeLock(hotProdCacheNodeLock);
                    zookeeperSession.releaseDistributeLock(ZK_HOT_CHACH_LOCK);
                }
                Utils.sleep(5000);
            }
        }).start();
    }

    /**
     * 启动更新热点缓存线程
     */
    private void startRefreshHotProdCacheThread() {
        new Thread(() ->{
            IMakeHotCache<HotProdInfo> makeHotCache = new MakeHotProductCache(hotProdInfoCache);
            while(true) {
                try {
                    zookeeperSession.acquireDistributeLock(hotProdCacheNodeLock);
                    zookeeperSession.createNode(hotProdCacheNode, false);
                    zookeeperSession.setNodeValue(hotProdCacheNode, JSONArray.toJSONString(makeHotCache.makeCache()));
                    zookeeperSession.releaseDistributeLock(hotProdCacheNodeLock);
                } catch (KeeperException | InterruptedException e) {
                    LOGGER.error("record cache to zookeeper error", e);
                    zookeeperSession.releaseDistributeLock(hotProdCacheNodeLock);
                }
                Utils.sleep(5000);
            }
        }).start();
    }

    @Override
    public void execute(Tuple input) {
        JSONObject hotProdInfo = (JSONObject)input.getValueByField("hotProdInfo");
        LOGGER.warn("get hotprodcache: {}", hotProdInfo);
        HotProdInfo hotProdCache = hotProdInfo.getObject("uri_args",  HotProdInfo.class);
        if(hotProdInfoCache.containsKey(hotProdCache.getProdId())){
            hotProdCache = hotProdInfoCache.get(hotProdCache.getProdId());
            hotProdInfoCache.remove(hotProdCache.getProdId());
        }
        hotProdCache.increaseVisitCount();
        hotProdInfoCache.put(hotProdCache.getProdId(), hotProdCache);
        //todo 将该节点的缓存预热状态置为未预热
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
