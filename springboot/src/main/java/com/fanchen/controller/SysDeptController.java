package com.fanchen.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fanchen.annotation.Log;
import com.fanchen.common.lang.Result;
import com.fanchen.entity.SysDept;
import com.fanchen.entity.SysUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 部门表 前端控制器
 * </p>
 *
 * @author fanchen
 * @since 2021-11-29
 */
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController extends BaseController<SysDept> {

    @GetMapping("/list/{flag}")
    public Result list(@PathVariable boolean flag){
        List<SysDept> deptList = sysDeptService.getAllDept(flag);
        return Result.succ(deptList);
    }

    @PostMapping
    @Log(title = "部门管理", businessType = "添加部门")
    @PreAuthorize("hasAnyAuthority('sys:dept:save')")
    public Result save(@Validated @RequestBody SysDept sysDept){
        boolean save = sysDeptService.save(sysDept);
        if (save){
            return Result.succ("添加成功！");
        }else {
            return Result.fail("添加失败！");
        }
    }

    @PutMapping
    @Log(title = "部门管理", businessType = "修改部门")
    @PreAuthorize("hasAnyAuthority('sys:dept:update')")
    public Result update(@Validated @RequestBody SysDept sysDept){
        boolean update = sysDeptService.updateById(sysDept);
        if (update){
            return Result.succ("修改成功！");
        }else {
            return Result.fail("修改失败！");
        }
    }

    @DeleteMapping("/{id}")
    @Log(title = "部门管理", businessType = "删除部门")
    @PreAuthorize("hasAnyAuthority('sys:dept:delete')")
    public Result delete(@PathVariable Long id){
        boolean remove = sysDeptService.removeById(id);
        if (remove){
            List<SysUser> userList = sysUserService.list(new QueryWrapper<SysUser>().eq("dept_id", id));
            if (userList.size() > 0){
                userList.forEach(user -> user.setDeptId(100L));
                sysUserService.saveBatch(userList);
            }
            return Result.succ("删除成功！");
        }else {
            return Result.fail("删除失败！");
        }
    }

}

