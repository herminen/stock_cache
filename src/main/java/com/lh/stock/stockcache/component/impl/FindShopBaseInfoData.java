package com.lh.stock.stockcache.component.impl;

import com.lh.stock.stockcache.component.IFindFreshData;
import com.lh.stock.stockcache.domain.KafkaMsgContext;
import com.lh.stock.stockcache.domain.ShopBaseInfo;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Service;

import static com.lh.stock.stockcache.constant.KafkaMsgConstants.PROD_BASE_INFO;
import static com.lh.stock.stockcache.constant.KafkaMsgConstants.SHOP_BASE_INFO;

/**
 * @Author: liuhai
 * @Date: 2020/6/11 9:23
 */
@Service("findShopBaseInfoData")
public class FindShopBaseInfoData implements IFindFreshData<ShopBaseInfo> {

    @Override
    public ShopBaseInfo findData(KafkaMsgContext msgContext) {
        //查询数据库或者调用rpc接口，这边简化一下- -！
        ShopBaseInfo shopBaseInfo = new ShopBaseInfo();
        shopBaseInfo.setId(NumberUtils.toLong( msgContext.getRequestId()));
        shopBaseInfo.setGoodCommentRate(192.8D);
        shopBaseInfo.setLevel(12);
        return shopBaseInfo;
    }

    @Override
    public boolean matchKafkaMsg(KafkaMsgContext<ShopBaseInfo> msgContext) {
        return msgContext.matchMsg(SHOP_BASE_INFO);
    }
}
