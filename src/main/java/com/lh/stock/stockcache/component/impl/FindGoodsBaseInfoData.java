package com.lh.stock.stockcache.component.impl;

import com.lh.stock.stockcache.component.IFindFreshData;
import com.lh.stock.stockcache.domain.GoodsBaseInfo;
import com.lh.stock.stockcache.domain.KafkaMsgContext;
import org.springframework.stereotype.Service;

/**
 * @Author: liuhai
 * @Date: 2020/6/6 16:08
 */
@Service("findGoodsBaseInfoData")
public class FindGoodsBaseInfoData implements IFindFreshData<GoodsBaseInfo> {
    private String matchTag = "goodsBaseInfo";

    @Override
    public void findData(KafkaMsgContext msgContext) {
        GoodsBaseInfo goodsBaseInfo = new GoodsBaseInfo();
        goodsBaseInfo.setGoodsId(101L);
        goodsBaseInfo.setGoodsName("双子星大厦");
        msgContext.setCacheData(goodsBaseInfo);
    }

    @Override
    public boolean matchKafkaMsg(KafkaMsgContext msgContext) {
        return msgContext.matchMsg(matchTag);
    }
}
