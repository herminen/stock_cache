package com.lh.stock.stockcache.storm.bolt;

import com.google.common.collect.Maps;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ASUS
 * Date: 2020/6/13
 * Time: 23:55
 * Description: No Description
 */
public class CountBolt extends BaseRichBolt {
    private static final Logger LOGGER = LoggerFactory.getLogger(CountBolt.class);
    private static final long serialVersionUID = -4423616713382067808L;
    private Map<String, Long> wordCount = Maps.newHashMapWithExpectedSize(100);

    private OutputCollector outputCollector;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.outputCollector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        String word = tuple.getStringByField("word");
        Long count = wordCount.get(word);
        if(null == count){
            count = 0L;
        }
        count++;
        wordCount.put(word, count);
        LOGGER.info("【单词计数】" + word + "出现的次数是" + count);

        outputCollector.emit(new Values(word, count));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("word", "count"));
    }
}
