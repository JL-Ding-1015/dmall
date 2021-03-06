package com.djl.dmall.sms.service;

import com.djl.dmall.sms.entity.FlashPromotionProductRelation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.djl.dmall.vo.PageInfoVo;

/**
 * <p>
 * 商品限时购与商品关系表 服务类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
public interface FlashPromotionProductRelationService extends IService<FlashPromotionProductRelation> {

    PageInfoVo listRelationForPage(Long flashPromotionId, Long flashPromotionSessionId, Integer pageSize, Integer pageNum);
}
