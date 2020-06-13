package com.lh.stock.stockcache.service.impl.cache;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lh.stock.stockcache.component.impl.RedisCacheComponent;
import com.lh.stock.stockcache.domain.GoodsBaseInfo;
import com.lh.stock.stockcache.domain.ProductBaseInfo;
import com.lh.stock.stockcache.service.ICacheInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.lh.stock.stockcache.constant.CacheKeyConstants.CACHE_GOODS_KEY;
import static com.lh.stock.stockcache.constant.CacheKeyConstants.CACHE_PRODUCT_KEY;

/**
 * @Author: liuhai
 * @Date: 2020/6/6 15:52
 */
@Service("productInfoRedisService")
public class ProductInfoRedisServiceImpl implements ICacheInfoService<ProductBaseInfo> {

    @Autowired
    private RedisCacheComponent redisCacheComponent;

    @Override
    public ProductBaseInfo getCacheInfoById(Long cacheId) {
        String goodsCacheInfo = redisCacheComponent.getCacheByKey(CACHE_PRODUCT_KEY + cacheId);
        if(StringUtils.isBlank(goodsCacheInfo)) {
            return null;
        }
        return JSONObject.parseObject(goodsCacheInfo, ProductBaseInfo.class);
    }


    @Override
    public ProductBaseInfo updateCacheInfo(ProductBaseInfo cacheInfo) {
        redisCacheComponent.setCacheByKey(CACHE_PRODUCT_KEY + cacheInfo.getId(),
                JSONObject.toJSONString(cacheInfo, SerializerFeature.WriteMapNullValue));
        return cacheInfo;
    }

    @Override
    public void removeGoodsBaseInfo(Long cacheId) {
        redisCacheComponent.deleteCacheByKey(CACHE_PRODUCT_KEY + cacheId);
    }

    @Override
    public boolean matchType(Object productBaseInfo) {
        return productBaseInfo instanceof ProductBaseInfo;
    }
}
