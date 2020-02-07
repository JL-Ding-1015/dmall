package com.djl.dmall.cart.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Setter
public class Cart implements Serializable {
    @Getter
    private List<CartItem> cartItems;

    private Integer count = 0; //所有商品的个数

    private BigDecimal totalPrice; //所有商品测总价格

    public Integer getCount() {
        if (cartItems != null) {
            AtomicInteger all = new AtomicInteger(0);
            cartItems.forEach((cartItem) -> {
                all.getAndAdd(cartItem.getCount());
            });

            return all.get();
        } else {
            return 0;
        }

    }

    public BigDecimal getTotalPrice() {
        BigDecimal allPrice = new BigDecimal(0);
        if (cartItems != null) {
            for (CartItem cartItem : cartItems) {
                allPrice = allPrice.add(cartItem.getItemPrice());
            }
        }
        return allPrice;
    }
}
