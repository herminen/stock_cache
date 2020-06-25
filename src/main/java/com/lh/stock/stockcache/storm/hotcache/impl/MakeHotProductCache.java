package com.lh.stock.stockcache.storm.hotcache.impl;

import com.lh.stock.stockcache.domain.HotProdInfo;
import com.lh.stock.stockcache.storm.hotcache.IMakeHotCache;
import org.apache.storm.trident.util.LRUMap;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ASUS
 * Date: 2020/6/25
 * Time: 23:08
 * Description: No Description
 */
public class MakeHotProductCache implements IMakeHotCache<HotProdInfo> {

    private LRUMap<Long, HotProdInfo> hotProdInfoLRUMap;

    public MakeHotProductCache(LRUMap<Long, HotProdInfo> hotProdInfoLRUCache) {
        this.hotProdInfoLRUMap = hotProdInfoLRUCache;
    }

    @Override
    public List<HotProdInfo> makeCache() {
        return null;
    }
}
