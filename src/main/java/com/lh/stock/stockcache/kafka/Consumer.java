package com.lh.stock.stockcache.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * @Author: liuhai
 * @Date: 2020/6/6 11:49
 */
@Service
public class Consumer {
    private final Logger logger = LoggerFactory.getLogger(Consumer.class);

    @KafkaListener(id = "stock-cache-group1", topics={"${kafka.stock.topic}"}, properties={"'subscriptionType':'USER_ASSIGNED'"})
    public void consume(String msg){
        logger.info(String.format("$$ -> Consumed Message -> %s",msg));
    }
}
