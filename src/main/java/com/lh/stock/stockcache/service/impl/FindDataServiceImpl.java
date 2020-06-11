package com.lh.stock.stockcache.service.impl;

import com.lh.stock.stockcache.component.IFindFreshData;
import com.lh.stock.stockcache.domain.KafkaMsgContext;
import com.lh.stock.stockcache.service.IFindDataService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: liuhai
 * @Date: 2020/6/11 14:06
 */
@Service
public class FindDataServiceImpl implements ApplicationContextAware, IFindDataService {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 查找需要更新缓存的数据，可能会查数据库，调服务化接口等
     * @param msgContext
     */
    @Override
    public <T> T fetchFreshData(KafkaMsgContext<T> msgContext) {
        Map<String, IFindFreshData> findCacheDataMap = applicationContext.getBeansOfType(IFindFreshData.class);
        for (IFindFreshData findCacheData : findCacheDataMap.values()) {
            if(findCacheData.matchKafkaMsg(msgContext)){
                return (T) findCacheData.findData(msgContext);
            }
        }
        return null;
    }
}
