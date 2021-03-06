package com.djl.dmall.sms.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djl.dmall.sms.entity.HomeBrand;
import com.djl.dmall.sms.mapper.HomeBrandMapper;
import com.djl.dmall.sms.service.HomeBrandService;
import com.djl.dmall.vo.PageInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 首页推荐品牌表 服务实现类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
@Service
@Component
public class HomeBrandServiceImpl extends ServiceImpl<HomeBrandMapper, HomeBrand> implements HomeBrandService {

    @Autowired
    HomeBrandMapper homeBrandMapper;

    @Override
    public PageInfoVo listBrandForPage(String brandName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        QueryWrapper<HomeBrand> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(brandName)){
            wrapper.like("brand_name",brandName);
        }

        if(recommendStatus!=null){
            wrapper.eq("recommend_status",recommendStatus);
        }

        IPage<HomeBrand> iPage = homeBrandMapper
                .selectPage(new Page<HomeBrand>(pageNum, pageSize), wrapper);
        return PageInfoVo.getVo(iPage,pageSize.longValue());
    }

    @Override
    public int updateSort(Long id, Integer sort) {
        HomeBrand homeBrand = new HomeBrand();
        homeBrand.setId(id);
        homeBrand.setSort(sort);
        return homeBrandMapper.updateById(homeBrand);
    }

    @Override
    public void updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        for (Long id : ids) {
            HomeBrand homeBrand = new HomeBrand();
            homeBrand.setId(id);
            homeBrand.setRecommendStatus(recommendStatus);
            homeBrandMapper.updateById(homeBrand);
        }
    }
}
