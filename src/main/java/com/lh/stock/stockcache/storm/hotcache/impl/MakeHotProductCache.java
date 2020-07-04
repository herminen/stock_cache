package com.lh.stock.stockcache.storm.hotcache.impl;

import com.google.common.collect.Lists;
import com.lh.stock.stockcache.domain.HotProdInfo;
import com.lh.stock.stockcache.storm.hotcache.IMakeHotCache;
import org.apache.commons.collections4.MapUtils;
import org.apache.storm.trident.util.LRUMap;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

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
        TreeSet<HotProdInfo> sortedList = new TreeSet<>((o1, o2) -> o2.getVisitCount().compareTo(o1.getVisitCount()));
        hotProdInfoLRUMap.values().stream().forEachOrdered(hotProdInfo -> {sortedList.add(hotProdInfo);});
        return Lists.newArrayList(sortedList);
    }
}
