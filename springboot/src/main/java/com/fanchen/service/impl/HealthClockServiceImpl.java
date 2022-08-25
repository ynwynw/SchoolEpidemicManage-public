package com.fanchen.service.impl;

import com.fanchen.entity.HealthClock;
import com.fanchen.mapper.HealthClockMapper;
import com.fanchen.service.HealthClockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 打卡健康表 服务实现类
 * </p>
 *
 * @author fanchen
 * @since 2021-12-15
 */
@Service
public class HealthClockServiceImpl extends ServiceImpl<HealthClockMapper, HealthClock> implements HealthClockService {

    @Resource
    private HealthClockMapper healthClockMapper;

    @Override
    public int checkClockToday(String name) {
        return healthClockMapper.checkClockToday(name);
    }

}
