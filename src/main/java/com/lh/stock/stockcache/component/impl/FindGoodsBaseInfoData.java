package com.lh.stock.stockcache.component.impl;

import com.lh.stock.stockcache.component.IFindFreshData;
import com.lh.stock.stockcache.domain.GoodsBaseInfo;
import com.lh.stock.stockcache.domain.KafkaMsgContext;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Service;

import static com.lh.stock.stockcache.constant.KafkaMsgConstants.GOODS_BASE_INFO;

/**
 * @Author: liuhai
 * @Date: 2020/6/6 16:08
 */
@Service("findGoodsBaseInfoData")
public class FindGoodsBaseInfoData implements IFindFreshData<GoodsBaseInfo> {

    @Override
    public GoodsBaseInfo findData(KafkaMsgContext msgContext) {
        GoodsBaseInfo goodsBaseInfo = new GoodsBaseInfo();
        goodsBaseInfo.setGoodsId(NumberUtils.toLong( msgContext.getRequestId()));
        goodsBaseInfo.setGoodsName("双子星大厦");
        return goodsBaseInfo;
    }

    @Override
    public boolean matchKafkaMsg(KafkaMsgContext msgContext) {
        return msgContext.matchMsg(GOODS_BASE_INFO);
    }

}
