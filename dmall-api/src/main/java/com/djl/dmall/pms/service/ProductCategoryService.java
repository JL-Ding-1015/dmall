package com.djl.dmall.pms.service;

import com.djl.dmall.pms.entity.ProductCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.djl.dmall.vo.product.PmsProductCategoryWithChildrenItem;

import java.util.List;

/**
 * <p>
 * 产品分类 服务类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
public interface ProductCategoryService extends IService<ProductCategory> {

    /**
     * 查询这个菜单以及他的子菜单
     * @param i
     * @return
     */
    List<PmsProductCategoryWithChildrenItem> listCategoryWithChild(Integer i);
}
