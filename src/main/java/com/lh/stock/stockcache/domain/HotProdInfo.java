package com.lh.stock.stockcache.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * 用户访问产品详情页请求信息，用于记录热门产品id，生成热点缓存
 *
 * @Author: liuhai
 * @Date: 2020/6/22 11:51
 */
@Getter
@Setter
@ToString
public class HotProdInfo implements Comparable<HotProdInfo> {

    private Long prodId;

    private Long shopId;

    private Integer visitCount = 0;

    public String getProdIdString(){
        return String.valueOf(prodId);
    }

    public void increaseVisitCount(){
        visitCount++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HotProdInfo that = (HotProdInfo) o;
        return Objects.equals(prodId, that.prodId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prodId);
    }

    @Override
    public int compareTo(HotProdInfo o) {
        return o.getVisitCount().compareTo(this.getVisitCount());
    }
}
