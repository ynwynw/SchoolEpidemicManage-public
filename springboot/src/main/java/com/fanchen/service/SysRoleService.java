package com.fanchen.service;

import com.fanchen.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fanchen
 * @since 2021-11-29
 */
public interface SysRoleService extends IService<SysRole> {

    boolean insertRole(SysRole sysRole);
}
