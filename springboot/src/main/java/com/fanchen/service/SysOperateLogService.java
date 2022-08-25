package com.fanchen.service;

import com.fanchen.entity.SysOperateLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 操作日志记录 服务类
 * </p>
 *
 * @author fanchen
 * @since 2021-12-05
 */
public interface SysOperateLogService extends IService<SysOperateLog> {

    int clearAll();
}
