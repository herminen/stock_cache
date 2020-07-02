package com.lh.stock.stockcache;

import com.lh.stock.stockcache.util.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@SpringBootApplication
@EnableCaching
@EnableKafka
public class StockCacheApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(StockCacheApplication.class, args);
        SpringContextUtil.setApplicationContext(applicationContext);
    }

}
