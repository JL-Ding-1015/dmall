package com.djl.dmall.cart.service.Impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.djl.dmall.cart.component.MemberComponent;
import com.djl.dmall.cart.service.CartService;
import com.djl.dmall.cart.vo.Cart;
import com.djl.dmall.cart.vo.CartItem;
import com.djl.dmall.cart.vo.CartResponse;
import com.djl.dmall.contant.CartConstant;
import com.djl.dmall.pms.entity.Product;
import com.djl.dmall.pms.entity.SkuStock;
import com.djl.dmall.pms.service.ProductService;
import com.djl.dmall.pms.service.SkuStockService;
import com.djl.dmall.ums.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@Component
public class CartServiceImpl implements CartService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    MemberComponent memberComponent;

    @Autowired
    RedissonClient redissonClient;

    @Reference
    SkuStockService skuStockService;

    @Reference
    ProductService productService;

    /**
     * 添加商品到购物车
     * @param skuId
     * @param num
     * @param cartKey
     * @param accessToken
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Override
    public CartResponse addToCart(Long skuId, Integer num, String cartKey, String accessToken) throws ExecutionException, InterruptedException {
        String redisCartKey = "";
        //1.是登录，在线购物车 cart:user:id
        //2.否登录，离线购物车 cart:temp:cartKey
        //3.都没有 新建一个临时购物车
        if (!StringUtils.isEmpty(accessToken)) {
            Member member = memberComponent.getMemberByAccessToken(accessToken);
            if (member != null) {
                //登陆了
                if (!StringUtils.isEmpty(cartKey)) {
                    mergeCart(cartKey, member.getId());
                }
            }
        }
        redisCartKey = memberComponent.getCartKey(accessToken, cartKey);
        CartItem cartItem = addItemToCart(skuId, num, redisCartKey);
        CartResponse cartResponse = new CartResponse();
        cartResponse.setCartItem(cartItem);
        cartResponse.setCart(listCart(cartKey, accessToken).getCart());
        if (StringUtils.isEmpty(cartKey)) {
            cartResponse.setCartKey(redisCartKey.split(":")[2]);
        }
        return cartResponse;
    }

    /**
     * 更新购物车
     *
     * @param skuId
     * @param num
     * @param cartKey
     * @param accessToken
     * @return
     */
    @Override
    public CartResponse updateCartItemNum(Long skuId, Integer num, String cartKey, String accessToken) {
        String redisCartKey = memberComponent.getCartKey(accessToken, cartKey);
        RMap<String, String> cartMap = redissonClient.getMap(redisCartKey);
        if (cartMap != null && !cartMap.isEmpty()) {
            String itemJson = cartMap.get(skuId.toString());
            if (StringUtils.isEmpty(itemJson)) {
                return new CartResponse();
            } else {
                CartItem cartItem = JSON.parseObject(itemJson, CartItem.class);
                cartItem.setCount(num);

                String updatedJson = JSON.toJSONString(cartItem);
                cartMap.put(skuId.toString(), updatedJson);

                CartResponse cartResponse = new CartResponse();
                cartResponse.setCartItem(cartItem);
                cartResponse.setCart(listCart(cartKey, accessToken).getCart());
                return cartResponse;
            }
        } else {
            return new CartResponse();
        }


    }

    /**
     * 查询购物车的全部数据
     *
     * @param cartKey
     * @param accessToken
     * @return
     */
    @Override
    public CartResponse listCart(String cartKey, String accessToken) {
        String redisCartKey = memberComponent.getCartKey(accessToken, cartKey);
        //判断是否合并
        if (!StringUtils.isEmpty(accessToken)) {
            Member member = memberComponent.getMemberByAccessToken(accessToken);
            if (member != null) {
                //用户登陆成功
                mergeCart(cartKey, member.getId());
            }
        }
        //查询购物车的数据
        RMap<String, String> cartMap = redissonClient.getMap(redisCartKey);
        Cart cart = new Cart();
        ArrayList<CartItem> cartItems = new ArrayList<>();
        CartResponse cartResponse = new CartResponse();
        if (cartMap != null && !cartMap.isEmpty()) {
            //购物车有东西
            cartMap.entrySet().forEach((entry) -> {
                String key = entry.getKey();
                if(!key.equalsIgnoreCase("checked")){
                    String skuId = entry.getKey();
                    String itemJson = entry.getValue();
                    CartItem cartItem = JSON.parseObject(itemJson, CartItem.class);
                    cartItems.add(cartItem);
                }
            });
            cart.setCartItems(cartItems);
        } else {
            String[] split = redisCartKey.split(":");
            if (split[1].equalsIgnoreCase("temp")) {
                cartResponse.setCartKey(redisCartKey.split(":")[2]);
            }

        }
        cartResponse.setCart(cart);
        return cartResponse;
    }

    /**
     * 删除单独购物条目
     *
     * @param skuId
     * @param cartKey
     * @param accessToken
     * @return
     */
    @Override
    public CartResponse delCartItem(Long skuId, String cartKey, String accessToken) {
        String redisCartKey = memberComponent.getCartKey(accessToken, cartKey);

        RMap<Object, Object> cartMap = redissonClient.getMap(redisCartKey);

        cartMap.remove(skuId.toString());
        changeItemStatus(new Long[]{skuId}, false, redisCartKey);

        CartResponse cartResponse = listCart(cartKey, accessToken);

        return cartResponse;
    }

    /**
     * 清空购物车
     * @param cartKey
     * @param accessToken
     * @return
     */
    @Override
    public CartResponse clearCart(String cartKey, String accessToken) {
        String redisCartKey = memberComponent.getCartKey(accessToken, cartKey);
        RMap<String, String> cartMap = redissonClient.getMap(redisCartKey);
        cartMap.clear();
        return new CartResponse();
    }

    /**
     * 修改多条商品的选中状态
     * @param skuIds
     * @param ops
     * @param cartKey
     * @param accessToken
     * @return
     */
    @Override
    public CartResponse checkCartItems(Long[] skuIds, Integer ops, String cartKey, String accessToken) {
        boolean checked = ops == 1 ? true : false;
        //修改购物项状态
        String redisCartKey = memberComponent.getCartKey(accessToken, cartKey);
        RMap<String, String> cartMap = redissonClient.getMap(redisCartKey);
        if (cartMap != null && !cartMap.isEmpty()) {
            if(skuIds.length != 0){
                for (Long skuId : skuIds) {
                    String itemJson = cartMap.get(skuId.toString());
                    CartItem cartItem = JSON.parseObject(itemJson, CartItem.class);
                    cartItem.setCheck(checked);
                    cartMap.put(skuId.toString(), JSON.toJSONString(cartItem));
                }
            }
        }
        //修改checked集合的状态
        changeItemStatus(skuIds, checked, redisCartKey);

        CartResponse cartResponse = new CartResponse();
        return cartResponse;
    }

    /**
     * 修改选中状态子方法
     * @param skuIds
     * @param checked
     * @param redisCartKey
     */
    public void changeItemStatus(Long[] skuIds, boolean checked, String redisCartKey) {
        RMap<String, String> cartMap = redissonClient.getMap(redisCartKey);
        String checkedJson = cartMap.get(CartConstant.CART_CHECKED_KEY);
        Set<Long> checkedSet = JSON.parseObject(checkedJson, new TypeReference<Set<Long>>() {
        });
        if(checkedSet == null || checkedSet.size() == 0){
            checkedSet = new HashSet<>();
        }
        if(checked){
            //如果是选中状态
            checkedSet.addAll(Arrays.asList(skuIds));
            log.debug("选中的商品Set是：{}",checkedSet);
        }else {
            //不选中
            checkedSet.removeAll(Arrays.asList(skuIds));
            log.debug("选中的商品Set是：{}",checkedSet);
        }
        cartMap.put(CartConstant.CART_CHECKED_KEY, JSON.toJSONString(checkedSet));
    }

    /**
     * 当未登录下添加了几件商品，而且用户购物车中本来就有商品时，登录时要进行合并
     *
     * @param cartKey
     * @param memberId
     */
    private void mergeCart(String cartKey, Long memberId) {
        String tempCartKey = CartConstant.TEMP_CART_KEY_PREFIX + cartKey;
        String userCartKey = CartConstant.USER_CART_KEY_PREFIX + memberId;
        RMap<String, String> cartMap = redissonClient.getMap(tempCartKey);
        if (cartMap != null && !cartMap.isEmpty()) {
            cartMap.entrySet().forEach((item) -> {
                String key = item.getKey();
                if(!key.equalsIgnoreCase("checked")){
                    CartItem cartItem = JSON.parseObject(item.getValue(), CartItem.class);
                    try {
                        addItemToCart(Long.parseLong(key), cartItem.getCount(), userCartKey);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            });
            cartMap.clear();
        }


    }

    /**
     * 添加商品到购物车子方法
     *
     * @param skuId
     * @param redisCartKey
     * @return
     */
    private CartItem addItemToCart(Long skuId, Integer num, String redisCartKey) throws ExecutionException, InterruptedException {
        CartItem newCartItem = new CartItem();
        //按照id查找sku属性
        CompletableFuture<Void> skuFuture = CompletableFuture.supplyAsync(() -> {
            SkuStock skuStock = skuStockService.getById(skuId);
            return skuStock;
        }).thenAcceptAsync(skuStock -> {
            Long productId = skuStock.getProductId();
            Product product = productService.getById(productId);
            BeanUtils.copyProperties(skuStock, newCartItem);
            newCartItem.setId(skuStock.getId());
            newCartItem.setName(product.getName());
            newCartItem.setCount(num);
        });

        //查出skuId在数据库对应的最新详情,远程查询
        RMap<String, String> map = redissonClient.getMap(redisCartKey);
        //获取购物车中这个skuId对应的购物项
        String itemJson = map.get(skuId.toString());
        changeItemStatus(new Long[]{skuId},true, redisCartKey);

        skuFuture.get();//在线等结果
        if (!StringUtils.isEmpty(itemJson)) {
            //原来里面就有相同的商品
            CartItem oldItem = JSON.parseObject(itemJson, CartItem.class);
            Integer count = oldItem.getCount();


            newCartItem.setCount(count + newCartItem.getCount());
            String newItemJson = JSON.toJSONString(newCartItem);
            map.put(skuId.toString(), newItemJson);
        } else {
            //新加一个商品
            String newItemJson = JSON.toJSONString(newCartItem);
            map.put(skuId.toString(), newItemJson);
        }


        return newCartItem;
    }
}
