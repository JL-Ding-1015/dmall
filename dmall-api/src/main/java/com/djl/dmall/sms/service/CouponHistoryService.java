package com.djl.dmall.sms.service;

import com.djl.dmall.sms.entity.CouponHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.djl.dmall.vo.PageInfoVo;

/**
 * <p>
 * 优惠券使用、领取历史表 服务类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
public interface CouponHistoryService extends IService<CouponHistory> {

    PageInfoVo listCouponHistoryForPage(Long couponId, Integer useStatus, String orderSn, Integer pageSize, Integer pageNum);

}
