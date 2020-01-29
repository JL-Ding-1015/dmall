package com.djl.dmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djl.dmall.pms.entity.ProductAttributeCategory;
import com.djl.dmall.pms.mapper.ProductAttributeCategoryMapper;
import com.djl.dmall.pms.service.ProductAttributeCategoryService;
import com.djl.dmall.vo.PageInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 产品属性分类表 服务实现类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
@Service
@Component
public class ProductAttributeCategoryServiceImpl extends ServiceImpl<ProductAttributeCategoryMapper, ProductAttributeCategory> implements ProductAttributeCategoryService {

    @Autowired
    ProductAttributeCategoryMapper productAttributeCategoryMapper;

    @Override
    public PageInfoVo productAttributeCategoryPageInfo(Integer pageNum, Integer pageSize) {

        IPage<ProductAttributeCategory> page = productAttributeCategoryMapper.selectPage(
                new Page<ProductAttributeCategory>(pageNum, pageSize), null);

        PageInfoVo pageInfoVo = PageInfoVo.getVo(page, pageSize.longValue());
        //返回分页属性分类
        return pageInfoVo;
    }
}
