package com.lh.stock.stockcache;

import com.lh.stock.stockcache.domain.GoodsBaseInfo;
import com.lh.stock.stockcache.service.IGoodsInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StockCacheApplicationTests {

    @Qualifier("goodsInfoRedisService")
    @Autowired
    private IGoodsInfoService goodsInfoService;

    @Test
    void testHeapCache() {
        GoodsBaseInfo goodsBaseInfo = new GoodsBaseInfo();
        goodsBaseInfo.setGoodsId(1L);
        goodsBaseInfo.setGoodsName("Fresh trumple");

//        System.out.println(goodsInfoService.getGoodsBaseInfoById(1L));
//        System.out.println("------------------------------------------------");
//        goodsInfoService.updateGoodsBaseInfo(goodsBaseInfo);
//        System.out.println(goodsInfoService.getGoodsBaseInfoById(1L));
//        goodsInfoService.removeGoodsBaseInfo(1L);
//        System.out.println("------------------------------------------------");
//        System.out.println(goodsInfoService.getGoodsBaseInfoById(1L));
//        System.out.println("------------------------------------------------");
        System.out.println(goodsInfoService.getGoodsBaseInfoById(101L));
    }

}
