package com.lh.stock.stockcache.service.impl.cache;

import com.lh.stock.stockcache.domain.ShopBaseInfo;
import com.lh.stock.stockcache.service.ICacheInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.lh.stock.stockcache.constant.CacheKeyConstants.CACHE_SHOP_KEY;

/**
 * @Author: liuhai
 * @Date: 2020/6/5 15:02
 */
@Service("shopInfoHeapService")
@CacheConfig(cacheNames = {"shopBaseInfo"})
public class ShopInfoHeapServiceImpl implements ICacheInfoService<ShopBaseInfo> {

    private static Logger logger = LoggerFactory.getLogger(ShopInfoHeapServiceImpl.class);

    @Cacheable(key = "#root.target.getCacheKey()+#cacheId" )
    public ShopBaseInfo getCacheInfoById(Long cacheId){
        logger.warn("fetch shop info by id {}", cacheId);
        return null;
    }


    @CachePut(key = "#root.target.getCacheKey()+#cacheInfo.getId()")
    public ShopBaseInfo updateCacheInfo(ShopBaseInfo cacheInfo){
        logger.warn("refresh shop info  {}", cacheInfo);
        return cacheInfo;
    }

    @CacheEvict(key = "#root.target.getCacheKey()+#cacheId")
    public void removeGoodsBaseInfo(Long cacheId){
        logger.warn("remove shop info by id {}", cacheId);
    }

    @Override
    public boolean matchType(Object shopBaseInfo) {
        return shopBaseInfo instanceof ShopBaseInfo;
    }

    @Override
    public String getCacheKey() {
        return CACHE_SHOP_KEY;
    }
}
