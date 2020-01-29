package com.djl.dmall.ums.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djl.dmall.ums.entity.MemberLevel;
import com.djl.dmall.ums.mapper.MemberLevelMapper;
import com.djl.dmall.ums.service.MemberLevelService;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 会员等级表 服务实现类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
@Service
@Component
public class MemberLevelServiceImpl extends ServiceImpl<MemberLevelMapper, MemberLevel> implements MemberLevelService {

}
