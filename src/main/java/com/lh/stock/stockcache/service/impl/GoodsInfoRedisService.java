package com.lh.stock.stockcache.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lh.stock.stockcache.component.impl.RedisCacheComponent;
import com.lh.stock.stockcache.domain.GoodsBaseInfo;
import com.lh.stock.stockcache.service.IGoodsInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: liuhai
 * @Date: 2020/6/6 15:52
 */
@Service("goodsInfoRedisService")
public class GoodsInfoRedisService implements IGoodsInfoService {

    @Autowired
    private RedisCacheComponent redisCacheComponent;

    @Override
    public GoodsBaseInfo getGoodsBaseInfoById(Long goodsId) {
        String goodsCacheInfo = redisCacheComponent.getCacheByKey("key_" + goodsId);
        if(StringUtils.isBlank(goodsCacheInfo)) {
            return null;
        }
        return JSONObject.parseObject(goodsCacheInfo, GoodsBaseInfo.class);
    }

    @Override
    public GoodsBaseInfo updateGoodsBaseInfo(GoodsBaseInfo goodsBaseInfo) {
        redisCacheComponent.setCacheByKey("key_" + goodsBaseInfo.getGoodsId(),
                JSONObject.toJSONString(goodsBaseInfo, SerializerFeature.WriteMapNullValue));
        return goodsBaseInfo;
    }

    @Override
    public void removeGoodsBaseInfo(Long goodsId) {
        redisCacheComponent.deleteCacheByKey("key_" + goodsId);
    }
}
