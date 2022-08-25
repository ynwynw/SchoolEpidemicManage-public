package com.fanchen.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanchen.annotation.Log;
import com.fanchen.common.lang.Result;
import com.fanchen.entity.GoodType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 物资类型表 前端控制器
 * </p>
 *
 * @author fanchen
 * @since 2021-12-09
 */
@RestController
@RequestMapping("/good/type")
public class GoodTypeController extends BaseController<GoodType> {

    @GetMapping
    @PreAuthorize("hasAnyAuthority('good:type:list')")
    public Result simpleList(){
        QueryWrapper<GoodType> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);
        wrapper.select("id", "type");
        wrapper.orderByAsc("order_num");
        List<GoodType> list = goodTypeService.list(wrapper);
        return Result.succ(list);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('good:type:list')")
    public Result list(String type, String createBy, Integer status) {
        QueryWrapper<GoodType> wrapper = new QueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(type), "type", type);
        wrapper.like(StrUtil.isNotBlank(createBy), "create_by", createBy);
        wrapper.eq(status != null, "status", status);
        wrapper.orderByAsc("order_num");
        Page<GoodType> page = goodTypeService.page(getPage(), wrapper);
        return Result.succ(page);
    }

    @PostMapping
    @Log(title = "物资类型", businessType = "添加类型")
    @PreAuthorize("hasAnyAuthority('good:type:save')")
    public Result save(@Validated @RequestBody GoodType goodType, Principal principal) {
        goodType.setCreateBy(principal.getName());
        boolean save = goodTypeService.save(goodType);
        if (save) {
            return Result.succ("添加类型成功！");
        } else {
            return Result.fail("添加类型失败！");
        }
    }

    @PutMapping
    @Log(title = "物资类型", businessType = "更新类型")
    @PreAuthorize("hasAnyAuthority('good:type:update')")
    public Result update(@Validated @RequestBody GoodType goodType, Principal principal) {
        goodType.setCreateBy(principal.getName());
        boolean update = goodTypeService.updateById(goodType);
        if (update) {
            return Result.succ("修改类型成功！");
        } else {
            return Result.fail("修改类型失败！");
        }
    }

    @DeleteMapping
    @Log(title = "物资类型", businessType = "删除类型")
    @PreAuthorize("hasAnyAuthority('good:type:delete')")
    public Result delete(Long[] ids) {
        boolean remove = goodTypeService.removeByIds(Arrays.asList(ids));
        if (remove) {
            return Result.succ("删除类型成功！");
        } else {
            return Result.fail("删除类型失败！");
        }
    }

}

