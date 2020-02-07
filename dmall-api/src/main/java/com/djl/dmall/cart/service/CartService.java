package com.djl.dmall.cart.service;

import com.djl.dmall.cart.vo.CartItem;
import com.djl.dmall.cart.vo.CartResponse;

import java.util.concurrent.ExecutionException;

/**
 * 购物车的服务接口
 */
public interface CartService {
    /**
     * 添加购物车
     * @param skuId
     * @param cartKey
     * @param accessToken
     * @return
     */
    CartResponse addToCart(Long skuId, Integer num, String cartKey, String accessToken) throws ExecutionException, InterruptedException;

    /**
     * 更改购物项的数量
     * @param skuId
     * @param num
     * @param cartKey
     * @param accessToken
     * @return
     */
    CartResponse updateCartItemNum(Long skuId, Integer num, String cartKey, String accessToken);

    /**
     * 获取所有购物车的数据
     * @param cartKey
     * @param accessToken
     * @return
     */
    CartResponse listCart(String cartKey, String accessToken);

    /**
     * 删除单个条目
     * @param skuId
     * @param cartKey
     * @param accessToken
     * @return
     */
    CartResponse delCartItem(Long skuId, String cartKey, String accessToken);

    /**
     * 删除整个购物车
     * @param cartKey
     * @param accessToken
     * @return
     */
    CartResponse clearCart(String cartKey, String accessToken);

    /**
     * 批量选中、反选
     * @param skuIds
     * @param ops
     * @param cartKey
     * @param accessToken
     * @return
     */
    CartResponse checkCartItems(Long[] skuIds, Integer ops, String cartKey, String accessToken);
}
