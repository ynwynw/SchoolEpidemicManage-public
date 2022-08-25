package com.fanchen.service;

import com.fanchen.entity.AccessReturn;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 未归用户表 服务类
 * </p>
 *
 * @author fanchen
 * @since 2021-12-17
 */
public interface AccessReturnService extends IService<AccessReturn> {

    void deleteByName(String name);
}
