package com.lh.stock.stockcache;

import com.lh.stock.stockcache.kafka.Consumer;
import com.lh.stock.stockcache.zk.ZookeeperSession;
import org.apache.storm.utils.Utils;
import org.apache.zookeeper.KeeperException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: liuhai
 * @Date: 2020/6/6 13:59
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class StockCachekafkaTest {

    @Autowired
    private ZookeeperSession zookeeperSession;

    @Test
    public void testKafkaConsumer() throws InterruptedException {
        Thread.sleep(100000L);
    }

    @Test
    public void testDistributeLock() {

        try {
            zookeeperSession.acquireDistributeLock("/stock_cache/product_lock_100001");
            zookeeperSession.acquireDistributeLock("/stock_cache/product_lock_100001");
        } catch (KeeperException | InterruptedException e) {

        }
        zookeeperSession.releaseDistributeLock("/stock_cache/product_lock_100001");

    }
}
