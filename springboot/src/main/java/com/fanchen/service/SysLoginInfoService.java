package com.fanchen.service;

import com.fanchen.entity.SysLoginInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统访问记录 服务类
 * </p>
 *
 * @author fanchen
 * @since 2021-12-04
 */
public interface SysLoginInfoService extends IService<SysLoginInfo> {

    int clearAllInfo();
}
