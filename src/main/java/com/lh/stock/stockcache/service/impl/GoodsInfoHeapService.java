package com.lh.stock.stockcache.service.impl;

import com.lh.stock.stockcache.domain.GoodsBaseInfo;
import com.lh.stock.stockcache.service.IGoodsInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @Author: liuhai
 * @Date: 2020/6/5 15:02
 */
@Service("goodsInfoHeapService")
@CacheConfig(cacheNames = {"goodsBaseInfo"})
public class GoodsInfoHeapService implements IGoodsInfoService {

    private static Logger logger = LoggerFactory.getLogger(GoodsInfoHeapService.class);

    @Cacheable(key = "'key_'+#goodsId" )
    public GoodsBaseInfo getGoodsBaseInfoById(Long goodsId){
        logger.warn("fetch goods info by id {}", goodsId);
        return null;
    }

    @CachePut(key = "'key_'+#goodsBaseInfo.getGoodsId()")
    public GoodsBaseInfo updateGoodsBaseInfo(GoodsBaseInfo goodsBaseInfo){
        logger.warn("refresh goods info  {}", goodsBaseInfo);
        return goodsBaseInfo;
    }

    @CacheEvict(key = "'key_'+#goodsId")
    public void removeGoodsBaseInfo(Long goodsId){
        logger.warn("remove goods info by id {}", goodsId);
    }
}
