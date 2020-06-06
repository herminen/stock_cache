package com.lh.stock.stockcache.component;

import com.lh.stock.stockcache.domain.KafkaMsgContext;

/**
 * @Author: liuhai
 * @Date: 2020/6/6 16:06
 */
public interface IFindFreshData<T> {

    void findData(KafkaMsgContext msgContext);

    boolean matchKafkaMsg(KafkaMsgContext<T> msgContext);
}
