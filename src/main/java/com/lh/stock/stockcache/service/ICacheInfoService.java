package com.lh.stock.stockcache.service;

import com.lh.stock.stockcache.domain.GoodsBaseInfo;

import java.util.Map;

/**
 * @Author: liuhai
 * @Date: 2020/6/5 15:09
 */
public interface ICacheInfoService<T> {

    /**
     * 读取商品信息缓存
     * @param cacheId
     * @return
     */
    T getCacheInfoById(Long cacheId);

    /**
     * 更新商品基本信息缓存
     * @param cacheInfo
     */
    T updateCacheInfo(T cacheInfo);

    /**
     * 删除商品基本信息
     * @param cacheId
     */
    void removeGoodsBaseInfo(Long cacheId);

    /**
     * 判断缓存类型
     * @param t
     * @return
     */
    boolean matchType(Object t);

    default String getCacheKey(){
        return "key_";
    }

}
