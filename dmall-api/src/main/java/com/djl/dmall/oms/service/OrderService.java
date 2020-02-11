package com.djl.dmall.oms.service;

import com.djl.dmall.oms.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.djl.dmall.vo.order.OrderConfirmVo;
import com.djl.dmall.vo.order.OrderCreateVo;

import java.math.BigDecimal;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
public interface OrderService extends IService<Order> {
    /**
     * 订单确认
     * @param id
     * @return
     */
    OrderConfirmVo orderConfirm(Long id);

    /**
     * 创建订单
     * @return
     * @param totalPrice
     * @param addressId
     * @param note
     */
    OrderCreateVo createOrder(BigDecimal totalPrice, Long addressId, String note);

}
