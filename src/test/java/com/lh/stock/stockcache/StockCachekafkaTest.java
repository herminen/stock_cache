package com.lh.stock.stockcache;

import com.lh.stock.stockcache.kafka.Consumer;
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


    @Test
    public void testKafkaConsumer() throws InterruptedException {
        Thread.sleep(100000L);
    }
}
