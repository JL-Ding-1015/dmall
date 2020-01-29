package com.djl.dmall.vo.sms;

import com.djl.dmall.pms.entity.Product;
import com.djl.dmall.sms.entity.FlashPromotionProductRelation;
import lombok.Getter;
import lombok.Setter;

/**
 * 限时购及商品信息封装
 */
public class SmsFlashPromotionProduct extends FlashPromotionProductRelation {
    @Getter
    @Setter
    private Product product;
}
