package com.fanchen;

import com.fanchen.common.lang.Const;
import com.fanchen.entity.SysUser;
import com.fanchen.service.SysUserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class SystemAdminJavaApplicationTests {

    @Resource
    private SysUserService sysUserService;
    @Resource
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void contextLoads() {
        List<SysUser> userList = sysUserService.list();
        for (SysUser user : userList) {
            System.out.println(user);
        }
    }

    @Test
    void pass(){
        System.out.println(bCryptPasswordEncoder.encode("admin"));
    }

}
