package com.djl.dmall.oms.service.Impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djl.dmall.cart.service.CartService;

import com.djl.dmall.cart.vo.CartItem;
import com.djl.dmall.contant.OrderStatusEnume;
import com.djl.dmall.contant.SysCacheConstant;
import com.djl.dmall.oms.component.MemberComponent;
import com.djl.dmall.oms.entity.Order;
import com.djl.dmall.oms.entity.OrderItem;
import com.djl.dmall.oms.mapper.OrderItemMapper;
import com.djl.dmall.oms.mapper.OrderMapper;
import com.djl.dmall.oms.service.OrderService;
import com.djl.dmall.pms.entity.SkuStock;
import com.djl.dmall.pms.service.ProductService;
import com.djl.dmall.pms.service.SkuStockService;
import com.djl.dmall.sms.entity.Coupon;
import com.djl.dmall.sms.service.CouponService;
import com.djl.dmall.to.es.EsProduct;
import com.djl.dmall.to.es.EsProductAttributeValue;
import com.djl.dmall.to.es.EsSkuProductInfo;
import com.djl.dmall.ums.entity.Member;
import com.djl.dmall.ums.entity.MemberReceiveAddress;
import com.djl.dmall.ums.service.MemberService;
import com.djl.dmall.vo.order.OrderConfirmVo;
import com.djl.dmall.vo.order.OrderCreateVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
@Service
@Component
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Reference
    MemberService memberService;
    @Reference
    CartService cartService;
    @Reference
    CouponService couponService;
    @Reference
    ProductService productService;
    @Reference
    SkuStockService skuStockService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    MemberComponent memberComponent;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderItemMapper orderItemMapper;

    ThreadLocal<List<CartItem>> threadLocalCartItemList = new ThreadLocal<>();
    ThreadLocal<Map<Long, SkuStock>> threadLocalSkuStockMap = new ThreadLocal<>();

    /**
     * 订单确认步骤
     * @param id
     * @return
     */
    @Override
    public OrderConfirmVo orderConfirm(Long id) {
        String accessToken = RpcContext.getContext().getAttachment("accessToken");
        OrderConfirmVo orderConfirmVo = new OrderConfirmVo();
        orderConfirmVo.setAddressList(memberService.getMemberAddressList(id));

        List<CartItem> cartItemList = cartService.getCartItemListForOrder(accessToken);
        orderConfirmVo.setCartItemList(cartItemList);

        cartItemList.forEach((cartItem) -> {
            orderConfirmVo.setCount(orderConfirmVo.getCount() + cartItem.getCount());
            orderConfirmVo.setProductTotalPrice(orderConfirmVo
                    .getProductTotalPrice().add(cartItem.getItemPrice()));
        });

        List<Coupon> couponList = couponService.getCouponListByMemberId(id);
        orderConfirmVo.setCouponList(couponList);

        String token = UUID.randomUUID().toString().replace("-", "");
        token = token + "_" + System.currentTimeMillis() + "_" + 60 * 10 * 1000;
        stringRedisTemplate.opsForSet().add(SysCacheConstant.ORDER_UNIQUE_TOKEN, token);
        orderConfirmVo.setOrderToken(token);

        orderConfirmVo.setCouponDiscount(new BigDecimal("5.5"));

        orderConfirmVo.setTransPrice(new BigDecimal("10"));

        orderConfirmVo.setTotalPrice(orderConfirmVo.getProductTotalPrice()
                .add(orderConfirmVo.getTransPrice())
                .subtract(orderConfirmVo.getCouponDiscount()));

        return orderConfirmVo;
    }

    /**
     * 订单正式创建
     * @param fromPortalPrice
     * @param addressId
     * @param note
     * @return
     */
    @Transactional
    @Override
    public OrderCreateVo createOrder(BigDecimal fromPortalPrice, Long addressId, String note) {
        OrderCreateVo orderCreateVo = new OrderCreateVo();
        //防重复
        String orderToken = RpcContext.getContext().getAttachment("orderToken");
        if (StringUtils.isEmpty(orderToken)) {
            orderCreateVo.setToken("出现错误，令牌为空，请重试");
            return orderCreateVo;
        }

        String[] split = orderToken.split("_");
        if (split.length != 3) {
            //可能是伪造
            orderCreateVo.setToken("出现错误，令牌结构错误，请重试");
            return orderCreateVo;
        } else {
            long createTime = Long.parseLong(split[1]);
            long timeLimit = Long.parseLong(split[2]);
            if (System.currentTimeMillis() - createTime >= timeLimit) {
                orderCreateVo.setToken("订单超时，请重试");
                return orderCreateVo;
            }
        }
        //验证是否重复提交
        Long remove = stringRedisTemplate.opsForSet().remove(SysCacheConstant.ORDER_UNIQUE_TOKEN, orderToken);
        if (remove != 1) {
            orderCreateVo.setToken("创建失败，请勿重复操作"); //重复
            return orderCreateVo;
        }

        //验证登录
        String accessToken = RpcContext.getContext().getAttachment("accessToken");
        if (!validPrice(fromPortalPrice, accessToken)) {
            //比价失败
            orderCreateVo.setLimit(false);//比价失败
            return orderCreateVo;
        }
        //价格正常
        Member member = memberComponent.getMemberByAccessToken(accessToken);
        String orderSn = IdWorker.getTimeId();
        orderCreateVo = initOrderCreateVo(fromPortalPrice, addressId, accessToken, orderCreateVo, member, orderSn);
        //加工处理数据
        //保存订单信息
        Order order = initOrder(fromPortalPrice, addressId, note, member, orderSn);
        //订单保存到数据库
        orderMapper.insert(order);
        //2.订单项
        saveOrderItem(orderSn, order, accessToken);


        return orderCreateVo;

    }



    /**
     * 保存订单项
     * @param orderSn
     * @param order
     */
    private void saveOrderItem(String orderSn, Order order, String accessToken) {
        List<CartItem> cartItemList = threadLocalCartItemList.get();
//        List<OrderItem> orderItemList = new ArrayList<>();
        List<Long> skuIdList = new ArrayList<>();
        cartItemList.forEach((cartItem) -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setOrderSn(orderSn);
            Long skuId = cartItem.getSkuId();
            skuIdList.add(skuId);
            EsProduct esProduct = productService.productSkuInfo(skuId);
            SkuStock skuStock = threadLocalSkuStockMap.get().get(skuId);
            orderItem.setProductId(esProduct.getId());
            orderItem.setProductPic(esProduct.getPic());
            orderItem.setProductName(esProduct.getName());
            orderItem.setProductBrand(esProduct.getBrandName());
            orderItem.setProductSn(esProduct.getProductSn());
            orderItem.setProductPrice(cartItem.getPrice());
            orderItem.setProductQuantity(cartItem.getCount());
            orderItem.setProductSkuId(skuStock.getId());
            orderItem.setProductSkuCode(skuStock.getSkuCode());
            orderItem.setProductCategoryId(esProduct.getProductCategoryId());
            orderItem.setSp1(skuStock.getSp1());
            orderItem.setSp2(skuStock.getSp2());
            orderItem.setSp3(skuStock.getSp3());
            List<EsSkuProductInfo> skuProductInfos = esProduct.getSkuProductInfos();
            for (EsSkuProductInfo skuProductInfo : skuProductInfos) {
                if (skuId.equals(skuProductInfo.getId())) {
                    List<EsProductAttributeValue> values = skuProductInfo.getAttributeValues();
                    String json = JSON.toJSONString(values);
                    orderItem.setProductAttr(json);
                }
            }
            orderItemMapper.insert(orderItem);
//            orderItemList.add(orderItem);
        });

        //清除购物项
        cartService.delCartItems(skuIdList, accessToken);

    }

    /**
     * 初始化订单Vo
     * @param fromPortalPrice
     * @param addressId
     * @param accessToken
     * @param orderCreateVo
     * @param member
     * @param orderSn
     */
    private OrderCreateVo initOrderCreateVo(BigDecimal fromPortalPrice, Long addressId, String accessToken, OrderCreateVo orderCreateVo, Member member, String orderSn) {
        orderCreateVo.setOrderSn(orderSn);
        orderCreateVo.setAddressId(addressId);
        orderCreateVo.setTotalPrice(fromPortalPrice);
        List<CartItem> cartItemList = cartService.getCartItemListForOrder(accessToken);
        orderCreateVo.setCartItemList(cartItemList);
        orderCreateVo.setMemberId(member.getId());
        orderCreateVo.setDetailInfo(String.valueOf(cartItemList.size()));
        return orderCreateVo;
    }

    /**
     * 初始化订单基本信息
     * @param fromPortalPrice
     * @param addressId
     * @param note
     * @param member
     * @param orderSn
     */
    private Order initOrder(BigDecimal fromPortalPrice, Long addressId, String note, Member member, String orderSn) {
        Order order = new Order();
        order.setMemberId(member.getId());
        order.setOrderSn(orderSn);
        order.setCreateTime(new Date());
        order.setNote(note);
        order.setMemberUsername(member.getUsername());
        order.setTotalAmount(fromPortalPrice);
        order.setFreightAmount(new BigDecimal("10"));
        order.setStatus(OrderStatusEnume.UNPAY.getCode());
        //收货人信息
        MemberReceiveAddress address = memberService.getOrderAddressById(addressId);
        order.setReceiverDetailAddress(address.getDetailAddress());
        order.setReceiverName(address.getName());
        order.setReceiverPhone(address.getPhoneNumber());
        order.setReceiverPostCode(address.getPostCode());
        order.setReceiverProvince(address.getProvince());
        order.setReceiverCity(address.getCity());
        order.setReceiverProvince(address.getProvince());
        return order;
    }

    /**
     * 前后端价格验证
     * @param fromPortalPrice
     * @param accessToken
     * @return
     */
    private Boolean validPrice(BigDecimal fromPortalPrice, String accessToken) {
        List<CartItem> cartItemList = cartService.getCartItemListForOrder(accessToken);
        threadLocalCartItemList.set(cartItemList);
        BigDecimal calcPrice = new BigDecimal("0");
        Map<Long, SkuStock> skuStockMap = new HashMap<>();
        //要计算当前实时更新的商品价格
        for (CartItem cartItem : cartItemList) {
//            calcPrice.add(cartItem.getItemPrice());
//            BigDecimal currentPrice = skuStockService.getSkuPriceBySkuId(cartItem.getSkuId());
            SkuStock skuStock = skuStockService.getById(cartItem.getSkuId());
            BigDecimal currentPrice = skuStock.getPrice();
            cartItem.setPrice(currentPrice);
            BigDecimal currentItemPrice = currentPrice.multiply(new BigDecimal(cartItem.getCount()));
            cartItem.setItemPrice(currentItemPrice);
            calcPrice = calcPrice.add(currentItemPrice);
            Long skuId = skuStock.getId();
            skuStockMap.put(skuId, skuStock);
        }
        threadLocalSkuStockMap.set(skuStockMap);

        int i = calcPrice.compareTo(fromPortalPrice);
        return i == 0 ? true : false;
    }

}
