package com.djl.dmall.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djl.dmall.sms.entity.HomeNewProduct;
import com.djl.dmall.sms.entity.HomeRecommendProduct;
import com.djl.dmall.sms.mapper.HomeRecommendProductMapper;
import com.djl.dmall.sms.service.HomeRecommendProductService;
import com.djl.dmall.vo.PageInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 人气推荐商品表 服务实现类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
@Service
@Component
public class HomeRecommendProductServiceImpl extends ServiceImpl<HomeRecommendProductMapper, HomeRecommendProduct> implements HomeRecommendProductService {

    @Autowired
    HomeRecommendProductMapper homeRecommendProductMapper;

    @Override
    public void updateSort(Long id, Integer sort) {
        HomeRecommendProduct newProduct = new HomeRecommendProduct();
        newProduct.setId(id);
        newProduct.setSort(sort);
        homeRecommendProductMapper.updateById(newProduct);
    }

    @Override
    public void updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        HomeRecommendProduct newProduct = new HomeRecommendProduct();
        for (Long id : ids) {
            newProduct.setId(id);
            newProduct.setRecommendStatus(recommendStatus);
            homeRecommendProductMapper.updateById(newProduct);
        }
    }

    @Override
    public PageInfoVo listrecommendProductForPage(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        QueryWrapper<HomeRecommendProduct> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(productName)) {
            wrapper.like("product_name", productName);
        }
        if(recommendStatus != null){
            wrapper.eq("recommend_status", recommendStatus);
        }
        IPage<HomeRecommendProduct> iPage = homeRecommendProductMapper.selectPage(
                new Page<HomeRecommendProduct>(pageNum.longValue(), pageSize.longValue()), wrapper);
        return PageInfoVo.getVo(iPage, pageNum.longValue());
    }
}
