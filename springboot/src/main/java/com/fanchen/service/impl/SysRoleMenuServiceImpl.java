package com.fanchen.service.impl;

import com.fanchen.entity.SysRoleMenu;
import com.fanchen.mapper.SysRoleMenuMapper;
import com.fanchen.service.SysRoleMenuService;
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
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

}
