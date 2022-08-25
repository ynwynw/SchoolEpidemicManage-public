package com.fanchen.controller;

import com.fanchen.common.lang.Result;
import com.fanchen.entity.SysDept;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author fanchen
 * @date 2021/12/12
 * @time 19:34
 */

@RestController
public class RegisterController extends BaseController {

    @PostMapping("/register")
    public Result register(String username, String password, Integer roleType, String registerCode,
    String phoneNumber, Long deptId){
        //注册用户
        boolean flag = sysUserService.registerUser(username, password, roleType, registerCode, deptId, phoneNumber);
        if (flag){
            return Result.succ("注册成功！");
        }
        return Result.fail("注册码有误！");
    }

    @GetMapping("/register/deptList")
    public Result deptList(){
        //查询所有的部门并返回
        List<SysDept> deptList = sysDeptService.getAllDept(true);
        return Result.succ(deptList);
    }

}
