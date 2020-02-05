package com.djl.dmall.client1.controller;

import com.alibaba.fastjson.JSON;
import com.djl.dmall.client1.config.SsoConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    SsoConfigProperties ssoConfigProperties;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 受保护
     *
     * @param model
     * @return
     */
    @GetMapping("/")
    public String index(Model model, @CookieValue(value = "sso_user", required = false) String ssoUserCookie,
                        HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "sso_user", required = false) String ssoUserParam) throws IOException {
        if(!StringUtils.isEmpty(ssoUserParam)){
            //如果是服务器跳转到的
            Cookie cookie = new Cookie("sso_user", ssoUserParam);
            response.addCookie(cookie);
            String json = stringRedisTemplate.opsForValue().get(cookie.getValue());
            Map<String, Object> obj = (Map<String, Object>) JSON.parse(json);
            return "index";
        }


        if (StringUtils.isEmpty(ssoUserCookie)) {
            //没登录
            StringBuffer requestURL = request.getRequestURL();
            response.sendRedirect(ssoConfigProperties.getUrl() + ssoConfigProperties.getLoginPath()
                    + "?redirect_url=" + requestURL);
            return null;

        } else {
            //登陆了
            String json = stringRedisTemplate.opsForValue().get(ssoUserCookie);

            return "index";
        }


    }


}

