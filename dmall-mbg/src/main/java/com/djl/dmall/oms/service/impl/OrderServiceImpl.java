package com.djl.dmall.oms.service.impl;

import com.djl.dmall.oms.entity.Order;
import com.djl.dmall.oms.mapper.OrderMapper;
import com.djl.dmall.oms.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

}
