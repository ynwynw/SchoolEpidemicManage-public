package com.fanchen.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {
    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication()
    {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static String getLoginUser(){
        try {
            return getAuthentication().getName();
        }catch (Exception e){
            log.error("获取用户信息异常：{}", e.getMessage());
        }
        return null;
    }

}
