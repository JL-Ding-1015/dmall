package com.djl.dmall.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.djl.dmall.contant.SysCacheConstant;
import com.djl.dmall.to.CommonResult;
import com.djl.dmall.ums.entity.Member;
import com.djl.dmall.ums.service.MemberService;
import com.djl.dmall.vo.ums.LoginResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
public class LoginController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Reference
    MemberService memberService;

    @ResponseBody
    @PostMapping("/userInfo")
    public CommonResult getUserInfo(@RequestParam("accessToken") String accessToken){
        String redisKey = SysCacheConstant.LOGIN_MEMBER + accessToken;

        String memberJson = stringRedisTemplate.opsForValue().get(redisKey);

        Member member = JSON.parseObject(memberJson, Member.class);
        member.setId(null);
        member.setPassword(null);

        return new CommonResult().success(member);
    }

    @PostMapping("/applogin")
    @ResponseBody
    public CommonResult loginForDmall(@RequestParam("username") String username,
                                      @RequestParam("password") String password) {
        Member member = memberService.login(username, password);
        if (member == null) {
            //没用该用户
            CommonResult result = new CommonResult().failed();
            result.setMessage("用户名或密码错误");
            return result;
        } else {
            String token = UUID.randomUUID().toString().replace("-", "");
            String memberJson = JSON.toJSONString(member);
            stringRedisTemplate.opsForValue()
                    .set(SysCacheConstant.LOGIN_MEMBER + token, memberJson,
                            SysCacheConstant.LOGIN_MEMBER_TIMEOUT, TimeUnit.MINUTES);
            LoginResponseVo vo = new LoginResponseVo();
            BeanUtils.copyProperties(member, vo);
            vo.setAccessToken(token);
            return new CommonResult().success(vo);
        }

    }


    @GetMapping("/login")
    public String login(@RequestParam("redirect_url") String redirect_url,
                        @CookieValue(value = "sso_user", required = false) String ssoUserCookie,
                        HttpServletResponse response,
                        Model model) throws IOException {
        if (StringUtils.isEmpty(ssoUserCookie)) {
            //没登录过
            model.addAttribute("redirect_url", redirect_url);
            return "login";
        } else {
            //登录过了
            String url = redirect_url + "?sso_user=" + ssoUserCookie;
            response.sendRedirect(url);
            return null;
        }

    }

    @PostMapping("/dologin")
    public String doLogin(String username, String password, HttpServletResponse response,
                          String redirect_url) throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("myLoginUser", username);
        map.put("email", username + "@qq.com");

        String token = UUID.randomUUID().toString().replace("-", "");
        stringRedisTemplate.opsForValue().set(token, JSON.toJSONString(map));

        Cookie cookie = new Cookie("sso_user", token);
        response.addCookie(cookie);
        String url = redirect_url + "?sso_user=" + token;
        response.sendRedirect(url);
        return null;
    }

}
