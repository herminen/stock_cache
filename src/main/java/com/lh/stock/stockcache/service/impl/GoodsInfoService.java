package com.lh.stock.stockcache.service.impl;

import com.lh.stock.stockcache.domain.GoodsBaseInfo;
import com.lh.stock.stockcache.service.IGoodsInfoService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: liuhai
 * @Date: 2020/6/5 15:02
 */
@Service
@CacheConfig(cacheNames = {"goodsBaseInfo"})
public class GoodsInfoService implements IGoodsInfoService {

    @Cacheable(key ="'key_'+#goodsId" )
    public GoodsBaseInfo getGoodsBaseInfoById(Long goodsId){
        return null;
    }

    @CachePut(key = "'key_'+#goodsBaseInfo.getGoodsId()")
    public GoodsBaseInfo updateGoodsBaseInfo(GoodsBaseInfo goodsBaseInfo){
        return goodsBaseInfo;
    }
}
