package com.djl.dmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djl.dmall.pms.entity.ProductAttribute;
import com.djl.dmall.pms.mapper.ProductAttributeMapper;
import com.djl.dmall.pms.service.ProductAttributeService;
import com.djl.dmall.vo.PageInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 商品属性参数表 服务实现类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
@Service
@Component
public class ProductAttributeServiceImpl extends ServiceImpl<ProductAttributeMapper, ProductAttribute> implements ProductAttributeService {

    @Autowired
    ProductAttributeMapper productAttributeMapper;


    /**
     * 查出该属性分类下所有的销售属性和基本参数
     *
     * @param cid
     * @param type
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public PageInfoVo getCategoryAttributes(Long cid, Integer type, Integer pageSize, Integer pageNum) {

        QueryWrapper<ProductAttribute> wrapper = new QueryWrapper<ProductAttribute>()
                .eq("product_attribute_category_id", cid).eq("type", type);

        IPage<ProductAttribute> Page = productAttributeMapper.selectPage(new Page<ProductAttribute>(pageNum,pageSize), wrapper);

        PageInfoVo pageInfoVo = PageInfoVo.getVo(Page, pageSize.longValue());
        return pageInfoVo;
    }
}
