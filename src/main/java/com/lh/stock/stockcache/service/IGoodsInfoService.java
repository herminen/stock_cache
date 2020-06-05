package com.lh.stock.stockcache.service;

import com.lh.stock.stockcache.domain.GoodsBaseInfo;

import java.util.Map;

/**
 * @Author: liuhai
 * @Date: 2020/6/5 15:09
 */
public interface IGoodsInfoService {

    /**
     * 读取商品信息缓存
     * @param goodsId
     * @return
     */
    GoodsBaseInfo getGoodsBaseInfoById(Long goodsId);

    /**
     * 更新商品基本信息缓存
     * @param goodsBaseInfo
     */
    GoodsBaseInfo updateGoodsBaseInfo(GoodsBaseInfo goodsBaseInfo);

    /**
     * 删除商品基本信息
     * @param goodsId
     */
    void removeGoodsBaseInfo(Long goodsId);
}
