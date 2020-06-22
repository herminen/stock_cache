package com.lh.stock.stockcache.storm.bolt.hotprod;

import com.alibaba.fastjson.JSONObject;
import com.lh.stock.stockcache.domain.HotProdInfo;
import com.lh.stock.stockcache.storm.bolt.CountBolt;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.trident.util.LRUMap;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @Author: liuhai
 * @Date: 2020/6/22 11:44
 */
public class SplitHotProdBolt extends BaseRichBolt {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountBolt.class);
    private static final long serialVersionUID = 7386047123242242362L;

    private OutputCollector collector;

    private LRUMap<Long, HotProdInfo> hotProdInfoCache = new LRUMap<Long, HotProdInfo>(1000);

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
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
