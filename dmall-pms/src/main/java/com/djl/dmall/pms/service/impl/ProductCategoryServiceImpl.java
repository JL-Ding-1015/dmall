package com.djl.dmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djl.dmall.pms.entity.ProductCategory;
import com.djl.dmall.pms.mapper.ProductCategoryMapper;
import com.djl.dmall.pms.service.ProductCategoryService;
import com.djl.dmall.vo.product.PmsProductCategoryWithChildrenItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 产品分类 服务实现类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
@Service
@Component
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {

    @Autowired
    ProductCategoryMapper categoryMapper;

    @Override
    public List<PmsProductCategoryWithChildrenItem> listCategoryWithChild(Integer i) {
        return categoryMapper.listCategoryWithChildren(i);
    }
}
