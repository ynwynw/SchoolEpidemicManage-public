const Mock = require('mockjs')

const Random = Mock.Random

let Result = {
    code: 200,
    msg: '操作成功',
    data: null
}

Mock.mock('/captcha', 'get', () => {
    Result.data = {
        key: Random.string(32), // 获取一个32位的随机字符串,
        base64: Random.dataImage("120x40", "3m12d") //生成验证码图片编码
    }
    return Result;
})

Mock.mock('/login', 'post', () => {
    Result.data = null;
    Result.code = 200;
    return Result;
})

Mock.mock('/sys/userInfo', 'get', () => {
    Result.data = {
        id: "1",
        username: "admin",
        avatar: "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
    }
    return Result;
})

Mock.mock('/logout', 'post', () => {
    Result.data = null;
    return Result;
})

Mock.mock('/sys/menu/nav', 'get', () => {
    let nav = [{
        "id": 1,
        "title": "系统管理",
        "icon": "el-icon-s-operation",
        "path": "",
        "name": "sys:manage",
        "component": "",
        "children": [{
            "id": 2,
            "title": "用户管理",
            "icon": "el-icon-s-custom",
            "path": "/sys/users",
            "name": "sys:user:list",
            "component": "sys/User",
            "children": []
        }, {
            "id": 3,
            "title": "角色管理",
            "icon": "el-icon-rank",
            "path": "/sys/roles",
            "name": "sys:role:list",
            "component": "sys/Role",
            "children": []
        }, {
            "id": 4,
            "title": "菜单管理",
            "icon": "el-icon-menu",
            "path": "/sys/menus",
            "name": "sys:menu:list",
            "component": "sys/Menu",
            "children": []
        }]
    }, {
        "id": 5,
        "title": "系统工具",
        "icon": "el-icon-s-tools",
        "path": "",
        "name": "sys:tools",
        "component": null,
        "children": [{
            "id": 6,
            "title": "数字字典",
            "icon": "el-icon-s-order",
            "path": "/sys/dicts",
            "name": "sys:dict:list",
            "component": "sys/Dict",
            "children": []
        }]
    }];
    let authoritys = ['SysUser', "SysUser:save"];
    Result.data = {
        nav: nav,
        authoritys: authoritys
    }
    return Result;
})

Mock.mock('/sys/menu/list', 'get', () => {
    Result.data = [{
        "id": 1,
        "created": "2021-01-15T18:58:18",
        "updated": "2021-01-15T18:58:20",
        "statu": 1,
        "parentId": 0,
        "name": "系统管理",
        "path": "system",
        "perms": "sys:manage",
        "component": "",
        "type": 0,
        "icon": "el-icon-s-operation",
        "orderNum": 1,
        "children": [{
            "id": 2,
            "created": "2021-01-15T19:03:45",
            "updated": "2021-01-15T19:03:48",
            "statu": 1,
            "parentId": 1,
            "name": "用户管理",
            "path": "/sys/users",
            "perms": "sys:user:list",
            "component": "sys/User",
            "type": 1,
            "icon": "el-icon-s-custom",
            "orderNum": 1,
            "children": [{
                "id": 9,
                "created": "2021-01-17T21:48:32",
                "updated": null,
                "statu": 1,
                "parentId": 2,
                "name": "添加用户",
                "path": null,
                "perms": "sys:user:save",
                "component": null,
                "type": 2,
                "icon": null,
                "orderNum": 1,
                "children": []
            }, {
                "id": 10,
                "created": "2021-01-17T21:49:03",
                "updated": "2021-01-17T21:53:04",
                "statu": 1,
                "parentId": 2,
                "name": "修改用户",
                "path": null,
                "perms": "sys:user:update",
                "component": null,
                "type": 2,
                "icon": null,
                "orderNum": 2,
                "children": []
            }, {
                "id": 11,
                "created": "2021-01-17T21:49:21",
                "updated": null,
                "statu": 1,
                "parentId": 2,
                "name": "删除用户",
                "path": null,
                "perms": "sys:user:delete",
                "component": null,
                "type": 2,
                "icon": null,
                "orderNum": 3,
                "children": []
            }, {
                "id": 12,
                "created": "2021-01-17T21:49:58",
                "updated": null,
                "statu": 1,
                "parentId": 2,
                "name": "分配角色",
                "path": null,
                "perms": "sys:user:role",
                "component": null,
                "type": 2,
                "icon": null,
                "orderNum": 4,
                "children": []
            }, {
                "id": 13,
                "created": "2021-01-17T21:50:36",
                "updated": null,
                "statu": 1,
                "parentId": 2,
                "name": "重置密码",
                "path": null,
                "perms": "sys:user:repass",
                "component": null,
                "type": 2,
                "icon": null,
                "orderNum": 5,
                "children": []
            }]
        }, {
            "id": 3,
            "created": "2021-01-15T19:03:45",
            "updated": "2021-01-15T19:03:48",
            "statu": 1,
            "parentId": 1,
            "name": "角色管理",
            "path": "/sys/roles",
            "perms": "sys:role:list",
            "component": "sys/Role",
            "type": 1,
            "icon": "el-icon-rank",
            "orderNum": 2,
            "children": [{
                "id": 7,
                "created": "2021-01-15T23:02:25",
                "updated": "2021-01-17T21:53:14",
                "statu": 1,
                "parentId": 3,
                "name": "添加角色",
                "path": "",
                "perms": "sys:role:save",
                "component": "",
                "type": 2,
                "icon": "",
                "orderNum": 1,
                "children": []
            }, {
                "id": 14,
                "created": "2021-01-17T21:51:14",
                "updated": null,
                "statu": 1,
                "parentId": 3,
                "name": "修改角色",
                "path": null,
                "perms": "sys:role:update",
                "component": null,
                "type": 2,
                "icon": null,
                "orderNum": 2,
                "children": []
            }, {
                "id": 15,
                "created": "2021-01-17T21:51:39",
                "updated": null,
                "statu": 1,
                "parentId": 3,
                "name": "删除角色",
                "path": null,
                "perms": "sys:role:delete",
                "component": null,
                "type": 2,
                "icon": null,
                "orderNum": 3,
                "children": []
            }, {
                "id": 16,
                "created": "2021-01-17T21:52:02",
                "updated": null,
                "statu": 1,
                "parentId": 3,
                "name": "分配权限",
                "path": null,
                "perms": "sys:role:perm",
                "component": null,
                "type": 2,
                "icon": null,
                "orderNum": 5,
                "children": []
            }]
        }, {
            "id": 4,
            "created": "2021-01-15T19:03:45",
            "updated": "2021-01-15T19:03:48",
            "statu": 1,
            "parentId": 1,
            "name": "菜单管理",
            "path": "/sys/menus",
            "perms": "sys:menu:list",
            "component": "sys/Menu",
            "type": 1,
            "icon": "el-icon-menu",
            "orderNum": 3,
            "children": [{
                "id": 17,
                "created": "2021-01-17T21:53:53",
                "updated": "2021-01-17T21:55:28",
                "statu": 1,
                "parentId": 4,
                "name": "添加菜单",
                "path": null,
                "perms": "sys:menu:save",
                "component": null,
                "type": 2,
                "icon": null,
                "orderNum": 1,
                "children": []
            }, {
                "id": 18,
                "created": "2021-01-17T21:56:12",
                "updated": null,
                "statu": 1,
                "parentId": 4,
                "name": "修改菜单",
                "path": null,
                "perms": "sys:menu:update",
                "component": null,
                "type": 2,
                "icon": null,
                "orderNum": 2,
                "children": []
            }, {
                "id": 19,
                "created": "2021-01-17T21:56:36",
                "updated": null,
                "statu": 1,
                "parentId": 4,
                "name": "删除菜单",
                "path": null,
                "perms": "sys:menu:delete",
                "component": null,
                "type": 2,
                "icon": null,
                "orderNum": 3,
                "children": []
            }]
        }]
    }, {
        "id": 5,
        "created": "2021-01-15T19:06:11",
        "updated": null,
        "statu": 1,
        "parentId": 0,
        "name": "系统工具",
        "path": "tool",
        "perms": "sys:tools",
        "component": null,
        "type": 0,
        "icon": "el-icon-s-tools",
        "orderNum": 2,
        "children": [{
            "id": 6,
            "created": "2021-01-15T19:07:18",
            "updated": "2021-01-18T16:32:13",
            "statu": 1,
            "parentId": 5,
            "name": "数字字典",
            "path": "/sys/dicts",
            "perms": "sys:dict:list",
            "component": "sys/Dict",
            "type": 1,
            "icon": "el-icon-s-order",
            "orderNum": 1,
            "children": []
        }]
    }]
    return Result
})

Mock.mock('/sys/role/list', 'get', () => {
    Result.data = {
        "records": [
            {
                "id": 1,
                "created": "2021-01-16T13:29:03",
                "updated": "2021-01-17T15:50:45",
                "statu": 1,
                "name": "超级管理员",
                "code": "admin",
                "remark": "系统默认最高权限，不可以编辑和任意修改",
                "menuIds": []
            }, {
                "id": 2,
                "created": "2021-01-04T10:09:14",
                "updated": "2021-01-30T08:19:52",
                "statu": 1,
                "name": "普通用户",
                "code": "normal",
                "remark": "只有基本查看功能",
                "menuIds": [1, 2, 3, 4, 5, 6]
            }],
        "total": 2,
        "size": 10,
        "current": 1,
        "orders": [],
        "optimizeCountSql": true,
        "hitCount": false,
        "countId": null,
        "maxLimit": null,
        "searchCount": true,
        "pages": 1
    };
    return Result;
})

Mock.mock('/sys/role/info/2', 'get', () => {
    Result.data = {
        "id": 3,
        "created": "2021-01-04T10:09:14",
        "updated": "2021-01-30T08:19:52",
        "statu": 1,
        "name": "普通用户",
        "code": "normal",
        "remark": "只有基本查看功能",
        "menuIds": [1, 2, 3, 4, 5, 6]
    }
    return Result
})

Mock.mock('/system/dept/list', 'get', () => {
    Result.data = [{
        "id": 100,
        "label": "若依科技",
        "children": [{
            "id": 101,
            "label": "深圳总公司",
            "children": [{"id": 103, "label": "研发部门"}, {"id": 104, "label": "市场部门"}, {
                "id": 105,
                "label": "测试部门"
            }, {"id": 106, "label": "财务部门"}, {"id": 107, "label": "运维部门"}]
        }, {"id": 102, "label": "长沙分公司", "children": [{"id": 108, "label": "市场部门"}, {"id": 109, "label": "财务部门"}]}]
    }]
    return Result
})

Mock.mock('/system/user/list', 'get', () => {
    Result.data = {
        "total": 2,
        "rows": [{
            "searchValue": null,
            "createBy": "admin",
            "createTime": "2021-09-09 17:25:28",
            "updateBy": null,
            "updateTime": null,
            "remark": "管理员",
            "params": {},
            "userId": 1,
            "deptId": 103,
            "userName": "admin",
            "nickName": "若依",
            "email": "ry@163.com",
            "phoneNumber": "15888888888",
            "sex": 1,
            "avatar": "",
            "status": 1,
            "delFlag": "0",
            "loginIp": "36.41.86.133",
            "loginDate": "2021-11-28T18:54:26.000+0800",
            "dept": {
                "searchValue": null,
                "createBy": null,
                "createTime": null,
                "updateBy": null,
                "updateTime": null,
                "remark": null,
                "params": {},
                "deptId": 103,
                "parentId": null,
                "ancestors": null,
                "deptName": "研发部门",
                "orderNum": null,
                "leader": "若依",
                "phone": null,
                "email": null,
                "status": null,
                "delFlag": null,
                "parentName": null,
                "children": []
            },
            "roles": [],
            "roleIds": null,
            "postIds": null,
            "roleId": null,
            "admin": true
        }, {
            "searchValue": null,
            "createBy": "admin",
            "createTime": "2021-09-09 17:25:29",
            "updateBy": null,
            "updateTime": null,
            "remark": "测试员",
            "params": {},
            "userId": 2,
            "deptId": 105,
            "userName": "ry",
            "nickName": "若依",
            "email": "ry@qq.com",
            "phoneNumber": "15666666666",
            "sex": 1,
            "avatar": "",
            "status": 1,
            "delFlag": "0",
            "loginIp": "39.174.128.84",
            "loginDate": "2021-11-27T22:35:12.000+0800",
            "dept": {
                "searchValue": null,
                "createBy": null,
                "createTime": null,
                "updateBy": null,
                "updateTime": null,
                "remark": null,
                "params": {},
                "deptId": 105,
                "parentId": null,
                "ancestors": null,
                "deptName": "测试部门",
                "orderNum": null,
                "leader": "若依",
                "phone": null,
                "email": null,
                "status": null,
                "delFlag": null,
                "parentName": null,
                "children": []
            },
            "roles": [],
            "roleIds": null,
            "postIds": null,
            "roleId": null,
            "admin": false
        }]
    }
    return Result
})

Mock.mock('/sys/user/info/2', 'get', () => {
    Result.data = {
        "id": 2,
        "created": "2021-01-30T08:20:22",
        "updated": "2021-01-30T08:55:57",
        "statu": 1,
        "username": "test",
        "password": "$2a$10$0ilP4ZD1kLugYwLCs4pmb.ZT9cFqzOZTNaMiHxrBnVIQUGUwEvBIO",
        "avatar": "https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/5a9f48118166308daba8b6da7e466aab.jpg",
        "email": "test@qq.com",
        "city": "广州",
        "lastLogin": null,
        "roles": [{
            "id": 2,
            "created": "2021-01-04T10:09:14",
            "updated": "2021-01-30T08:19:52",
            "statu": 1,
            "label": "普通用户",
            "code": "normal",
            "remark": "只有基本查看功能",
            "menuIds": []
        }]
    }
    return Result
})
