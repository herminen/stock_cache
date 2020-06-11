package com.lh.stock.stockcache.service.impl.cache;

import com.lh.stock.stockcache.domain.ProductBaseInfo;
import com.lh.stock.stockcache.service.ICacheInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.lh.stock.stockcache.constant.CacheKeyConstants.CACHE_PRODUCT_KEY;

/**
 * @Author: liuhai
 * @Date: 2020/6/5 15:02
 */
@Service("productInfoHeapService")
@CacheConfig(cacheNames = {"productBaseInfo"})
public class ProductInfoHeapServiceImpl implements ICacheInfoService<ProductBaseInfo> {

    private static Logger logger = LoggerFactory.getLogger(ProductInfoHeapServiceImpl.class);

    @Cacheable(key = "#root.target.getCacheKey()+#cacheId" )
    public ProductBaseInfo getCacheInfoById(Long cacheId){
        logger.warn("fetch product info by id {}", cacheId);
        return null;
    }


    @CachePut(key = "#root.target.getCacheKey()+#cacheInfo.getId()")
    public ProductBaseInfo updateCacheInfo(ProductBaseInfo cacheInfo){
        logger.warn("refresh product info  {}", cacheInfo);
        return cacheInfo;
    }

    @CacheEvict(key =  "#root.target.getCacheKey()+#cacheId")
    public void removeGoodsBaseInfo(Long cacheId){
        logger.warn("remove product info by id {}", cacheId);
    }

    @Override
    public boolean matchType(Object productBaseInfo) {
        return productBaseInfo instanceof ProductBaseInfo;
    }

    @Override
    public String getCacheKey() {
        return CACHE_PRODUCT_KEY;
    }
}
