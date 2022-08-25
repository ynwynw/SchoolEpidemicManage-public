package com.fanchen.service;

import com.fanchen.entity.HealthClock;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 打卡健康表 服务类
 * </p>
 *
 * @author fanchen
 * @since 2021-12-15
 */
public interface HealthClockService extends IService<HealthClock> {

    int checkClockToday(String name);
}
