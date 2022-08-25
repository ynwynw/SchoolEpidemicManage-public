package com.fanchen.controller;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanchen.common.lang.Result;
import com.fanchen.entity.AccessReturn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 未归用户表 前端控制器
 * </p>
 *
 * @author fanchen
 * @since 2021-12-17
 */
@RestController
@RequestMapping("/access/return")
public class AccessReturnController extends BaseController<AccessReturn> {

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('access:return:list')")
    public Result list(String name, String dept, String start, String end){
        LambdaQueryWrapper<AccessReturn> wrapper = Wrappers.lambdaQuery(AccessReturn.class);
        wrapper.like(StrUtil.isNotBlank(name), AccessReturn::getName, name);
        wrapper.like(StrUtil.isNotBlank(dept), AccessReturn::getDept, dept);
        if (StrUtil.isNotBlank(start) && StrUtil.isNotBlank(end)){
            DateTime a = DateUtil.parse(start + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
            DateTime b = DateUtil.parse(end + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
            wrapper.between(AccessReturn::getCreateTime, a, b);
        }
        wrapper.orderByDesc(AccessReturn::getCreateTime);
        Page<AccessReturn> page = accessReturnService.page(getPage(), wrapper);
        return Result.succ(page);
    }

}

