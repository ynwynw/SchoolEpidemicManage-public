package com.fanchen.controller;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanchen.annotation.Log;
import com.fanchen.common.lang.Const;
import com.fanchen.common.lang.Result;
import com.fanchen.entity.HealthReport;
import com.fanchen.entity.SysUser;
import com.fanchen.entity.SysUserRole;
import com.fanchen.utils.UploadUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

/**
 * <p>
 * 二码一报告表 前端控制器
 * </p>
 *
 * @author fanchen
 * @since 2021-12-16
 */
@RestController
@RequestMapping("/health/report")
public class HealthReportController extends BaseController<HealthReport> {

    @Value("${system.code.teacherRole}")
    private Long teacherRole;

    @GetMapping
    public Result check(Principal principal){
        String name = principal.getName();
        int count = healthReportService.checkReportToday(name);
        return count > 0 ? Result.succ("今日已上报，请不要重复上报！") : Result.succ("allow");
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('health:report:list')")
    public Result list(String username, Long deptId, Integer type, Principal principal, String start, String end){
        String name = principal.getName();
        SysUser sysUser = (SysUser) redisUtil.get("User:" + name);
        List<SysUserRole> sysUserRoles = sysUserRoleService.
                list(new QueryWrapper<SysUserRole>().eq("user_id", sysUser.getId()));
        boolean flag = false;
        for (SysUserRole userRole : sysUserRoles) {
            if (userRole.getRoleId().equals(teacherRole)){
                flag = true;
                break;
            }
        }
        LambdaQueryWrapper<HealthReport> wrapper = Wrappers.lambdaQuery(HealthReport.class);
        if (flag){
            wrapper.like(StrUtil.isNotBlank(username), HealthReport::getUsername, username);
            wrapper.eq(deptId != null, HealthReport::getDeptId, deptId);
            wrapper.eq(type != null, HealthReport::getType, type);
            if (StrUtil.isNotBlank(start) && StrUtil.isNotBlank(end)){
                DateTime a = DateUtil.parse(start + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
                DateTime b = DateUtil.parse(end + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
                wrapper.between(HealthReport::getCreateTime, a, b);
            }
        }else {
            wrapper.eq(HealthReport::getUsername, name);
        }
        wrapper.orderByDesc(HealthReport::getCreateTime);
        Page<HealthReport> page = healthReportService.page(getPage(), wrapper);
        return Result.succ(page);
    }

    @PostMapping
    @Log(title = "二码一报告", businessType = "添加报告")
    @PreAuthorize("hasAnyAuthority('health:report:save')")
    public Result save(@RequestParam("file1") MultipartFile file1,
                       @RequestParam("file2") MultipartFile file2,
                       @RequestParam("file3") MultipartFile file3,
                       @RequestParam("type") Integer type, Principal principal){
        HealthReport healthReport = new HealthReport();
        healthReport.setType(type);
        if (!file1.isEmpty() && !file2.isEmpty() && !file3.isEmpty()){
            String img1 = UploadUtil.uploadImg(file1);
            String img2 = UploadUtil.uploadImg(file2);
            String img3 = UploadUtil.uploadImg(file3);
            healthReport.setImg1(Const.IMG_PATH + img1);
            healthReport.setImg2(Const.IMG_PATH + img2);
            healthReport.setImg3(Const.IMG_PATH + img3);
        }
        String username = principal.getName();
        SysUser sysUser = (SysUser) redisUtil.get("User:" + username);
        healthReport.setUsername(sysUser.getUsername());
        healthReport.setPhoneNumber(sysUser.getPhoneNumber());
        healthReport.setDeptId(sysUser.getDeptId());
        boolean save = healthReportService.save(healthReport);
        if (save){
            return Result.succ("上报成功");
        }else {
            return Result.fail("上传失败");
        }
    }

}

