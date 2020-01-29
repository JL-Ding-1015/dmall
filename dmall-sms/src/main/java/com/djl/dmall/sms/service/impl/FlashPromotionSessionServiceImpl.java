package com.djl.dmall.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djl.dmall.sms.entity.FlashPromotionProductRelation;
import com.djl.dmall.sms.entity.FlashPromotionSession;
import com.djl.dmall.sms.mapper.FlashPromotionProductRelationMapper;
import com.djl.dmall.sms.mapper.FlashPromotionSessionMapper;
import com.djl.dmall.sms.service.FlashPromotionSessionService;
import com.djl.dmall.vo.PageInfoVo;
import com.djl.dmall.vo.sms.SmsFlashPromotionProduct;
import com.djl.dmall.vo.sms.SmsFlashPromotionSessionDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 限时购场次表 服务实现类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
@Slf4j
@Service
@Component
public class FlashPromotionSessionServiceImpl extends ServiceImpl<FlashPromotionSessionMapper, FlashPromotionSession> implements FlashPromotionSessionService {

    @Autowired
    FlashPromotionSessionMapper flashPromotionSessionMapper;

    @Autowired
    FlashPromotionProductRelationMapper flashPromotionProductRelationMapper;

    @Override
    public PageInfoVo selectListForPage(Long flashPromotionId) {
        QueryWrapper<FlashPromotionSession> wrapper = new QueryWrapper<>();
        wrapper.eq("id", flashPromotionId);
        IPage<FlashPromotionSession> iPage = flashPromotionSessionMapper.selectPage(new Page<FlashPromotionSession>(1, 100), wrapper);
        log.debug("读取id是：{}，返回的数据是：{}", flashPromotionId, iPage);

        List<FlashPromotionSession> records = iPage.getRecords();
        List<SmsFlashPromotionSessionDetail> smsDetailList = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            SmsFlashPromotionSessionDetail smsFlashPromotionSessionDetail = new SmsFlashPromotionSessionDetail();
            BeanUtils.copyProperties(records.get(i), smsFlashPromotionSessionDetail);
            smsFlashPromotionSessionDetail.setProductCount(
                    flashPromotionProductRelationMapper.selectCount(
                            new QueryWrapper<FlashPromotionProductRelation>()
                                    .eq("flash_promotion_id", flashPromotionId)));
            smsDetailList.add(smsFlashPromotionSessionDetail);
        }
        IPage<SmsFlashPromotionSessionDetail> iPage1 = new Page<>();
        BeanUtils.copyProperties(iPage, iPage1, "records");
        iPage1.setRecords(smsDetailList);
        return PageInfoVo.getVo(iPage1, 100L);
    }

    @Override
    public void updateStatus(Long id, Integer status) {

    }
}
