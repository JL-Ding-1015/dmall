package com.djl.dmall.sms.service;

import com.djl.dmall.sms.entity.FlashPromotion;
import com.baomidou.mybatisplus.extension.service.IService;
import com.djl.dmall.vo.PageInfoVo;

/**
 * <p>
 * 限时购表 服务类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
public interface FlashPromotionService extends IService<FlashPromotion> {

    /**
     * 根据关键字（没有关键字及查询所有），查询符合条件的FlashPromotion
     * @param keyword
     * @param pageSize
     * @param pageNum
     * @return
     */
    PageInfoVo listFlashPromotionForPage(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 改变FlashPromotion的上下线状态
     * @param id
     * @param status
     * @return
     */
    int updateStatus(Long id, Integer status);

    /**
     * 添加FlashPromotion活动
     * @param flashPromotion
     * @return
     */
    int createFlashPromotion(FlashPromotion flashPromotion);

}
