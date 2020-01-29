package com.djl.dmall.sms.service;

import com.djl.dmall.sms.entity.HomeBrand;
import com.baomidou.mybatisplus.extension.service.IService;
import com.djl.dmall.vo.PageInfoVo;

import java.util.List;

/**
 * <p>
 * 首页推荐品牌表 服务类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
public interface HomeBrandService extends IService<HomeBrand> {

    PageInfoVo listBrandForPage(String brandName, Integer recommendStatus, Integer pageSize, Integer pageNum);

    int updateSort(Long id, Integer sort);

    void updateRecommendStatus(List<Long> ids, Integer recommendStatus);

}
