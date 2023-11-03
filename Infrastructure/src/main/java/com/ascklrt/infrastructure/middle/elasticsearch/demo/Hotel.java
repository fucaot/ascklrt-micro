package com.ascklrt.infrastructure.middle.elasticsearch.demo;

import lombok.Data;

@Data
public class Hotel {

    private Long id;

    private String name;

    private String address;

    private Integer price;

    /**
     * 得分
     */
    private Integer score;

    /**
     * 品牌
     */
    private String brand;

    private String city;

    /**
     * 星级
     */
    private String starName;

    /**
     * 商圈
     */
    private String business;

    /**
     * 经度
     */
    private String location;

    private String pic;
}
