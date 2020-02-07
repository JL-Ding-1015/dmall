package com.djl.dmall.cart;

import com.djl.dmall.cart.vo.Cart;
import com.djl.dmall.cart.vo.CartItem;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;

@SpringBootTest
class DmallCartApplicationTests {
    @Autowired
    RedissonClient redissonClient;

    @Test
    public void userRedissonMap(){

    }


    @Test
    void contextLoads() {

        CartItem item1 = new CartItem();
        item1.setCount(2);
        item1.setPrice(new BigDecimal(10.23));
        CartItem item2 = new CartItem();
        item2.setCount(4);
        item2.setPrice(new BigDecimal(15.67));

        Cart cart = new Cart();
        cart.setCartItems(Arrays.asList(item1,item2));
        System.out.println(cart.getCount() + "-->" + cart.getTotalPrice());

    }

}
