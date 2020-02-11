package com.djl.dmall.cart.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 购物项
 */
@Setter
public class CartItem implements Serializable {
    //当前购物项
    @Getter
    private Long skuId;
    @Getter
    private Long productId;
    @Getter
    private String name;
    @Getter
    private String skuCode;
    @Getter
    private Integer stock;
    @Getter
    private String sp1;
    @Getter
    private String sp2;
    @Getter
    private String sp3;
    @Getter
    private String pic;
    @Getter
    private BigDecimal price;
    @Getter
    private BigDecimal promotionPrice;
    @Getter
    private Integer lockStock;


    private BigDecimal itemPrice;//当前购物项的总价
    @Getter
    private Integer count;//每个物品多少个
    @Getter
    private boolean check = true;//此种商品是否选中


    public BigDecimal getItemPrice() {
        BigDecimal calcPrice = price.multiply(new BigDecimal(count));

        return calcPrice;
    }


}
