package com.djl.dmall.vo.order;

import com.djl.dmall.cart.vo.CartItem;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


@Data
public class OrderCreateVo implements Serializable {

    private String orderSn;
    private BigDecimal totalPrice;

    private Long addressId;
    private Long memberId;
    private String detailInfo;

    private List<CartItem> cartItemList;

    private Boolean limit = true;//验证价格
    private String token;//防重复
}



