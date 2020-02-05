package com.djl.dmall.pms.mapper;

import com.djl.dmall.pms.entity.ProductAttributeValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.djl.dmall.to.es.EsProductAttributeValue;

import java.util.List;

/**
 * <p>
 * 存储产品参数信息的表 Mapper 接口
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
public interface ProductAttributeValueMapper extends BaseMapper<ProductAttributeValue> {
    /**
     * 查询基础商品信息
     * @param id
     * @return
     */
    List<EsProductAttributeValue> selectBaseAttributeValues(Long id);

    /**
     * 查询每个商品的sku属性的name，有排序
     * @param id
     * @return
     */
    List<EsProductAttributeValue> selectSkuAttributeNames(Long id);
}
