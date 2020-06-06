package com.lh.stock.stockcache.component;

/**
 * @Author: liuhai
 * @Date: 2020/6/3 19:34
 */
public interface ICacheComponent {
    /**
     * 查询缓存
     * @param key
     * @return
     */
    String getCacheByKey(String key);

    /**
     * 删除缓存
     * @param key
     * @return
     */
    boolean deleteCacheByKey(String key);

    /**
     * 设置缓存
     * @param key
     * @param value
     */
    void setCacheByKey(String key, String value);
}
