package com.djl.dmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djl.dmall.pms.entity.SkuStock;
import com.djl.dmall.pms.mapper.SkuStockMapper;
import com.djl.dmall.pms.service.SkuStockService;
import org.springframework.stereotype.Component;

/**
 * <p>
 * sku的库存 服务实现类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
@Service
@Component
public class SkuStockServiceImpl extends ServiceImpl<SkuStockMapper, SkuStock> implements SkuStockService {

}
