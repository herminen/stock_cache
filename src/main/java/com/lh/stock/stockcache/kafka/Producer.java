package com.lh.stock.stockcache.kafka;

import com.alibaba.fastjson.JSONObject;
import com.lh.stock.stockcache.domain.KafkaMsgContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author: liuhai
 * @Date: 2020/6/6 9:23
 */
@Service
public class Producer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    @Autowired
    private KafkaTemplate<String, String> stringKafkaTemplate;

    @Value("${kafka.stock.topic}")
    private String topic;


    public void sendMessage(KafkaMsgContext message){
        String contextMessage = JSONObject.toJSONString(message);
        logger.info(String.format("$$ -> Producing message --> %s",contextMessage));
        this.stringKafkaTemplate.send(topic, contextMessage);
    }
}
