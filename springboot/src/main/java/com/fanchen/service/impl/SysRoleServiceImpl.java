package com.fanchen.service.impl;

import com.fanchen.entity.SysRole;
import com.fanchen.entity.SysRoleMenu;
import com.fanchen.mapper.SysRoleMapper;
import com.fanchen.service.SysRoleMenuService;
import com.fanchen.service.SysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
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
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    @Override
    public boolean insertRole(SysRole sysRole) {
        sysRoleMapper.insertRole(sysRole);
        Long roleId = sysRole.getId();
        List<SysRoleMenu> list = new ArrayList<>();
        for (Long menuId : sysRole.getMenuIds()) {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenu.setRoleId(roleId);
            list.add(sysRoleMenu);
        }
        return sysRoleMenuService.saveBatch(list);
    }
}
