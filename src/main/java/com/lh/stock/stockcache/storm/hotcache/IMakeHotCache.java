package com.lh.stock.stockcache.storm.hotcache;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: liuhai
 * Date: 2020/6/25
 * Time: 23:06
 * Description: No Description
 */
public interface IMakeHotCache<T> {

    /**
     * 构造缓存对象
     * @return
     */
    List<T> makeCache();
}
