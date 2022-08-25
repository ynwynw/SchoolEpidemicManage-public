package com.fanchen.service.impl;

import com.fanchen.entity.SysOperateLog;
import com.fanchen.mapper.SysOperateLogMapper;
import com.fanchen.service.SysOperateLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 操作日志记录 服务实现类
 * </p>
 *
 * @author fanchen
 * @since 2021-12-05
 */
@Service
public class SysOperateLogServiceImpl extends ServiceImpl<SysOperateLogMapper, SysOperateLog> implements SysOperateLogService {

    @Resource
    private SysOperateLogMapper sysOperateLogMapper;

    @Override
    public int clearAll() {
        return sysOperateLogMapper.clearAll();
    }
}
