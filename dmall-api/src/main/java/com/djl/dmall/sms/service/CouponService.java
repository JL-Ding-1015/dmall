package com.djl.dmall.sms.service;

import com.djl.dmall.sms.entity.Coupon;
import com.baomidou.mybatisplus.extension.service.IService;
import com.djl.dmall.vo.PageInfoVo;
import com.djl.dmall.vo.sms.SmsCouponParam;

import java.util.List;

/**
 * <p>
 * 优惠卷表 服务类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
public interface CouponService extends IService<Coupon> {

    /**
     * 根据名称等条件分页查询优惠券
     * @param name
     * @param type
     * @param pageSize
     * @param pageNum
     * @return
     */
    PageInfoVo listForPage(String name, Integer type, Integer pageSize, Integer pageNum);

    /**
     * 根据id修改优惠券
     * @param id
     * @param couponParam
     * @return
     */
    int updateCouponInfos(Long id, SmsCouponParam couponParam);

    /**
     * 插入新的优惠券
     * @param couponParam
     * @return
     */
    int create(SmsCouponParam couponParam);

    /**
     * 查看优惠券的信息
     * @param id
     * @return
     */
    SmsCouponParam getCouponItemInfo(Long id);

    /**
     * 根据用户id查看没使用的优惠券
     * @param id
     * @return
     */
    List<Coupon> getCouponListByMemberId(Long id);
}
