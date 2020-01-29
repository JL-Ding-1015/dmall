package com.djl.dmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djl.dmall.contant.SysCacheConstant;
import com.djl.dmall.pms.entity.ProductCategory;
import com.djl.dmall.pms.mapper.ProductCategoryMapper;
import com.djl.dmall.pms.service.ProductCategoryService;
import com.djl.dmall.vo.product.PmsProductCategoryWithChildrenItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 产品分类 服务实现类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
@Slf4j
@Service
@Component
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {

    @Autowired
    ProductCategoryMapper categoryMapper;

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    /**
     * 分布式系统，缓存用redis
     *
     * @param i
     * @return
     */
    @Override
    public List<PmsProductCategoryWithChildrenItem> listCategoryWithChild(Integer i) {
        Object cacheObj = redisTemplate.opsForValue().get(SysCacheConstant.CATEGORY_MENU_CACHE_KEY);
        List<PmsProductCategoryWithChildrenItem> items;
        if (cacheObj != null) {
            log.debug("商品分类数据命中缓存......");
            System.out.println("走的缓存");
            items = (List<PmsProductCategoryWithChildrenItem>) cacheObj;
        }else{
            items = categoryMapper.listCategoryWithChildren(i);
            redisTemplate.opsForValue().set(SysCacheConstant.CATEGORY_MENU_CACHE_KEY, items);
        }
        return items;
    }
}
