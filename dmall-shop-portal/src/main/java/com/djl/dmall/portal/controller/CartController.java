package com.djl.dmall.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.djl.dmall.cart.service.CartService;
import com.djl.dmall.cart.vo.CartItem;
import com.djl.dmall.cart.vo.CartResponse;
import com.djl.dmall.to.CommonResult;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

/**
 * 购物车
 */
@RequestMapping("/cart")
@RestController
public class CartController {

    @Reference
    CartService cartService;

    /**
     * 添加购物车，返回添加的商品信息
     *
     * @param skuId
     * @param cartKey
     * @param accessToken
     * @return
     */
    @PostMapping("/add")
    public CommonResult addToCart(@RequestParam(value = "skuId") Long skuId,
                                  @RequestParam(value = "num", defaultValue = "1") Integer num,
                                  @RequestParam(value = "cartKey", required = false) String cartKey,
                                  @RequestParam(value = "accessToken", required = false) String accessToken) throws ExecutionException, InterruptedException {
        CartResponse cartResponse = cartService.addToCart(skuId, num, cartKey, accessToken);
        return new CommonResult().success(cartResponse);
    }

    /**
     * 修改购物项的数量
     *
     * @param skuId
     * @param num
     * @param cartKey
     * @param accessToken
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @PostMapping("/update")
    public CommonResult updateCartItemNum(@RequestParam(value = "skuId") Long skuId,
                                          @RequestParam(value = "num", defaultValue = "1") Integer num,
                                          @RequestParam(value = "cartKey", required = false) String cartKey,
                                          @RequestParam(value = "accessToken", required = false) String accessToken) {
        CartResponse cartResponse = cartService.updateCartItemNum(skuId, num, cartKey, accessToken);
        return new CommonResult().success(cartResponse);
    }


    /**
     * 查看购物车所有内容
     *
     * @param cartKey
     * @param accessToken
     * @return
     */
    @GetMapping("/list")
    public CommonResult listCart(@RequestParam(value = "cartKey", required = false) String cartKey,
                                 @RequestParam(value = "accessToken", required = false) String accessToken) {
        if (StringUtils.isEmpty(cartKey) && StringUtils.isEmpty(accessToken)) {
            //如果同时没数据侧报错
            System.out.println("error......");
            CommonResult result = new CommonResult().failed();
            result.setMessage("无可用数据");
            return result;
        }

        CartResponse cartResponse = cartService.listCart(cartKey, accessToken);

        return new CommonResult().success(cartResponse);
    }

    /**
     * 删除单个条目
     *
     * @param skuId
     * @param cartKey
     * @param accessToken
     * @return
     */
    @GetMapping("/del")
    public CommonResult delCartItem(@RequestParam(value = "skuId") Long skuId,
                                    @RequestParam(value = "cartKey", required = false) String cartKey,
                                    @RequestParam(value = "accessToken", required = false) String accessToken) {
        CartResponse cartResponse = cartService.delCartItem(skuId, cartKey, accessToken);
        return new CommonResult().success(cartResponse);
    }

    /**
     * 清除购物车
     *
     * @param cartKey
     * @param accessToken
     * @return
     */
    @GetMapping("/clear")
    public CommonResult clearCart(@RequestParam(value = "cartKey", required = false) String cartKey,
                                  @RequestParam(value = "accessToken", required = false) String accessToken) {
        CartResponse cartResponse = cartService.clearCart(cartKey, accessToken);
        return new CommonResult().success(cartResponse);
    }

    /**
     * 批量选中、反选
     * @param skuIds
     * @param ops
     * @param cartKey
     * @param accessToken
     * @return
     */
    @PostMapping("/check")
    public CommonResult cartCheck(@RequestParam(value = "skuIds") Long[] skuIds,
                                  @RequestParam(value = "ops", defaultValue = "1") Integer ops,
                                  @RequestParam(value = "cartKey", required = false) String cartKey,
                                  @RequestParam(value = "accessToken", required = false) String accessToken) {
        CartResponse cartResponse = cartService.checkCartItems(skuIds, ops, cartKey, accessToken);
        return new CommonResult().success(cartResponse);
    }

}
