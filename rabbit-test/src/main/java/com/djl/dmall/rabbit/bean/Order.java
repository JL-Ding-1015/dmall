package com.djl.dmall.rabbit.bean;

import java.io.Serializable;

public class Order implements Serializable {

    private String orderSn;
    private Long skuId;
    private Integer num;
    private Integer memberId;

    public Order() {
    }

    public Order(String orderSn, Long skuId, Integer num, Integer memberId) {
        this.orderSn = orderSn;
        this.skuId = skuId;
        this.num = num;
        this.memberId = memberId;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }
}
