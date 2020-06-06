package com.lh.stock.stockcache.kafka;

import com.alibaba.fastjson.JSONObject;
import com.lh.stock.stockcache.component.IFindFreshData;
import com.lh.stock.stockcache.component.IFreshCache;
import com.lh.stock.stockcache.domain.KafkaMsgContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: liuhai
 * @Date: 2020/6/6 11:49
 */
@Service
public class Consumer implements ApplicationContextAware {
    private final Logger logger = LoggerFactory.getLogger(Consumer.class);

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @KafkaListener(id = "stock-cache-group1", topics={"${kafka.stock.topic}"}, properties={"'subscriptionType':'USER_ASSIGNED'"})
    public void consume(ConsumerRecord<Integer, String> record){
        logger.info(String.format("$$ -> Consumed Message -> %s",record.value()));
        if(StringUtils.isBlank(record.value())){
            return;
        }
        //解析消息
        KafkaMsgContext msgContext = JSONObject.parseObject(record.value(), KafkaMsgContext.class);
        //调用接口查询数据
        fetchFreshData(msgContext);
        //操作缓存
        cacheData(msgContext);

    }

    /**
     * 保存进对缓存和redis缓存
     * @param msgContext
     */
    private void cacheData(KafkaMsgContext msgContext) {
        Map<String, IFreshCache> freshCacheDataMap = applicationContext.getBeansOfType(IFreshCache.class);
        for (IFreshCache freshCacheData : freshCacheDataMap.values()) {
            if(freshCacheData.matchKafkaMsg(msgContext)){
                freshCacheData.cacheData(msgContext);
            }
        }
    }

    /**
     * 查找需要更新缓存的数据，可能会查数据库，调服务化接口等
     * @param msgContext
     */
    private void fetchFreshData(KafkaMsgContext msgContext) {
        Map<String, IFindFreshData> findCacheDataMap = applicationContext.getBeansOfType(IFindFreshData.class);
        for (IFindFreshData findCacheData : findCacheDataMap.values()) {
            if(findCacheData.matchKafkaMsg(msgContext)){
                findCacheData.findData(msgContext);
                return;
            }
        }
    }
}
