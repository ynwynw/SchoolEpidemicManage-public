package com.fanchen.controller;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanchen.annotation.Log;
import com.fanchen.common.lang.Result;
import com.fanchen.entity.AccessRegister;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * <p>
 * 出入登记表 前端控制器
 * </p>
 *
 * @author fanchen
 * @since 2021-12-17
 */
@RestController
@RequestMapping("/access/register")
public class AccessRegisterController extends BaseController<AccessRegister> {

    @PostMapping
    @Log(title = "出入登记", businessType = "添加记录")
    @PreAuthorize("hasAnyAuthority('access:register:save')")
    public Result save(@Validated @RequestBody AccessRegister accessRegister, Principal principal){
        accessRegister.setCreateBy(principal.getName());
        boolean flag = accessRegisterService.addRegister(accessRegister);
        return flag ? Result.succ("登记成功") : Result.fail("登记失败");
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('access:register:list')")
    public Result list(String name, Integer type, String start, String end){
        LambdaQueryWrapper<AccessRegister> wrapper = Wrappers.lambdaQuery(AccessRegister.class);
        wrapper.like(StrUtil.isNotBlank(name), AccessRegister::getName, name);
        wrapper.eq(type != null, AccessRegister::getType, type);
        if (StrUtil.isNotBlank(start) && StrUtil.isNotBlank(end)){
            DateTime a = DateUtil.parse(start + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
            DateTime b = DateUtil.parse(end + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
            wrapper.between(AccessRegister::getCreateTime, a, b);
        }
        wrapper.orderByDesc(AccessRegister::getCreateTime);
        Page<AccessRegister> page = accessRegisterService.page(getPage(), wrapper);
        return Result.succ(page);
    }

}

