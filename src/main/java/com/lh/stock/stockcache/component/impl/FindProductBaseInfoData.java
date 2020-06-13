package com.lh.stock.stockcache.component.impl;

import com.google.common.collect.Lists;
import com.lh.stock.stockcache.component.IFindFreshData;
import com.lh.stock.stockcache.domain.KafkaMsgContext;
import com.lh.stock.stockcache.domain.ProductBaseInfo;
import org.springframework.stereotype.Service;

import static com.lh.stock.stockcache.constant.KafkaMsgConstants.PROD_BASE_INFO;

/**
 * @Author: liuhai
 * @Date: 2020/6/11 9:17
 */
@Service("findProductBaseInfoData")
public class FindProductBaseInfoData implements IFindFreshData<ProductBaseInfo> {

    @Override
    public ProductBaseInfo findData(KafkaMsgContext msgContext) {
        //查询数据库或者调用rpc接口，这边简化一下- -！
        ProductBaseInfo productBaseInfo = new ProductBaseInfo();
        productBaseInfo.setId(101L);
        productBaseInfo.setColor("blue");
        productBaseInfo.setName("4008");
        productBaseInfo.setPictureList(Lists.newArrayList("image1.jpg", "image3.jpg"));
        productBaseInfo.setPrice(28.9D);
        productBaseInfo.setService("代驾");
        productBaseInfo.setSize("4900");
        productBaseInfo.setSpecification("舒适");
        return productBaseInfo;
    }

    @Override
    public boolean matchKafkaMsg(KafkaMsgContext<ProductBaseInfo> msgContext) {
        return msgContext.matchMsg(PROD_BASE_INFO);
    }
}
