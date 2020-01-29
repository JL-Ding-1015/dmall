package com.djl.dmall.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djl.dmall.sms.entity.HomeRecommendSubject;
import com.djl.dmall.sms.mapper.HomeRecommendSubjectMapper;
import com.djl.dmall.sms.service.HomeRecommendSubjectService;
import com.djl.dmall.vo.PageInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 首页推荐专题表 服务实现类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
@Service
@Component
public class HomeRecommendSubjectServiceImpl extends ServiceImpl<HomeRecommendSubjectMapper, HomeRecommendSubject> implements HomeRecommendSubjectService {

    @Autowired
    HomeRecommendSubjectMapper homeRecommendSubjectMapper;


    @Override
    public void updateSort(Long id, Integer sort) {
        HomeRecommendSubject newProduct = new HomeRecommendSubject();
        newProduct.setId(id);
        newProduct.setSort(sort);
        homeRecommendSubjectMapper.updateById(newProduct);
    }

    @Override
    public void updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        HomeRecommendSubject newProduct = new HomeRecommendSubject();
        for (Long id : ids) {
            newProduct.setId(id);
            newProduct.setRecommendStatus(recommendStatus);
            homeRecommendSubjectMapper.updateById(newProduct);
        }
    }

    @Override
    public PageInfoVo listForPage(String subjectName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        QueryWrapper<HomeRecommendSubject> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(subjectName)) {
            wrapper.like("subject_name", subjectName);
        }
        if(recommendStatus != null){
            wrapper.eq("recommend_status", recommendStatus);
        }
        IPage<HomeRecommendSubject> iPage = homeRecommendSubjectMapper.selectPage(
                new Page<HomeRecommendSubject>(pageNum.longValue(), pageSize.longValue()), wrapper);
        return PageInfoVo.getVo(iPage, pageNum.longValue());
    }
}
