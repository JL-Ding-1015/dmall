package com.djl.dmall.pms.service;

import com.djl.dmall.pms.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import com.djl.dmall.vo.PageInfoVo;
import com.djl.dmall.vo.product.PmsProductParam;
import com.djl.dmall.vo.product.PmsProductQueryParam;

/**
 * <p>
 * 商品信息 服务类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
public interface ProductService extends IService<Product> {

    /**
     * 根据复杂查询条件返回分页数据
     * @param productQueryParam
     * @return
     */
    PageInfoVo productPageInfo(PmsProductQueryParam productQueryParam);

    /**
     * 保存商品数据
     * @param productParam
     */
    void saveProduct(PmsProductParam productParam);
}
