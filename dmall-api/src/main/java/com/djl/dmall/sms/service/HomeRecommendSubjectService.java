package com.djl.dmall.sms.service;

import com.djl.dmall.sms.entity.HomeRecommendSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.djl.dmall.vo.PageInfoVo;

import java.util.List;

/**
 * <p>
 * 首页推荐专题表 服务类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
public interface HomeRecommendSubjectService extends IService<HomeRecommendSubject> {

    void updateSort(Long id, Integer sort);

    void updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    PageInfoVo listForPage(String subjectName, Integer recommendStatus, Integer pageSize, Integer pageNum);
}
