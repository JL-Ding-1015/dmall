package com.djl.dmall.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djl.dmall.sms.entity.FlashPromotion;
import com.djl.dmall.sms.mapper.FlashPromotionMapper;
import com.djl.dmall.sms.service.FlashPromotionService;
import com.djl.dmall.vo.PageInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 限时购表 服务实现类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
@Service
@Component
public class FlashPromotionServiceImpl extends ServiceImpl<FlashPromotionMapper, FlashPromotion> implements FlashPromotionService {

    @Autowired
    FlashPromotionMapper flashPromotionMapper;

    @Override
    public PageInfoVo listFlashPromotionForPage(String keyword, Integer pageSize, Integer pageNum) {

        QueryWrapper<FlashPromotion> wrapper = new QueryWrapper<FlashPromotion>();
        if(!StringUtils.isEmpty(keyword)){
            wrapper.like("title", keyword);
        }
        IPage<FlashPromotion> iPage = flashPromotionMapper.selectPage(new Page<FlashPromotion>(pageNum.longValue(),
                pageSize.longValue()), wrapper);
        PageInfoVo pageInfoVo = PageInfoVo.getVo(iPage, pageSize.longValue());
        return pageInfoVo;
    }

    @Override
    public int updateStatus(Long id, Integer status) {
        FlashPromotion flashPromotion = new FlashPromotion();
        flashPromotion.setId(id);
        flashPromotion.setStatus(status);
        return flashPromotionMapper.updateById(flashPromotion);
    }

    @Override
    public int createFlashPromotion(FlashPromotion flashPromotion) {
        int insert = flashPromotionMapper.insert(flashPromotion);
        return insert;
    }
}
