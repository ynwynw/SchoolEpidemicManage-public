package com.fanchen.controller;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanchen.annotation.Log;
import com.fanchen.common.lang.Result;
import com.fanchen.entity.LeaveApply;
import com.fanchen.entity.SysUser;
import com.fanchen.entity.SysUserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * <p>
 * 请假审批表 前端控制器
 * </p>
 *
 * @author fanchen
 * @since 2021-12-17
 */
@RestController
@RequestMapping("/leave/apply")
public class LeaveApplyController extends BaseController<LeaveApply> {

    @Value("${system.code.teacherRole}")
    private Long teacherRole;

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('leave:apply:list', 'leave:record:list')")
    public Result list(String username, Long deptId, Integer leaveType, Integer status, String start, String end, Principal principal) {
        SysUser sysUser = (SysUser) redisUtil.get("User:" + principal.getName());
        List<SysUserRole> sysUserRoles = sysUserRoleService.list(new QueryWrapper<SysUserRole>().eq("user_id", sysUser.getId()));
        boolean flag = false;
        for (SysUserRole userRole : sysUserRoles) {
            if (userRole.getRoleId().equals(teacherRole)) {
                flag = true;
                break;
            }
        }
        LambdaQueryWrapper<LeaveApply> wrapper = Wrappers.lambdaQuery(LeaveApply.class);
        if (flag) {
            wrapper.like(StrUtil.isNotBlank(username), LeaveApply::getUsername, username);
            wrapper.eq(deptId != null, LeaveApply::getDeptId, deptId);
            wrapper.eq(leaveType != null, LeaveApply::getLeaveType, leaveType);
            wrapper.eq(status != null, LeaveApply::getStatus, status);
            if (StrUtil.isNotBlank(start) && StrUtil.isNotBlank(end)) {
                DateTime a = DateUtil.parse(start + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
                DateTime b = DateUtil.parse(end + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
                wrapper.between(LeaveApply::getCreateTime, a, b);
            }
        } else {
            wrapper.eq(LeaveApply::getUsername, principal.getName());
        }
        wrapper.orderByDesc(LeaveApply::getCreateTime);
        Page<LeaveApply> page = leaveApplyService.page(getPage(), wrapper);
        return Result.succ(page);
    }

    @PostMapping
    @Log(title = "请假管理", businessType = "新增请假")
    @PreAuthorize("hasAnyAuthority('leave:apply:save')")
    public Result save(@Validated @RequestBody LeaveApply leaveApply, Principal principal) {
        SysUser sysUser = (SysUser) redisUtil.get("User:" + principal.getName());
        leaveApply.setStatus(1);
        leaveApply.setUsername(sysUser.getUsername());
        leaveApply.setDeptId(sysUser.getDeptId());
        leaveApply.setNickname(sysUser.getNickname());
        boolean save = leaveApplyService.save(leaveApply);
        return save ? Result.succ("请假提交成功！") : Result.fail("请假提交失败！");
    }

    @PutMapping
    @Log(title = "请假管理", businessType = "修改请假")
    @PreAuthorize("hasAnyAuthority('leave:apply:update', 'leave:record:examine')")
    public Result update(@Validated @RequestBody LeaveApply leaveApply){
        boolean update = leaveApplyService.updateById(leaveApply);
        return update ? Result.succ("更新成功！") : Result.fail("更新失败！");
    }

}

