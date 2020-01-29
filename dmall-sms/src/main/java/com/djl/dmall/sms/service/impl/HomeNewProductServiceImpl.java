package com.djl.dmall.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djl.dmall.sms.entity.HomeNewProduct;
import com.djl.dmall.sms.mapper.HomeNewProductMapper;
import com.djl.dmall.sms.service.HomeNewProductService;
import com.djl.dmall.vo.PageInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 新鲜好物表 服务实现类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
@Service
@Component
public class HomeNewProductServiceImpl extends ServiceImpl<HomeNewProductMapper, HomeNewProduct> implements HomeNewProductService {

    @Autowired
    HomeNewProductMapper homeNewProductMapper;

    @Override
    public PageInfoVo listNewProductForPage(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        QueryWrapper<HomeNewProduct> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(productName)) {
            wrapper.like("product_name", productName);
        }
        if(recommendStatus != null){
            wrapper.eq("recommend_status", recommendStatus);
        }
        IPage<HomeNewProduct> iPage = homeNewProductMapper.selectPage(
                new Page<HomeNewProduct>(pageNum.longValue(), pageSize.longValue()), wrapper);
        return PageInfoVo.getVo(iPage, pageNum.longValue());
    }

    @Override
    public void updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        HomeNewProduct newProduct = new HomeNewProduct();
        for (Long id : ids) {
            newProduct.setId(id);
            newProduct.setRecommendStatus(recommendStatus);
            homeNewProductMapper.updateById(newProduct);
        }
    }

    @Override
    public void updateSort(Long id, Integer sort) {
        HomeNewProduct newProduct = new HomeNewProduct();
        newProduct.setId(id);
        newProduct.setSort(sort);
        homeNewProductMapper.updateById(newProduct);
    }
}
