package com.fanchen.mapper;

import com.fanchen.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fanchen
 * @since 2021-11-29
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    int insertRole(SysRole sysRole);
}
