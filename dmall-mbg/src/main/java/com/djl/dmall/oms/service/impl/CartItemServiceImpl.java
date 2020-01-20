package com.djl.dmall.oms.service.impl;

import com.djl.dmall.oms.entity.CartItem;
import com.djl.dmall.oms.mapper.CartItemMapper;
import com.djl.dmall.oms.service.CartItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 购物车表 服务实现类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
@Service
public class CartItemServiceImpl extends ServiceImpl<CartItemMapper, CartItem> implements CartItemService {

}
