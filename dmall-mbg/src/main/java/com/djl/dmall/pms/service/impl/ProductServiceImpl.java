package com.djl.dmall.pms.service.impl;

import com.djl.dmall.pms.entity.Product;
import com.djl.dmall.pms.mapper.ProductMapper;
import com.djl.dmall.pms.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-22
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

}
