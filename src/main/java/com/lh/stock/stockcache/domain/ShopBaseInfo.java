package com.lh.stock.stockcache.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: liuhai
 * @Date: 2020/6/11 9:14
 */
@Getter
@Setter
@ToString
public class ShopBaseInfo {

    private Long id;

    private Integer level;

    private Double goodCommentRate;
}
