package com.djl.dmall.vo.order;

import com.djl.dmall.cart.vo.CartItem;
import com.djl.dmall.sms.entity.Coupon;
import com.djl.dmall.ums.entity.MemberReceiveAddress;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderConfirmVo implements Serializable {
    //购物车项目的信息
    private List<CartItem> cartItemList;
    //地址列表
    List<MemberReceiveAddress> addressList;
    //优惠券信息
    private List<Coupon> couponList;
    //其他一些......

    //订单令牌，前端要妥善保存，后续步骤要用
    private String orderToken;

    //商品总价
    private BigDecimal productTotalPrice = new BigDecimal("0");
    //总价
    private BigDecimal totalPrice = new BigDecimal("0");
    //商品总数
    private Integer count = 0;
    //优惠减免
    private BigDecimal couponDiscount = new BigDecimal("0");
    //运费
    private BigDecimal transPrice = new BigDecimal("0");

}
