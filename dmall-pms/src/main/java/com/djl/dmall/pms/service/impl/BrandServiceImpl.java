package com.djl.dmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djl.dmall.pms.entity.Brand;
import com.djl.dmall.pms.mapper.BrandMapper;
import com.djl.dmall.pms.service.BrandService;
import com.djl.dmall.vo.PageInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 品牌表 服务实现类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
@Service
@Component
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {

    @Autowired
    BrandMapper brandMapper;

    @Override
    public PageInfoVo brandPageInfo(String keyword, Integer pageNum, Integer pageSize) {

        QueryWrapper<Brand> name = null;

        if (!StringUtils.isEmpty(keyword)) {
            name = new QueryWrapper<Brand>().like("name",keyword);
        }


        IPage<Brand> brands = brandMapper.selectPage(new Page<Brand>(pageNum.longValue(), pageSize), name);

        PageInfoVo resPageInfoVo = new PageInfoVo(brands.getTotal(),brands.getPages(),pageSize.longValue(),brands.getRecords(),pageNum.longValue());

        return resPageInfoVo;
    }
}
