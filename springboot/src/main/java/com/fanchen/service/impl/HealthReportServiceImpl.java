package com.fanchen.service.impl;

import com.fanchen.entity.HealthReport;
import com.fanchen.mapper.HealthReportMapper;
import com.fanchen.service.HealthReportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 二码一报告表 服务实现类
 * </p>
 *
 * @author fanchen
 * @since 2021-12-16
 */
@Service
public class HealthReportServiceImpl extends ServiceImpl<HealthReportMapper, HealthReport> implements HealthReportService {

    @Resource
    private HealthReportMapper healthReportMapper;

    @Override
    public int checkReportToday(String name) {
        return healthReportMapper.checkReportToday(name);
    }
}
