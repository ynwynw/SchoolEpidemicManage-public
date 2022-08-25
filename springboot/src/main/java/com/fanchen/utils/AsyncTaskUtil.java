package com.fanchen.utils;

import cn.hutool.core.util.StrUtil;
import com.fanchen.entity.SysLoginInfo;
import com.fanchen.entity.SysOperateLog;
import com.fanchen.service.SysLoginInfoService;
import com.fanchen.service.SysOperateLogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Component
@Slf4j
public class AsyncTaskUtil {

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private SysLoginInfoService sysLoginInfoService;

    @Resource
    private SysOperateLogService sysOperateLogService;

    public void recordLoginInfo(String username, Integer status, String message, Date date){
        HttpServletRequest request = ServletUtil.getRequest();
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr(request);
        String os = userAgent.getOperatingSystem().getName();
        String browser = userAgent.getBrowser().getName();
        String rspStr = HttpUtils.sendGet("http://whois.pconline.com.cn/ipJson.jsp", "ip=" + ip + "&json=true", "GBK");
        ObjectMapper objectMapper = new ObjectMapper();
        SysLoginInfo loginInfo = new SysLoginInfo();
        if (StrUtil.isNullOrUndefined(username)){
            username = request.getParameter("username");
        }
        loginInfo.setIp(ip);
        loginInfo.setBrowser(browser);
        loginInfo.setOs(os);
        loginInfo.setUsername(username);
        loginInfo.setStatus(status);
        loginInfo.setMsg(message);
        loginInfo.setLoginTime(date);
        try {
            Map<String,String> map = objectMapper.readValue(rspStr, Map.class);
            loginInfo.setLoginLocation(map.get("addr"));
        } catch (JsonProcessingException e) {
            loginInfo.setLoginLocation("未知地址");
            log.warn(e.getMessage());
        }
        CompletableFuture<Void> loginFuture = CompletableFuture
                .runAsync(() -> sysLoginInfoService.save(loginInfo), threadPoolTaskExecutor);
        try {
            CompletableFuture.allOf(loginFuture).get();
        } catch (InterruptedException | ExecutionException e) {
            log.warn("登录信息记录失败，详细信息如下：" + e.getMessage());
        }
    }

    public void recordOperateInfo(SysOperateLog sysOperateLog){
        String rspStr = HttpUtils.sendGet("http://whois.pconline.com.cn/ipJson.jsp", "ip=" + sysOperateLog.getOperIp() + "&json=true", "GBK");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String,String> map = objectMapper.readValue(rspStr, Map.class);
            sysOperateLog.setOperLocation(map.get("addr"));
        } catch (JsonProcessingException e) {
            sysOperateLog.setOperLocation("未知地址");
            log.warn(e.getMessage());
        }
        sysOperateLog.setOperTime(new Date());
        CompletableFuture<Void> operateFuture = CompletableFuture
                .runAsync(() -> sysOperateLogService.save(sysOperateLog), threadPoolTaskExecutor);
        try {
            CompletableFuture.allOf(operateFuture).get();
        } catch (InterruptedException | ExecutionException e) {
            log.warn("操作信息记录失败，详细信息如下：" + e.getMessage());
        }
    }

}
