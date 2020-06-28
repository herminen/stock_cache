package com.lh.stock.stockcache.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户访问产品详情页请求信息，用于记录热门产品id，生成热点缓存
 *
 * @Author: liuhai
 * @Date: 2020/6/22 11:51
 */
@Getter
@Setter
@ToString
public class HotProdInfo {

    private Long prodId;

    private Long shopId;

    private Integer visitCount;

    public String getProdIdString(){
        return String.valueOf(prodId);
    }

    public void increaseVisitCount(){
        visitCount++;
    }

}
