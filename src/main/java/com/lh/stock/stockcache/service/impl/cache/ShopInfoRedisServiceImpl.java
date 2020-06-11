package com.lh.stock.stockcache.service.impl.cache;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lh.stock.stockcache.component.impl.RedisCacheComponent;
import com.lh.stock.stockcache.domain.ProductBaseInfo;
import com.lh.stock.stockcache.domain.ShopBaseInfo;
import com.lh.stock.stockcache.service.ICacheInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.lh.stock.stockcache.constant.CacheKeyConstants.CACHE_PRODUCT_KEY;
import static com.lh.stock.stockcache.constant.CacheKeyConstants.CACHE_SHOP_KEY;

/**
 * @Author: liuhai
 * @Date: 2020/6/6 15:52
 */
@Service("shopInfoRedisService")
public class ShopInfoRedisServiceImpl implements ICacheInfoService<ShopBaseInfo> {

    @Autowired
    private RedisCacheComponent redisCacheComponent;

    @Override
    public ShopBaseInfo getCacheInfoById(Long cacheId) {
        String shopCacheInfo = redisCacheComponent.getCacheByKey(CACHE_SHOP_KEY + cacheId);
        if(StringUtils.isBlank(shopCacheInfo)) {
            return null;
        }
        return JSONObject.parseObject(shopCacheInfo, ShopBaseInfo.class);
    }


    @Override
    public ShopBaseInfo updateCacheInfo(ShopBaseInfo cacheInfo) {
        redisCacheComponent.setCacheByKey(CACHE_SHOP_KEY + cacheInfo.getId(),
                JSONObject.toJSONString(cacheInfo, SerializerFeature.WriteMapNullValue));
        return cacheInfo;
    }

    @Override
    public void removeGoodsBaseInfo(Long cacheId) {
        redisCacheComponent.deleteCacheByKey(CACHE_SHOP_KEY + cacheId);
    }

    @Override
    public boolean matchType(Object shopBaseInfo) {
        return shopBaseInfo instanceof ShopBaseInfo;
    }
}
