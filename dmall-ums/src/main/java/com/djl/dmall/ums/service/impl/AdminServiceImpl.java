package com.djl.dmall.ums.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djl.dmall.ums.entity.Admin;
import com.djl.dmall.ums.mapper.AdminMapper;
import com.djl.dmall.ums.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
@Service
@Component
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    AdminMapper adminMapper;

    @Override
    public Admin login(String username, String password) {

        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());

        QueryWrapper<Admin> wrapper = new QueryWrapper<Admin>().eq("username", username)
                                        .eq("password", md5Password);

        Admin admin = adminMapper.selectOne(wrapper);

        return admin;
    }

    @Override
    public Admin getUserInfo(String username) {


        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username",username));
    }
}
