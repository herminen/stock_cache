package com.lh.stock.stockcache.storm.spout;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.Properties;

/**
 * @Author: liuhai
 * @Date: 2020/6/22 10:45
 */
public class HotProductSpout extends BaseRichSpout {

    private Logger logger = LoggerFactory.getLogger(HotProductSpout.class);

    private SpoutOutputCollector outputCollector;

    private KafkaConsumer<String, String> hotProdInfoConsumer;
    @Autowired
    private ConsumerFactory<String, String> consumerFactory;


    @Override
    public void open(Map config, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.outputCollector = spoutOutputCollector;
        this.hotProdInfoConsumer = (KafkaConsumer<String, String>)((DefaultKafkaConsumerFactory)consumerFactory).
                createConsumer(null, null, null, initAndGetConsumerProperties());
        hotProdInfoConsumer.subscribe(Lists.newArrayList("log-product"));
    }

    @Override
    public void nextTuple() {
        ConsumerRecords<String, String> consumerRecords = hotProdInfoConsumer.poll(Duration.ofSeconds(1L));
        if(consumerRecords.isEmpty()){
            return;
        }
        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
            logger.warn("Consumer msg:{}", consumerRecord.value());
            outputCollector.emit(new Values(JSONObject.parseObject(consumerRecord.value())));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("hotProdInfo"));
    }

    private Properties initAndGetConsumerProperties(){
        Map<String, Object> configurationProperties = consumerFactory.getConfigurationProperties();
        Properties props = new Properties();
        props.put("bootstrap.servers", configurationProperties.get("bootstrapServers"));   //required
        props.put("group.id", "hot-prod-gourp");   //required
        props.put("enable.auto.commit", "false"); // 关闭自动提交
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", configurationProperties.get("autoOffsetReset"));     //从最早的消息开始读取
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");  //required
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer"); //required
        return props;
    }

}
