package com.fanchen.controller;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanchen.annotation.Log;
import com.fanchen.common.lang.Result;
import com.fanchen.entity.SysOperateLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * <p>
 * 操作日志记录 前端控制器
 * </p>
 *
 * @author fanchen
 * @since 2021-12-05
 */
@RestController
@RequestMapping("/sys/operateLog")
public class SysOperateLogController extends BaseController<SysOperateLog> {

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('monitor:operate:list')")
    public Result list(String title, String operName, Integer status, String start, String end){
        LambdaQueryWrapper<SysOperateLog> wrapper = Wrappers.lambdaQuery(SysOperateLog.class);
        wrapper.like(StrUtil.isNotBlank(title), SysOperateLog::getTitle, title);
        wrapper.like(StrUtil.isNotBlank(operName), SysOperateLog::getOperName, operName);
        wrapper.eq(status != null, SysOperateLog::getStatus, status);
        if (StrUtil.isNotBlank(start) && StrUtil.isNotBlank(end)){
            DateTime a = DateUtil.parse(start + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
            DateTime b = DateUtil.parse(end + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
            wrapper.between(SysOperateLog::getOperTime, a, b);
        }
        wrapper.orderByDesc(SysOperateLog::getOperTime);
        Page<SysOperateLog> page = sysOperateLogService.page(getPage(), wrapper);
        return Result.succ(page);
    }

    @DeleteMapping
    @Log(title = "操作日志", businessType = "删除日志")
    @PreAuthorize("hasAnyAuthority('monitor:operate:delete')")
    public Result delete(Long[] ids){
        boolean remove = sysOperateLogService.removeByIds(Arrays.asList(ids));
        if (remove){
            return Result.succ("删除成功！");
        }else {
            return Result.fail("删除失败！");
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('monitor:operate:clear')")
    public Result clear(){
        sysOperateLogService.clearAll();
        return Result.succ("清空成功！");
    }

}

