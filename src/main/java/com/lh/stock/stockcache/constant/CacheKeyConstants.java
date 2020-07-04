package com.lh.stock.stockcache.constant;

/**
 * @Author: liuhai
 * @Date: 2020/6/11 10:46
 */
public class CacheKeyConstants {

    public static final String CACHE_GOODS_KEY = "key_goods_";
    public static final String CACHE_PRODUCT_KEY = "key_prod_";
    public static final String CACHE_SHOP_KEY = "key_shop_";

    public static final String ZK_STOCK_CACHE = "/stock_cache";

    public static final String ZK_CACHE_LIST_NODE = ZK_STOCK_CACHE + "/hot_prod_list";
    public static final String ZK_CACHE_HOT_PROD_NODE = ZK_STOCK_CACHE + "/hot_prod_cache_";

    public static final String ZK_HOT_CHACH_LOCK = ZK_STOCK_CACHE + "/hot_prod_lock";
    public static final String ZK_CHACH_PROD_LOCK = ZK_STOCK_CACHE + "/cache_prod_lock";
    public static final String ZK_HOT_CACHE_NODE_LOCK = ZK_STOCK_CACHE + "/hot_prod_node_lock_";
}
