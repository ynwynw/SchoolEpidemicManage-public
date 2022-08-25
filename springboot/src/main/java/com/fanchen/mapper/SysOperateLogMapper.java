package com.fanchen.mapper;

import com.fanchen.entity.SysOperateLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 操作日志记录 Mapper 接口
 * </p>
 *
 * @author fanchen
 * @since 2021-12-05
 */
public interface SysOperateLogMapper extends BaseMapper<SysOperateLog> {

    int clearAll();
}
