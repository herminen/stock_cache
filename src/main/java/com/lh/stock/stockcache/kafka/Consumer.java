package com.lh.stock.stockcache.kafka;

import com.alibaba.fastjson.JSONObject;
import com.lh.stock.stockcache.component.IFreshCache;
import com.lh.stock.stockcache.domain.KafkaMsgContext;
import com.lh.stock.stockcache.service.IFindDataService;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

/**
 * @Author: liuhai
 * @Date: 2020/6/6 11:49
 */
@Service
public class Consumer {
    private final Logger logger = LoggerFactory.getLogger(Consumer.class);

    @Autowired
    private IFreshCache freshCache;

    @Autowired
    private IFindDataService findDataService;


    @KafkaListener(id = "stock-cache-group1", topics={"${kafka.stock.topic}"})
    public void consume(ConsumerRecord<Integer, String> record, Acknowledgment ack){
        logger.info(String.format("$$ -> Consumed Message -> %s",record.value()));
        if(StringUtils.isBlank(record.value())){
            return;
        }
        try {
            //解析消息
            KafkaMsgContext msgContext = JSONObject.parseObject(record.value(), KafkaMsgContext.class);
            //调用接口查询数据
            msgContext.setCacheData(findDataService.fetchFreshData(msgContext));
            //操作缓存
            freshCache.cacheData(msgContext);
            ack.acknowledge();
        } catch (Exception e){
            logger.error("deal kafka msg error.", e);
            ack.acknowledge();
        }
    }

}
