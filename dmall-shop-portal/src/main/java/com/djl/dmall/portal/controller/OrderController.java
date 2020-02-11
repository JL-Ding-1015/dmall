package com.djl.dmall.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSON;
import com.djl.dmall.contant.CartConstant;
import com.djl.dmall.contant.SysCacheConstant;
import com.djl.dmall.oms.service.OrderService;
import com.djl.dmall.to.CommonResult;
import com.djl.dmall.ums.entity.Member;
import com.djl.dmall.vo.order.OrderConfirmVo;
import com.djl.dmall.vo.order.OrderCreateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Reference
    OrderService orderService;

    /**
     * 订单确认
     * @param accessToken
     * @return
     */
    @PostMapping("/confirm")
    public CommonResult confirmOrder(@RequestParam("accessToken") String accessToken) {
        Member member = null;
        String memberJson = null;
        if (StringUtils.isEmpty(accessToken)) {
            //用户未登录
            CommonResult commonResult = new CommonResult().failed();
            commonResult.setMessage("未登录，请先登录");
            return commonResult;
        } else {

            memberJson = stringRedisTemplate.opsForValue().get(SysCacheConstant.LOGIN_MEMBER + accessToken);
            if (StringUtils.isEmpty(memberJson)) {
                //用户信息错误
                CommonResult commonResult = new CommonResult().failed();
                commonResult.setMessage("用户信息错误，请重新登录");
                return commonResult;
            }
        }
        member = JSON.parseObject(memberJson, Member.class);

        //RPC隐式传参
        RpcContext.getContext().setAttachment("accessToken", accessToken);
        OrderConfirmVo orderConfirmVo = orderService.orderConfirm(member.getId());
        /**
         * 1.用户的可选地址列表
         * 2.当前购物车中的商品信息
         * 3.可用的优惠券信息
         * 4。支付配送发票方式信息
         *
         */
        return new CommonResult().success(orderConfirmVo);
    }

    /**
     * 订单正式创建
     * @param totalPrice
     * @param accessToken
     * @param AddressId
     * @param note
     * @param orderToken
     * @return
     */
    @PostMapping("/create")
    public CommonResult createOrder(@RequestParam("totalPrice") BigDecimal totalPrice,
                                    @RequestParam("accessToken") String accessToken,
                                    @RequestParam("addressId") Long AddressId,
                                    @RequestParam(value = "note", required = false) String note,
                                    @RequestParam("orderToken") String orderToken) {
        RpcContext.getContext().setAttachment("accessToken", accessToken);
        RpcContext.getContext().setAttachment("orderToken", orderToken);
        //主要生成订单和订单项
        OrderCreateVo orderCreateVo = orderService.createOrder(totalPrice, AddressId, note);
        if (!StringUtils.isEmpty(orderCreateVo.getToken())) {
            //令牌验证失败
            CommonResult commonResult = new CommonResult().failed();
            commonResult.setMessage(orderCreateVo.getToken());
            return commonResult;
        }
        if (!orderCreateVo.getLimit()) {
            CommonResult commonResult = new CommonResult().failed();
            commonResult.setMessage("前后价格不一致");
            return commonResult;
        }
        return new CommonResult().success(orderCreateVo);
    }

    /**
     * 去支付
     * @return
     */
    @GetMapping("/pay")
    public String pay(){
        return "";
    }
}
