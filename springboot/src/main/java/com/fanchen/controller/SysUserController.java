package com.fanchen.controller;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanchen.annotation.Log;
import com.fanchen.common.lang.Const;
import com.fanchen.common.lang.Result;
import com.fanchen.entity.SysUser;
import com.fanchen.entity.SysUserRole;
import com.fanchen.utils.UploadUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fanchen
 * @since 2021-11-29
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends BaseController<SysUser> {

    @Resource
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('sys:user:list')")
    public Result list(String userName, String phoneNumber, Integer status, Long deptId){
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(userName), "username", userName);
        wrapper.like(StrUtil.isNotBlank(phoneNumber), "phone_number", phoneNumber);
        wrapper.eq(status != null, "status", status);
        wrapper.eq(deptId != null, "dept_id", deptId);
        wrapper.select(SysUser.class, user -> !user.getColumn().equals("password"));
        Page<SysUser> page = sysUserService.page(getPage(), wrapper);
        return Result.succ(page);
    }

    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id){
        QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", id);
        List<SysUserRole> list = sysUserRoleService.list(wrapper);
        Assert.notNull(list, "找不到相关数据");
        return Result.succ(list);
    }

    @PostMapping
    @Log(title = "用户管理", businessType = "添加用户")
    @PreAuthorize("hasAnyAuthority('sys:user:save')")
    public Result add(@Validated @RequestBody SysUser sysUser){
        sysUser.setPassword(bCryptPasswordEncoder.encode(Const.DEFAULT_PASSWORD));
        sysUser.setAvatar("https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png");
        boolean save = sysUserService.save(sysUser);
        if (save){
            return Result.succ("添加成功！");
        }else {
            return Result.fail("添加失败！");
        }
    }

    @PutMapping
    @Log(title = "用户管理", businessType = "修改用户")
    @PreAuthorize("hasAnyAuthority('sys:user:update')")
    public Result update(@Validated @RequestBody SysUser sysUser){
        boolean update = sysUserService.updateById(sysUser);
        if (update){
            return Result.succ("修改成功！");
        }else {
            return Result.fail("修改失败！");
        }
    }

    @DeleteMapping
    @Log(title = "用户管理", businessType = "删除用户")
    @PreAuthorize("hasAnyAuthority('sys:user:delete')")
    public Result delete(Long[] ids){
        boolean remove = sysUserService.removeByIds(Arrays.asList(ids));
        if (remove){
            sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("user_id", ids));
            return Result.succ("删除成功！");
        }else {
            return Result.fail("删除失败！");
        }
    }

    @PostMapping("/userRole/{id}")
    @Log(title = "用户管理", businessType = "分配角色")
    @PreAuthorize("hasAnyAuthority('sys:user:role')")
    public Result userRole(@PathVariable Long id, @RequestBody Long[] roleIds, Principal principal){
        List<SysUserRole> sysUserRoles = new ArrayList<>();
        for (Long roleId : roleIds) {
            SysUserRole userRole = new SysUserRole();
            userRole.setRoleId(roleId);
            userRole.setUserId(id);
            sysUserRoles.add(userRole);
        }
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().eq("user_id", id));
        boolean saveBatch = sysUserRoleService.saveBatch(sysUserRoles);
        if (saveBatch){
            sysUserService.clearUserAuthority(principal.getName());
            return Result.succ("操作成功！");
        }else {
            return Result.fail("操作失败！");
        }
    }

    @PostMapping("/reset")
    @Log(title = "用户管理", businessType = "重置密码")
    @PreAuthorize("hasAnyAuthority('sys:user:repass')")
    public Result resetPwd(@Validated @RequestBody SysUser sysUser){
        String password = sysUser.getPassword();
        sysUser.setPassword(bCryptPasswordEncoder.encode(password));
        boolean update = sysUserService.updateById(sysUser);
        if (update){
            return Result.succ("重置密码成功！");
        }else {
            return Result.fail("重置密码失败！");
        }
    }

    @PostMapping("/updateInfo")
    @Log(title = "用户管理", businessType = "用户更新信息")
    @PreAuthorize("hasAnyAuthority('sys:user:update')")
    public Result updateInfo(@RequestBody SysUser sysUser, Principal principal){
        String name = principal.getName();
        boolean update = sysUserService.updateById(sysUser);
        if (update){
            redisUtil.set("User:" + name, sysUserService.getById(sysUser.getId()), 3660);
            return Result.succ("更新个人信息成功！");
        }else {
            return Result.fail("更新个人信息失败！");
        }
    }

    @GetMapping("/updatePassword")
    @Log(title = "用户管理", businessType = "用户重置密码")
    @PreAuthorize("hasAnyAuthority('sys:user:update')")
    public Result updatePassword(String oldPassword, String newPassword, String confirmPassword, Principal principal){
        if (!newPassword.equals(confirmPassword)){
            return Result.fail("两次输入的密码不一致！");
        }
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", principal.getName());
        SysUser sysUser = sysUserService.getOne(wrapper);
        if (!bCryptPasswordEncoder.matches(oldPassword, sysUser.getPassword())){
            return Result.fail("旧密码错误！");
        }
        sysUser.setPassword(bCryptPasswordEncoder.encode(confirmPassword));
        boolean update = sysUserService.updateById(sysUser);
        if (update){
            return Result.succ("密码修改成功！");
        }else {
            return Result.fail("密码修改失败！");
        }
    }

    @PostMapping("/avatar")
    @Log(title = "用户管理", businessType = "用户修改头像")
    public Result avatar(@RequestParam("avatarFile")MultipartFile file, @RequestParam("user") String userJson) throws JsonProcessingException {
        if (!file.isEmpty()){
            String uploadImg = UploadUtil.uploadImg(file);
            if (StrUtil.isEmpty(uploadImg)){
                return Result.fail("头像上传失败！");
            }
            ObjectMapper mapper = new ObjectMapper();
            SysUser sysUser = mapper.readValue(userJson, SysUser.class);
            sysUser.setPassword(null);
            sysUser.setAvatar(Const.IMG_PATH + uploadImg);
            boolean update = sysUserService.updateById(sysUser);
            if (update){
                redisUtil.set("User:" + sysUser.getUsername(), sysUser, 3660);
                return Result.succ("头像上传成功！");
            }
        }
        return Result.fail("头像上传失败！");
    }

}

