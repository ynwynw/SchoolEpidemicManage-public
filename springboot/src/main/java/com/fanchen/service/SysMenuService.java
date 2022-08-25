package com.fanchen.service;

import com.fanchen.common.dto.NavMenu;
import com.fanchen.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fanchen
 * @since 2021-11-29
 */
public interface SysMenuService extends IService<SysMenu> {

    List<NavMenu> getCurrentUserNav();

    List<SysMenu> getCurrentUserMenu();
}
