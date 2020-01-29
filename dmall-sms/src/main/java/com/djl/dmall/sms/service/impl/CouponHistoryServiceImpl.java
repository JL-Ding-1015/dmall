package com.djl.dmall.sms.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djl.dmall.sms.entity.CouponHistory;
import com.djl.dmall.sms.mapper.CouponHistoryMapper;
import com.djl.dmall.sms.service.CouponHistoryService;
import com.djl.dmall.vo.PageInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 优惠券使用、领取历史表 服务实现类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
@Service
@Component
public class CouponHistoryServiceImpl extends ServiceImpl<CouponHistoryMapper, CouponHistory> implements CouponHistoryService {

    @Autowired
    CouponHistoryMapper couponHistoryMapper;

    @Override
    public PageInfoVo listCouponHistoryForPage(Long couponId, Integer useStatus, String orderSn, Integer pageSize, Integer pageNum) {
        QueryWrapper<CouponHistory> wrapper = new QueryWrapper<>();
        if(couponId!=null){
            wrapper.eq("coupon_id",couponId);
        }
        if(useStatus!=null){
            wrapper.eq("use_status",useStatus);
        }
        if(!StringUtils.isEmpty(orderSn)){
            wrapper.eq("order_sn",orderSn);
        }
        IPage<CouponHistory> iPage = couponHistoryMapper.selectPage(new Page<CouponHistory>(pageNum, pageSize), wrapper);
        return PageInfoVo.getVo(iPage,pageSize.longValue());
    }
}
