package com.fanchen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fanchen.entity.SysUser;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fanchen
 * @since 2021-11-29
 */
public interface SysUserService extends IService<SysUser> {

    SysUser getByUsername(String username);

    String getUserAuthority(String username);

    void clearUserAuthority(String username);

    void clearUserAuthorityByRoleId(Long roleId);

    void clearUserAuthorityByMenuId(Long menuId);

    boolean registerUser(String username, String password, Integer roleType, String registerCode, Long deptId, String phoneNumber);
}
