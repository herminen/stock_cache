package com.lh.stock.stockcache.component.impl;

import com.lh.stock.stockcache.component.ICacheComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

/**
 * @Author: liuhai
 * @Date: 2020/6/3 19:35
 */
@Component("jedisCacheComponent")
public class RedisCacheComponent implements ICacheComponent {
    @Autowired
    private JedisCluster jedisCluster;

    @Override
    public String getCacheByKey(String key) {
        return jedisCluster.get(key);
    }

    @Override
    public boolean deleteCacheByKey(String key) {
        return jedisCluster.del(key) > 0;
    }

    public void setCacheByKey(String key, String value){
        jedisCluster.set(key,value);
    }
}
