package com.fanchen.mapper;

import com.fanchen.entity.SysLoginInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 系统访问记录 Mapper 接口
 * </p>
 *
 * @author fanchen
 * @since 2021-12-04
 */
public interface SysLoginInfoMapper extends BaseMapper<SysLoginInfo> {

    int clearAllData();
}
