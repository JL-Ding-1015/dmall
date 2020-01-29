package com.djl.dmall.sms.service;

import com.djl.dmall.sms.entity.FlashPromotionSession;
import com.baomidou.mybatisplus.extension.service.IService;
import com.djl.dmall.vo.PageInfoVo;

/**
 * <p>
 * 限时购场次表 服务类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
public interface FlashPromotionSessionService extends IService<FlashPromotionSession> {

    /**
     * 查询限时促销的信息
     * @param flashPromotionId
     * @return
     */
    PageInfoVo selectListForPage(Long flashPromotionId);

    /**
     * 修改限时促销的使用状态
     * @param id
     * @param status
     */
    void updateStatus(Long id, Integer status);
}
