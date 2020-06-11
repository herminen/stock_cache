package com.lh.stock.stockcache;

import com.lh.stock.stockcache.domain.GoodsBaseInfo;
import com.lh.stock.stockcache.service.ICacheInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StockCacheApplicationTests {

    @Qualifier("goodsInfoRedisService")
    @Autowired
    private ICacheInfoService goodsInfoService;

    @Test
    void testHeapCache() {
        GoodsBaseInfo goodsBaseInfo = new GoodsBaseInfo();
        goodsBaseInfo.setGoodsId(1L);
        goodsBaseInfo.setGoodsName("Fresh trumple");

//        System.out.println(goodsInfoService.getCacheInfoById(1L));
//        System.out.println("------------------------------------------------");
//        goodsInfoService.updateCacheInfo(goodsBaseInfo);
//        System.out.println(goodsInfoService.getCacheInfoById(1L));
//        goodsInfoService.removeGoodsBaseInfo(1L);
//        System.out.println("------------------------------------------------");
//        System.out.println(goodsInfoService.getCacheInfoById(1L));
//        System.out.println("------------------------------------------------");
        System.out.println(goodsInfoService.getCacheInfoById(101L));
    }

}
