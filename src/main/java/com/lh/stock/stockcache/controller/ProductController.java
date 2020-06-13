package com.lh.stock.stockcache.controller;

import com.alibaba.fastjson.JSONObject;
import com.lh.stock.stockcache.constant.KafkaMsgConstants;
import com.lh.stock.stockcache.domain.KafkaMsgContext;
import com.lh.stock.stockcache.domain.ProductBaseInfo;
import com.lh.stock.stockcache.domain.ShopBaseInfo;
import com.lh.stock.stockcache.kafka.Consumer;
import com.lh.stock.stockcache.kafka.Producer;
import com.lh.stock.stockcache.service.ICacheInfoService;
import com.lh.stock.stockcache.service.IFindDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import static com.lh.stock.stockcache.constant.KafkaMsgConstants.PROD_BASE_INFO;
import static com.lh.stock.stockcache.constant.KafkaMsgConstants.SHOP_BASE_INFO;

/**
 * @Author: liuhai
 * @Date: 2020/6/10 11:23
 */
@RequestMapping("/product")
@RestController
public class ProductController {

    @Autowired
    @Qualifier("productInfoHeapService")
    private ICacheInfoService productInfoHeapService;

    @Autowired
    @Qualifier("productInfoRedisService")
    private ICacheInfoService productInfoRedisService;

    @Autowired
    @Qualifier("shopInfoRedisService")
    private ICacheInfoService shopInfoRedisService;
    @Autowired
    @Qualifier("shopInfoHeapService")
    private ICacheInfoService shopInfoHeapService;

    @Autowired
    private Producer producer;
    @Autowired
    private IFindDataService findDataService;

    @GetMapping("/getProductInfo")
    @ResponseBody
    public String getProductInfo(@RequestParam("prodId") Long prodId){
        //查找redis缓存
        ProductBaseInfo productBaseInfo = (ProductBaseInfo) productInfoRedisService.getCacheInfoById(prodId);
        if(null != productBaseInfo){
            return JSONObject.toJSONString(productBaseInfo);
        }
        //查找堆缓存
        productBaseInfo = (ProductBaseInfo) productInfoHeapService.getCacheInfoById(prodId);
        if(null != productBaseInfo){
            return JSONObject.toJSONString(productBaseInfo);
        }
        //查询数据库，更新缓存
        KafkaMsgContext msgContext = new KafkaMsgContext(String.valueOf(prodId), PROD_BASE_INFO);
        producer.sendMessage(msgContext);
        return JSONObject.toJSONString(findDataService.fetchFreshData(msgContext));
    }

    @GetMapping("/getShopInfo")
    @ResponseBody
    public String getShopInfo(@RequestParam("shop") Long shop){
        //查找redis缓存
        ShopBaseInfo shopBaseInfo = (ShopBaseInfo) shopInfoRedisService.getCacheInfoById(shop);
        if(null != shopBaseInfo){
            return JSONObject.toJSONString(shopBaseInfo);
        }
        //查找堆缓存
        shopBaseInfo = (ShopBaseInfo) shopInfoHeapService.getCacheInfoById(shop);
        if(null != shopBaseInfo){
            return JSONObject.toJSONString(shopBaseInfo);
        }
        //查询数据库，更新缓存
        KafkaMsgContext msgContext = new KafkaMsgContext(String.valueOf(shop), SHOP_BASE_INFO);
        producer.sendMessage(msgContext);
        return JSONObject.toJSONString(findDataService.fetchFreshData(msgContext));
    }
}
