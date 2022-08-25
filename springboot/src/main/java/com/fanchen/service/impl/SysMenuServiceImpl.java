package com.fanchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fanchen.common.dto.NavMenu;
import com.fanchen.entity.SysMenu;
import com.fanchen.entity.SysUser;
import com.fanchen.mapper.SysMenuMapper;
import com.fanchen.mapper.SysUserMapper;
import com.fanchen.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fanchen.service.SysUserService;
import com.fanchen.utils.RedisUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fanchen
 * @since 2021-11-29
 */
@Service
@Transactional
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public List<NavMenu> getCurrentUserNav() {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        SysUser sysUser = (SysUser) redisUtil.get("User:" + username);
        List<Long> menuIds = sysUserMapper.getMenuIds(sysUser.getId());
        List<SysMenu> menus = listByIds(menuIds);
        //转换为树状结构
        List<SysMenu> treeMenu = buildTreeMenu(menus);
        //pojo转为dto
        return convert(treeMenu);
    }

    @Override
    public List<SysMenu> getCurrentUserMenu() {
        List<SysMenu> menus = list(new QueryWrapper<SysMenu>().orderByAsc("order_num"));
        //转换为树状结构
        return buildTreeMenu(menus);
    }

    private List<SysMenu> buildTreeMenu(List<SysMenu> menus) {
        List<SysMenu> finalMenus = new ArrayList<>();
        for (SysMenu menu : menus) {
            for (SysMenu sysMenu : menus) {
                if (menu.getId().equals(sysMenu.getParentId())){
                    menu.getChildren().add(sysMenu);
                }
            }
            if (menu.getParentId() == 0L){
                finalMenus.add(menu);
            }
        }
        return finalMenus;
    }

    private List<NavMenu> convert(List<SysMenu> menuTree) {
        List<NavMenu> navMenus = new ArrayList<>();
        menuTree.forEach(
                menu -> {
                    if (menu.getStatus() != 0){
                        NavMenu navMenu = new NavMenu();
                        navMenu.setId(menu.getId());
                        navMenu.setTitle(menu.getName());
                        navMenu.setName(menu.getPerms());
                        navMenu.setComponent(menu.getComponent());
                        navMenu.setPath(menu.getPath());
                        navMenu.setIcon(menu.getIcon());
                        navMenu.setOrderNum(menu.getOrderNum());
                        if (menu.getChildren().size() > 0){
                            navMenu.setChildren(convert(menu.getChildren()));
                        }
                        navMenus.add(navMenu);
                    }
                }
        );
        navMenus.sort(Comparator.comparing(NavMenu::getOrderNum));
        navMenus.forEach(nav -> {
            if (nav.getChildren().size() > 1){
                nav.getChildren().sort(Comparator.comparing(NavMenu::getOrderNum));
            }
        });
        return navMenus;
    }
}
