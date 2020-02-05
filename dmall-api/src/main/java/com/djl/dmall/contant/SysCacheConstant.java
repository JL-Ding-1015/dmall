package com.djl.dmall.contant;

/**
 * 系统中使用的常量
 */
public class SysCacheConstant {

    public static final String CATEGORY_MENU_CACHE_KEY = "sys_category";
    /**
     * redis中存储登录用户的key，
     * login:member:+token = {用户信息}
     */
    public static final String LOGIN_MEMBER = "login:member:";
    /**
     * redis中用户储存信息的默认超时时间
     */
    public static final Integer LOGIN_MEMBER_TIMEOUT = 30;

}
