package com.djl.dmall.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djl.dmall.sms.entity.HomeAdvertise;
import com.djl.dmall.sms.mapper.HomeAdvertiseMapper;
import com.djl.dmall.sms.service.HomeAdvertiseService;
import com.djl.dmall.vo.PageInfoVo;
import org.codehaus.groovy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 首页轮播广告表 服务实现类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
@Service
@Component
public class HomeAdvertiseServiceImpl extends ServiceImpl<HomeAdvertiseMapper, HomeAdvertise> implements HomeAdvertiseService {

    @Autowired
    HomeAdvertiseMapper homeAdvertiseMapper;

    @Override
    public PageInfoVo listAdvertiseForPage(String name, Integer type, String endTime, Integer pageSize, Integer pageNum) {
        QueryWrapper<HomeAdvertise> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name", name);
        }
        if(type != null){
            wrapper.eq("type", type);
        }
        if(!StringUtils.isEmpty(endTime)){
            wrapper.like("end_time", endTime);
        }
        IPage<HomeAdvertise> iPage = homeAdvertiseMapper.selectPage(
                new Page<HomeAdvertise>(pageNum.longValue(), pageSize.longValue()), wrapper);

        return PageInfoVo.getVo(iPage, pageSize.longValue());
    }

    @Override
    public int updateStatus(Long id, Integer status) {
        QueryWrapper<HomeAdvertise> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        HomeAdvertise homeAdvertise = new HomeAdvertise();
        homeAdvertise.setId(id);
        homeAdvertise.setStatus(status);
        homeAdvertiseMapper.updateById(homeAdvertise);
        return 1;
    }

}
