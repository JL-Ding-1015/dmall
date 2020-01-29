package com.djl.dmall.sms.service;

import com.djl.dmall.sms.entity.HomeNewProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import com.djl.dmall.vo.PageInfoVo;

import java.util.List;

/**
 * <p>
 * 新鲜好物表 服务类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
public interface HomeNewProductService extends IService<HomeNewProduct> {
    /**
     * 分页查询推荐信息
     * @param productName
     * @param recommendStatus
     * @param pageSize
     * @param pageNum
     * @return
     */
    PageInfoVo listNewProductForPage(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum);

    /**
     * 修改推荐信息的状态
     * @param ids
     * @param recommendStatus
     */
    void updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 新的排序状态
     * @param id
     * @param sort
     */
    void updateSort(Long id, Integer sort);
}
