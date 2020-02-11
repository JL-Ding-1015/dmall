package com.djl.dmall.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djl.dmall.sms.entity.Coupon;
import com.djl.dmall.sms.entity.CouponProductCategoryRelation;
import com.djl.dmall.sms.entity.CouponProductRelation;
import com.djl.dmall.sms.mapper.CouponMapper;
import com.djl.dmall.sms.mapper.CouponProductCategoryRelationMapper;
import com.djl.dmall.sms.mapper.CouponProductRelationMapper;
import com.djl.dmall.sms.service.CouponService;
import com.djl.dmall.vo.PageInfoVo;
import com.djl.dmall.vo.sms.SmsCouponParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 优惠卷表 服务实现类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
@Service
@Component
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {

    @Autowired
    CouponMapper couponMapper;

    @Autowired
    CouponProductCategoryRelationMapper productCategoryRelationMapper;

    @Autowired
    CouponProductRelationMapper productRelationMapper;

    @Override
    public PageInfoVo listForPage(String name, Integer type, Integer pageSize, Integer pageNum) {
        QueryWrapper<Coupon> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name",name);
        }
        if(type != null){
            wrapper.eq("type",type);
        }
        IPage<Coupon> iPage = couponMapper.selectPage(new Page<Coupon>(pageNum.longValue(), pageSize.longValue()), wrapper);
        return PageInfoVo.getVo(iPage, pageSize.longValue());
    }

    @Override
    public int updateCouponInfos(Long id, SmsCouponParam couponParam) {
        Coupon coupon = new Coupon();
        BeanUtils.copyProperties(couponParam,coupon);
        coupon.setId(id);
        return couponMapper.updateById(coupon);
    }

    @Override
    public int create(SmsCouponParam couponParam) {
        List<CouponProductCategoryRelation> categoryRelationList = couponParam.getProductCategoryRelationList();
        List<CouponProductRelation> productRelationList = couponParam.getProductRelationList();

        Coupon coupon = new Coupon();
        BeanUtils.copyProperties(couponParam,coupon);
        couponMapper.insert(coupon);
        for (CouponProductCategoryRelation categoryRelation : categoryRelationList) {
            categoryRelation.setCouponId(coupon.getId());
            productCategoryRelationMapper.insert(categoryRelation);
        }

        for (CouponProductRelation productRelation : productRelationList) {
            productRelation.setCouponId(coupon.getId());
            productRelationMapper.insert(productRelation);
        }
        return 1;
    }

    @Override
    public SmsCouponParam getCouponItemInfo(Long id) {
        SmsCouponParam smsCouponParam = new SmsCouponParam();
        Coupon coupon = couponMapper.selectById(id);
        BeanUtils.copyProperties(coupon,smsCouponParam);
        QueryWrapper<CouponProductCategoryRelation> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("coupon_id",id);
        List<CouponProductCategoryRelation> categoryRelationList = productCategoryRelationMapper.selectList(wrapper1);
        if (categoryRelationList != null && categoryRelationList.size() != 0) {

            smsCouponParam.setProductCategoryRelationList(categoryRelationList);
            return smsCouponParam;
        }
        QueryWrapper<CouponProductRelation> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("coupon_id",id);
        List<CouponProductRelation> relationList = productRelationMapper.selectList(wrapper2);
        if (relationList != null && relationList.size() != 0) {
            smsCouponParam.setProductRelationList(relationList);
            return smsCouponParam;
        }
        return smsCouponParam;
    }

    @Override
    public List<Coupon> getCouponListByMemberId(Long id) {
        List<Coupon> couponList = couponMapper.getCouponListByMemberId(id);
        return couponList;
    }
}
