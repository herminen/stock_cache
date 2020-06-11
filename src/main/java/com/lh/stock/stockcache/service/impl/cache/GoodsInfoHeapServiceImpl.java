package com.lh.stock.stockcache.service.impl.cache;

import com.lh.stock.stockcache.domain.GoodsBaseInfo;
import com.lh.stock.stockcache.service.ICacheInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.lh.stock.stockcache.constant.CacheKeyConstants.CACHE_GOODS_KEY;

/**
 * @Author: liuhai
 * @Date: 2020/6/5 15:02
 */
@Service("goodsInfoHeapService")
@CacheConfig(cacheNames = {"goodsBaseInfo"})
public class GoodsInfoHeapServiceImpl implements ICacheInfoService<GoodsBaseInfo> {

    private static Logger logger = LoggerFactory.getLogger(GoodsInfoHeapServiceImpl.class);

    @Cacheable(key = "#root.target.getCacheKey()+#cacheId" )
    public GoodsBaseInfo getCacheInfoById(Long cacheId){
        logger.warn("fetch goods info by id {}", cacheId);
        return null;
    }


    @CachePut(key = "#root.target.getCacheKey()+#cacheInfo.getGoodsId()")
    public GoodsBaseInfo updateCacheInfo(GoodsBaseInfo cacheInfo){
        logger.warn("refresh goods info  {}", cacheInfo);
        return cacheInfo;
    }

    @CacheEvict(key = "#root.target.getCacheKey()+#cacheId")
    public void removeGoodsBaseInfo(Long cacheId){
        logger.warn("remove goods info by id {}", cacheId);
    }

    @Override
    public boolean matchType(Object goodsBaseInfo) {
        return goodsBaseInfo instanceof GoodsBaseInfo;
    }

    @Override
    public String getCacheKey() {
        return CACHE_GOODS_KEY;
    }
}
