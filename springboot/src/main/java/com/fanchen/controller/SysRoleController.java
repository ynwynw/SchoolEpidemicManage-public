package com.fanchen.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanchen.annotation.Log;
import com.fanchen.common.lang.Result;
import com.fanchen.entity.SysRole;
import com.fanchen.entity.SysRoleMenu;
import com.fanchen.entity.SysUserRole;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fanchen
 * @since 2021-11-29
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends BaseController<SysRole> {

    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id){
        SysRole sysRole = sysRoleService.getById(id);
        List<SysRoleMenu> roleMenus = sysRoleMenuService
                .list(new QueryWrapper<SysRoleMenu>().eq("role_id", id));
        List<Long> menuIds = roleMenus.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
        sysRole.setMenuIds(menuIds);
        return Result.succ(sysRole);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('sys:role:list')")
    public Result list(String roleName, String roleKey, Integer status){
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(roleName), "name", roleName);
        wrapper.like(StrUtil.isNotBlank(roleKey), "code", roleKey);
        wrapper.eq(status != null, "status", status);
        Page<SysRole> page = sysRoleService.page(getPage(), wrapper);
        return Result.succ(page);
    }

    @PostMapping
    @Log(title = "角色管理", businessType = "添加角色")
    @PreAuthorize("hasAnyAuthority('sys:role:save')")
    public Result add(@Validated @RequestBody SysRole sysRole){
        boolean add = sysRoleService.insertRole(sysRole);
        if (add){
            return Result.succ("添加成功！");
        }else {
            return Result.fail("添加失败");
        }
    }

    @PutMapping
    @Log(title = "角色管理", businessType = "修改角色")
    @PreAuthorize("hasAnyAuthority('sys:role:update')")
    public Result edit(@Validated @RequestBody SysRole sysRole){
        boolean update = sysRoleService.updateById(sysRole);
        if (update){
            sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("role_id", sysRole.getId()));
            List<Long> menuIds = sysRole.getMenuIds();
            List<SysRoleMenu> roleMenus = new ArrayList<>();
            for (Long menuId : menuIds) {
                SysRoleMenu roleMenu = new SysRoleMenu();
                roleMenu.setRoleId(sysRole.getId());
                roleMenu.setMenuId(menuId);
                roleMenus.add(roleMenu);
            }
            sysRoleMenuService.saveBatch(roleMenus);
            sysUserService.clearUserAuthorityByRoleId(sysRole.getId());
            return Result.succ("修改成功！");
        }else {
            return Result.fail("修改失败！");
        }
    }

    @DeleteMapping
    @Log(title = "角色管理", businessType = "删除角色")
    @PreAuthorize("hasAnyAuthority('sys:role:delete')")
    public Result delete(Long[] ids){
        boolean remove = sysRoleService.removeByIds(Arrays.asList(ids));
        if (remove){
            for (Long id : ids) {
                sysUserService.clearUserAuthorityByRoleId(id);
            }
            sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("role_id", ids));
            sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().in("role_id", ids));
            return Result.succ("删除成功！");
        }else {
            return Result.fail("删除失败！");
        }
    }
}

