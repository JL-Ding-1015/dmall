package com.djl.dmall.sms.service;

import com.djl.dmall.sms.entity.HomeAdvertise;
import com.baomidou.mybatisplus.extension.service.IService;
import com.djl.dmall.vo.PageInfoVo;

/**
 * <p>
 * 首页轮播广告表 服务类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
public interface HomeAdvertiseService extends IService<HomeAdvertise> {
    /**
     * 分页查询广告
     * @param name
     * @param type
     * @param endTime
     * @param pageSize
     * @param pageNum
     * @return
     */
    PageInfoVo listAdvertiseForPage(String name, Integer type, String endTime, Integer pageSize, Integer pageNum);

    /**
     * 修改广告的状态
     * @param id
     * @param status
     * @return
     */
    int updateStatus(Long id, Integer status);
}
