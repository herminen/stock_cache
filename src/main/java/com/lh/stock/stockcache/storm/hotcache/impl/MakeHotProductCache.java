package com.lh.stock.stockcache.storm.hotcache.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lh.stock.stockcache.domain.HotProdInfo;
import com.lh.stock.stockcache.storm.hotcache.IMakeHotCache;
import org.apache.commons.collections4.MapUtils;
import org.apache.storm.trident.util.LRUMap;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: liuhai
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
        if(MapUtils.isEmpty(hotProdInfoLRUMap)){
            return Collections.emptyList();
        }
        Set<HotProdInfo> prodInfoSet = Sets.newHashSet();
        hotProdInfoLRUMap.values().stream().forEachOrdered(hotProdInfo -> {prodInfoSet.add(hotProdInfo);});
        List<HotProdInfo> hotProdInfos = Lists.newArrayList(prodInfoSet);
        Collections.sort(hotProdInfos);
        return hotProdInfos;
    }
}
