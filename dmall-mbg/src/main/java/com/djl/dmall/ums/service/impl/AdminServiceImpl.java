package com.djl.dmall.ums.service.impl;

import com.djl.dmall.ums.entity.Admin;
import com.djl.dmall.ums.mapper.AdminMapper;
import com.djl.dmall.ums.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

}
