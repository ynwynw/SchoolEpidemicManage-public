package com.fanchen.controller;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanchen.annotation.Log;
import com.fanchen.common.lang.Const;
import com.fanchen.common.lang.Result;
import com.fanchen.entity.SysNotice;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.UUID;

/**
 * <p>
 * 公告表 前端控制器
 * </p>
 *
 * @author fanchen
 * @since 2021-12-22
 */
@RestController
@RequestMapping("/sys/notice")
public class SysNoticeController extends BaseController<SysNotice> {

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('monitor:notice:list')")
    public Result list(String title, String createBy, String start, String end, Integer status){
        LambdaQueryWrapper<SysNotice> wrapper = Wrappers.lambdaQuery(SysNotice.class);
        wrapper.like(StrUtil.isNotBlank(title), SysNotice::getTitle, title);
        wrapper.like(StrUtil.isNotBlank(createBy), SysNotice::getCreateBy, createBy);
        wrapper.eq(status != null, SysNotice::getStatus, status);
        if (StrUtil.isNotBlank(start) && StrUtil.isNotBlank(end)){
            DateTime a = DateUtil.parse(start + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
            DateTime b = DateUtil.parse(end + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
            wrapper.between(SysNotice::getCreateTime, a, b);
        }
        wrapper.orderByDesc(SysNotice::getCreateTime);
        Page<SysNotice> page = sysNoticeService.page(getPage(), wrapper);
        return Result.succ(page);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('monitor:notice:save')")
    @Log(title = "公告管理", businessType = "添加公告")
    public Result save(@Validated @RequestBody SysNotice sysNotice, Principal principal){
        sysNotice.setCreateBy(principal.getName());
        boolean save = sysNoticeService.save(sysNotice);
        if (save){
            return Result.succ("新增公告成功");
        }else {
            return Result.fail("新增公告失败");
        }
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('monitor:notice:update')")
    @Log(title = "公告管理", businessType = "更新公告")
    public Result update(@Validated @RequestBody SysNotice sysNotice, Principal principal){
        sysNotice.setUpdateBy(principal.getName());
        boolean update = sysNoticeService.updateById(sysNotice);
        if (update){
            return Result.succ("更新公告成功！");
        }else {
            return Result.fail("更新公告失败！");
        }
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('monitor:notice:delete')")
    @Log(title = "公告管理", businessType = "删除公告")
    public Result delete(Long[] ids){
        boolean remove = sysNoticeService.removeByIds(Arrays.asList(ids));
        if (remove){
            return Result.succ("删除成功！");
        }else {
            return Result.fail("删除失败！");
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('monitor:notice:set')")
    @Log(title = "公告管理", businessType = "设置公告")
    public Result notice(@PathVariable("id") Long id){
        SysNotice sysNotice = sysNoticeService.getById(id);
        sysNotice.setNoticeId(UUID.randomUUID().toString());
        redisUtil.set(Const.NOTICE_KEY, sysNotice, 86400);
        return Result.succ("公告设置成功！");
    }

    @GetMapping
    public Result getNotice(){
        if (redisUtil.hasKey(Const.NOTICE_KEY)){
            SysNotice sysNotice = (SysNotice) redisUtil.get(Const.NOTICE_KEY);
            return Result.succ(sysNotice);
        }
        return Result.succ("暂无公告");
    }

}

