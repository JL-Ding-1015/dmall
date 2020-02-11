package com.djl.dmall.ums.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djl.dmall.ums.entity.Member;
import com.djl.dmall.ums.entity.MemberReceiveAddress;
import com.djl.dmall.ums.mapper.MemberMapper;
import com.djl.dmall.ums.mapper.MemberReceiveAddressMapper;
import com.djl.dmall.ums.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.List;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
@Service
@Component
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Autowired
    MemberMapper memberMapper;
    @Autowired
    MemberReceiveAddressMapper memberReceiveAddressMapper;

    @Override
    public Member login(String username, String password) {
        String digest = DigestUtils.md5DigestAsHex(password.getBytes());

        Member member = memberMapper.selectOne(new QueryWrapper<Member>()
                .eq("username", username)
                .eq("password", digest));
        return member;
    }

    @Override
    public List<MemberReceiveAddress> getMemberAddressList(Long id) {
        return memberReceiveAddressMapper.selectList(new QueryWrapper<MemberReceiveAddress>().eq("member_id", id));
    }

    @Override
    public MemberReceiveAddress getOrderAddressById(Long addressId) {
        MemberReceiveAddress address = memberReceiveAddressMapper.selectById(addressId);
        return address;
    }

}
