package com.djl.dmall.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djl.dmall.pms.entity.Product;
import com.djl.dmall.pms.service.ProductService;
import com.djl.dmall.sms.entity.FlashPromotionProductRelation;
import com.djl.dmall.sms.entity.FlashPromotionSession;
import com.djl.dmall.sms.mapper.FlashPromotionProductRelationMapper;
import com.djl.dmall.sms.mapper.FlashPromotionSessionMapper;
import com.djl.dmall.sms.service.FlashPromotionProductRelationService;
import com.djl.dmall.vo.PageInfoVo;
import com.djl.dmall.vo.sms.SmsFlashPromotionProduct;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * <p>
 * 商品限时购与商品关系表 服务实现类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
@Service
@Component
public class FlashPromotionProductRelationServiceImpl extends ServiceImpl<FlashPromotionProductRelationMapper, FlashPromotionProductRelation> implements FlashPromotionProductRelationService {

    @Autowired
    FlashPromotionProductRelationMapper promotionProductRelationMapper;

    @Reference
    ProductService productService;

    @Override
    public PageInfoVo listRelationForPage(Long flashPromotionId,
                                          Long flashPromotionSessionId,
                                          Integer pageSize, Integer pageNum) {
        //sms_flash_promotion_product_relation
        QueryWrapper<FlashPromotionProductRelation> wrapper = new QueryWrapper<>();
        if (flashPromotionId != null) {
            wrapper.eq("flash_promotion_id", flashPromotionId);
        }
        if (flashPromotionSessionId != null) {
            wrapper.eq("flash_promotion_session_id", flashPromotionSessionId);
        }
        IPage<FlashPromotionProductRelation> iPage = promotionProductRelationMapper
                .selectPage(new Page<FlashPromotionProductRelation>(pageNum.longValue(), pageSize.longValue()), wrapper);
        /*for (int i = 0; i < iPage.getRecords().size(); i++) {
            FlashPromotionProductRelation flashPromotionProductRelation = iPage.getRecords().get(i);
            SmsFlashPromotionProduct smsFlashPromotionProduct = new SmsFlashPromotionProduct();
            BeanUtils.copyProperties(flashPromotionProductRelation, smsFlashPromotionProduct);
            Long productId = flashPromotionProductRelation.getProductId();
            Product product = productService.getById(productId);
            smsFlashPromotionProduct.setProduct(product);
        }*/
        IPage<SmsFlashPromotionProduct> iPage1 = new Page<>();
        BeanUtils.copyProperties(iPage,iPage1,"records");
        iPage1.setRecords(new ArrayList<>());
        for (int i = 0; i < iPage.getRecords().size(); i++) {
            FlashPromotionProductRelation productRelation = iPage.getRecords().get(i);
            Product product = productService.getById(productRelation.getProductId());
            SmsFlashPromotionProduct smsFlashPromotionProduct = new SmsFlashPromotionProduct();
            BeanUtils.copyProperties(productRelation, smsFlashPromotionProduct);
            smsFlashPromotionProduct.setProduct(product);
            iPage1.getRecords().add(smsFlashPromotionProduct);
        }
        return PageInfoVo.getVo(iPage1, pageSize.longValue());
    }
}
