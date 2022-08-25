package com.fanchen.mapper;

import com.fanchen.entity.HealthClock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 打卡健康表 Mapper 接口
 * </p>
 *
 * @author fanchen
 * @since 2021-12-15
 */
public interface HealthClockMapper extends BaseMapper<HealthClock> {

    int checkClockToday(String name);
}
