package com.lh.stock.stockcache.system;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author: liuhai
 * @Date: 2020/6/2 20:48
 */
@SpringBootConfiguration
public class JedisClusterConfig {

    @Bean
    public JedisCluster jedisCluster(JedisConnectionFactory jedisConnectionFactory) {
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();
        RedisClusterConfiguration clusterConfiguration = jedisConnectionFactory.getClusterConfiguration();

        for (RedisNode redisNode : clusterConfiguration.getClusterNodes()) {
            nodes.add(new HostAndPort(redisNode.getHost().trim(), redisNode.getPort()));
        }
        return new JedisCluster(nodes, clusterConfiguration.getMaxRedirects());
    }
}
