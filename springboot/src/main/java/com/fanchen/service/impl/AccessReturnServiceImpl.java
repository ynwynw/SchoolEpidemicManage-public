package com.fanchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fanchen.entity.AccessReturn;
import com.fanchen.mapper.AccessReturnMapper;
import com.fanchen.service.AccessReturnService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 未归用户表 服务实现类
 * </p>
 *
 * @author fanchen
 * @since 2021-12-17
 */
@Service
@Transactional
public class AccessReturnServiceImpl extends ServiceImpl<AccessReturnMapper, AccessReturn> implements AccessReturnService {

    @Resource
    private AccessReturnMapper accessReturnMapper;

    @Override
    public void deleteByName(String name) {
        AccessReturn accessReturn = accessReturnMapper.selectOne(new QueryWrapper<AccessReturn>().eq("name", name));
        if (accessReturn != null){
            accessReturnMapper.deleteById(accessReturn);
        }
    }
}
