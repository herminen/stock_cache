package com.lh.stock.stockcache.component.impl;

import com.lh.stock.stockcache.component.IFreshCache;
import com.lh.stock.stockcache.domain.GoodsBaseInfo;
import com.lh.stock.stockcache.domain.KafkaMsgContext;
import com.lh.stock.stockcache.service.IGoodsInfoService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: liuhai
 * @Date: 2020/6/6 16:22
 */
@Service("freshGoodsBaseInfoCache")
public class FreshGoodsBaseInfoCache implements IFreshCache<GoodsBaseInfo>, ApplicationContextAware {

    private String matchTag = "goodsBaseInfo";

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void cacheData(KafkaMsgContext<GoodsBaseInfo> msgContext) {
        Map<String, IGoodsInfoService> goodsInfoServiceMap = applicationContext.getBeansOfType(IGoodsInfoService.class);
        for (IGoodsInfoService goodsInfoService : goodsInfoServiceMap.values()) {
            goodsInfoService.updateGoodsBaseInfo(msgContext.getCacheData());
        }
    }

    @Override
    public boolean matchKafkaMsg(KafkaMsgContext<GoodsBaseInfo> msgContext) {
        return msgContext.matchMsg(matchTag);
    }
}
