package com.lh.stock.stockcache.component;

import com.lh.stock.stockcache.domain.KafkaMsgContext;

/**
 * @Author: liuhai
 * @Date: 2020/6/6 16:21
 */
public interface IFreshCache<T> {
    void cacheData(KafkaMsgContext<T> msgContext);
}
