package com.djl.dmall.sms.service;

import com.djl.dmall.sms.entity.HomeRecommendProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import com.djl.dmall.vo.PageInfoVo;

import java.util.List;

/**
 * <p>
 * 人气推荐商品表 服务类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
public interface HomeRecommendProductService extends IService<HomeRecommendProduct> {
    /**
     * 修改推荐排序
     * @param id
     * @param sort
     */
    void updateSort(Long id, Integer sort);

    /**
     * 批量修改是否推荐的状态
     * @param ids
     * @param recommendStatus
     */
    void updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 分页查询首页推荐的信息
     * @param productName
     * @param recommendStatus
     * @param pageSize
     * @param pageNum
     * @return
     */
    PageInfoVo listrecommendProductForPage(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum);
}
