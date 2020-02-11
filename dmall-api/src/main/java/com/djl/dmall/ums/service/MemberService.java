package com.djl.dmall.ums.service;

import com.djl.dmall.ums.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;
import com.djl.dmall.ums.entity.MemberReceiveAddress;

import java.util.List;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
public interface MemberService extends IService<Member> {

    Member login(String username, String password);

    /**
     * 根据用户id查询其收货地址
     * @param id
     * @return
     */
    List<MemberReceiveAddress> getMemberAddressList(Long id);

    /**
     * 根据订单确认页的地址id查询地址详细信息
     * @param addressId
     * @return
     */
    MemberReceiveAddress getOrderAddressById(Long addressId);
}
