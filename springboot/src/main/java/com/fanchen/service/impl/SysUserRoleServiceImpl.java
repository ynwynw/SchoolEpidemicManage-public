package com.fanchen.service.impl;

import com.fanchen.entity.SysUserRole;
import com.fanchen.mapper.SysUserRoleMapper;
import com.fanchen.service.SysUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fanchen
 * @since 2021-12-01
 */
@Service
@Transactional
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

}
