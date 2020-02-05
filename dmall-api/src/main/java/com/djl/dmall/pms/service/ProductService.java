package com.djl.dmall.pms.service;

import com.djl.dmall.pms.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import com.djl.dmall.to.es.EsProduct;
import com.djl.dmall.to.es.EsProductAttributeValue;
import com.djl.dmall.vo.PageInfoVo;
import com.djl.dmall.vo.product.PmsProductParam;
import com.djl.dmall.vo.product.PmsProductQueryParam;

import java.util.List;

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

    /**
     * 商品批量修改推荐状态
     * @param ids
     * @param recommendStatus
     */
    void updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 商品批量上架下架
     * @param ids
     * @param publishStatus
     */
    void updatePublishStatus(List<Long> ids, Integer publishStatus);

    /**
     * 获取product信息
     * @param id
     * @return
     */
    Product productInfoById(Long id);

    /**
     * 获取商品单个的详情
     * @param id
     * @return
     */
    EsProduct productAllInfo(Long id);

    /**
     * 根据商品的sku查询
     * @param id
     * @return
     */
    EsProduct productSkuInfo(Long id);
}
