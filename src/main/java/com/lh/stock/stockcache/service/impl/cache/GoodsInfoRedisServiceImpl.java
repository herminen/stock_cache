package com.lh.stock.stockcache.service.impl.cache;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lh.stock.stockcache.component.impl.RedisCacheComponent;
import com.lh.stock.stockcache.domain.GoodsBaseInfo;
import com.lh.stock.stockcache.service.ICacheInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.lh.stock.stockcache.constant.CacheKeyConstants.CACHE_GOODS_KEY;

/**
 * @Author: liuhai
 * @Date: 2020/6/6 15:52
 */
@Service("goodsInfoRedisService")
public class GoodsInfoRedisServiceImpl implements ICacheInfoService<GoodsBaseInfo> {

    @Autowired
    private RedisCacheComponent redisCacheComponent;

    @Override
    public GoodsBaseInfo getCacheInfoById(Long cacheId) {
        String goodsCacheInfo = redisCacheComponent.getCacheByKey(CACHE_GOODS_KEY + cacheId);
        if(StringUtils.isBlank(goodsCacheInfo)) {
            return null;
        }
        return JSONObject.parseObject(goodsCacheInfo, GoodsBaseInfo.class);
    }


    @Override
    public GoodsBaseInfo updateCacheInfo(GoodsBaseInfo cacheInfo) {
        redisCacheComponent.setCacheByKey(CACHE_GOODS_KEY + cacheInfo.getGoodsId(),
                JSONObject.toJSONString(cacheInfo, SerializerFeature.WriteMapNullValue));
        return cacheInfo;
    }

    @Override
    public void removeGoodsBaseInfo(Long cacheId) {
        redisCacheComponent.deleteCacheByKey(CACHE_GOODS_KEY + cacheId);
    }

    @Override
    public boolean matchType(Object goodsBaseInfo) {
        return goodsBaseInfo instanceof GoodsBaseInfo;
    }
}
