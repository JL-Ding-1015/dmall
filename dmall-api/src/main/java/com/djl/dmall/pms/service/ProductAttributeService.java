package com.djl.dmall.pms.service;

import com.djl.dmall.pms.entity.ProductAttribute;
import com.baomidou.mybatisplus.extension.service.IService;
import com.djl.dmall.vo.PageInfoVo;

/**
 * <p>
 * 商品属性参数表 服务类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
public interface ProductAttributeService extends IService<ProductAttribute> {

    PageInfoVo getCategoryAttributes(Long cid, Integer type, Integer pageSize, Integer pageNum);
}
