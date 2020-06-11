package com.lh.stock.stockcache.component.impl;

import com.lh.stock.stockcache.component.IFreshCache;
import com.lh.stock.stockcache.domain.GoodsBaseInfo;
import com.lh.stock.stockcache.domain.KafkaMsgContext;
import com.lh.stock.stockcache.service.ICacheInfoService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: liuhai
 * @Date: 2020/6/6 16:22
 */
@Service
public class FreshCacheInfoCache implements IFreshCache, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void cacheData(KafkaMsgContext msgContext) {
        Map<String, ICacheInfoService> goodsInfoServiceMap = applicationContext.getBeansOfType(ICacheInfoService.class);
        for (ICacheInfoService cacheInfoService : goodsInfoServiceMap.values()) {
            if(cacheInfoService.matchType(msgContext.getCacheData())){
                cacheInfoService.updateCacheInfo(msgContext.getCacheData());
            }
        }
    }

}
