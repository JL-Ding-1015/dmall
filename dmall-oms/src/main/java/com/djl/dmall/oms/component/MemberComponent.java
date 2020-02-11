package com.djl.dmall.oms.component;

import com.alibaba.fastjson.JSON;
import com.djl.dmall.contant.CartConstant;
import com.djl.dmall.contant.SysCacheConstant;
import com.djl.dmall.ums.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Component
public class MemberComponent {

    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 根据token获取用户
     * @param accessToken
     * @return
     */
    public Member getMemberByAccessToken(String accessToken){
        String json = redisTemplate.opsForValue().get(SysCacheConstant.LOGIN_MEMBER + accessToken);
        if(!StringUtils.isEmpty(json)){
            return JSON.parseObject(json, Member.class);
        }
        return null;
    }

    /**
     * 判断当前状态，并返回对应的的redisCartKey
     * @param accessToken
     * @param cartKey
     * @return
     */
    public String getCartKey(String accessToken, String cartKey){
        Member member = null;
        if(!StringUtils.isEmpty(accessToken)){
            member = getMemberByAccessToken(accessToken);
        }

        if(member != null){
            //查到用户存在
            return CartConstant.USER_CART_KEY_PREFIX + member.getId();
        }else if(!StringUtils.isEmpty(cartKey)){
            //没查到用户，cartKey不为空
            return CartConstant.TEMP_CART_KEY_PREFIX + cartKey;
        }else{
            //啥都没有，第一次
            String uuid = UUID.randomUUID().toString().replace("-", "");
            return CartConstant.TEMP_CART_KEY_PREFIX + uuid;
        }
    }
}
