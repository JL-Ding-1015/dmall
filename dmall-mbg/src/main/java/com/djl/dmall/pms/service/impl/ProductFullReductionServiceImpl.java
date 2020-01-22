package com.djl.dmall.pms.service.impl;

import com.djl.dmall.pms.entity.ProductFullReduction;
import com.djl.dmall.pms.mapper.ProductFullReductionMapper;
import com.djl.dmall.pms.service.ProductFullReductionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品满减表(只针对同商品) 服务实现类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-22
 */
@Service
public class ProductFullReductionServiceImpl extends ServiceImpl<ProductFullReductionMapper, ProductFullReduction> implements ProductFullReductionService {

}
