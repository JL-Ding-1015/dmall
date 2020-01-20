package com.djl.dmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djl.dmall.pms.entity.Product;
import com.djl.dmall.pms.mapper.ProductMapper;
import com.djl.dmall.pms.service.ProductService;
import org.springframework.stereotype.Component;


/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
@Service
@Component
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

}
