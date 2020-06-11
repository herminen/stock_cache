package com.lh.stock.stockcache.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Author: liuhai
 * @Date: 2020/6/11 9:08
 */
@Getter
@Setter
@ToString
public class ProductBaseInfo {

    private Long id;

    private String name;

    private Double price;

    private List<String> pictureList;

    private String specification;

    private String service;

    private String color;

    private String size;
}
