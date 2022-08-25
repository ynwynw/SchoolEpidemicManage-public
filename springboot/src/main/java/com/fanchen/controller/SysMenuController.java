package com.fanchen.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fanchen.annotation.Log;
import com.fanchen.common.dto.NavMenu;
import com.fanchen.common.lang.Result;
import com.fanchen.entity.SysMenu;
import com.fanchen.entity.SysRoleMenu;
import com.fanchen.entity.SysUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
@RequestMapping("/sys/menu")
public class SysMenuController extends BaseController<SysMenu> {

    @GetMapping("/nav")
    public Result nav(Principal principal){
        SysUser sysUser = (SysUser) redisUtil.get("User:" + principal.getName());
        //获取权限信息
        String authority = sysUserService.getUserAuthority(sysUser.getUsername());
        String[] authorityArray = StringUtils.tokenizeToStringArray(authority, ",");
        //获取导航栏信息
        List<NavMenu> nav = sysMenuService.getCurrentUserNav();
        return Result.succ(MapUtil.builder().put("authoritys", authorityArray).put("nav", nav).map());
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('sys:menu:list')")
    public Result list(){
        List<SysMenu> menuList = sysMenuService.getCurrentUserMenu();
        return Result.succ(menuList);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('sys:menu:save')")
    @Log(title = "菜单管理", businessType = "添加菜单")
    public Result add(@Validated @RequestBody SysMenu sysMenu){
        boolean save = sysMenuService.save(sysMenu);
        if (save){
            return Result.succ("添加成功！");
        }else {
            return Result.fail("添加失败！");
        }
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('sys:menu:update')")
    @Log(title = "菜单管理", businessType = "修改菜单")
    public Result edit(@Validated @RequestBody SysMenu sysMenu){
        boolean update = sysMenuService.updateById(sysMenu);
        if (update){
            sysUserService.clearUserAuthorityByMenuId(sysMenu.getId());
            return Result.succ("修改成功！");
        }else {
            return Result.fail("修改失败！");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('sys:menu:delete')")
    @Log(title = "菜单管理", businessType = "删除菜单")
    public Result delete(@PathVariable Long id){
        if (sysMenuService.count(new QueryWrapper<SysMenu>().eq("parent_id", id)) > 0){
            return Result.fail("请先删除下级数据！");
        }
        boolean remove = sysMenuService.removeById(id);
        if (remove){
            sysUserService.clearUserAuthorityByMenuId(id);
            sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("menu_id", id));
            return Result.succ("删除成功！");
        }else {
            return Result.fail("删除失败！");
        }
    }

}

