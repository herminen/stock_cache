package com.lh.stock.stockcache.service;

import com.lh.stock.stockcache.domain.KafkaMsgContext;

/**
 * @Author: liuhai
 * @Date: 2020/6/11 14:10
 */
public interface IFindDataService {
    <T> T fetchFreshData(KafkaMsgContext<T> msgContext);
}
