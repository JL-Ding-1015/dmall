package com.djl.dmall.cart.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CartResponse implements Serializable {
    private Cart cart; //整个购物车
    private CartItem cartItem; //一条购物信息
    private String cartKey; //临时购物车的key
}
