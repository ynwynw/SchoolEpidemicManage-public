import Vue from 'vue'
import VueRouter from 'vue-router'
import Router from 'vue-router'
import axios from 'axios'
import store from '../store'
import NotFound from '../views/error/404'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'


// 进度条配置项这样写
NProgress.configure({
    easing: 'ease',  // 动画方式
    speed: 500,  // 递增进度条的速度
    showSpinner: false, // 是否显示加载ico
    trickleSpeed: 200, // 自动递增间隔
    minimum: 0.3 // 初始化时的最小百分比
})

const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}
Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'main',
        component: () => import('../views/Home.vue'),
        children: [
            {
                path: '/',
                name: 'index',
                component: () => import('../views/Index/Index.vue')
            },
            {
                path: '/userCenter',
                name: 'userCenter',
                component: () => import('../views/UserCenter')
            },
            // {
            //   path: '/sys/users',
            //   name: 'sys:user:list',
            //   component: () => import('../views/sys/User')
            // },
            // {
            //   path: '/sys/roles',
            //   name: 'sys:role:list',
            //   component: () => import('../views/sys/Role')
            // },
            // {
            //   path: '/sys/menus',
            //   name: 'sys:menu:list',
            //   component: () => import('../views/sys/Menu')
            // },
            // {
            //   path: '/sys/dicts',
            //   name: 'sys:dict:list',
            //   component: () => import('../views/sys/Dict')
            // }
        ]
    },
    {
        path: '/login',
        name: 'login',
        component: () => import('../views/Login')
    },
    {
        path: '/register',
        name: 'register',
        component: () => import('../views/Register')
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

router.beforeEach((to, from, next) => {
    NProgress.start()
    // 这里为啥不把 * 匹配放到静态路由的最后面，是因为如果放置在静态路由最后面，作为一级路由，
    // 当url同前面的路由都不匹配时，会匹配到 *，这样一来，刷新页面时，由于还没加载动态路由，预期和动态路由匹配的url，
    // 会匹配到静态路由的 *，然后跳转404页面。
    if (router.options.routes[router.options.routes.length - 1].path !== "*") {
        router.options.routes = router.options.routes.concat([
            {
                path: "*",
                name: "notFound",
                component: NotFound
            }
        ]);
    }
    let token = localStorage.getItem("token")
    let hasRoute = store.state.menu.hasRoute
    if (to.path !== '/login' && to.path !== '/register') {
        if (token === undefined || token === 'undefined' || token === null) {
            next({
                path: "/login"
            })
            NProgress.done()
            return
        } else if (!hasRoute) {
            axios.get("/sys/menu/nav", {
                headers: {
                    Authorization: token
                }
            }).then(res => {
                res.config = null
                res.request = null
                //拿到menuList
                store.commit("setMenuList", res.data.data.nav);
                //拿到用户权限
                store.commit("setAuthoritys", res.data.data.authoritys);
                //动态绑定路由
                // let newRoutes = router.options.routes;
                router.options.routes[0].children = []
                res.data.data.nav.forEach(menu => {
                    if (menu.children) {
                        menu.children.forEach(e => {
                            //转换路由
                            let route = menuToRoute(e);
                            //添加到管理器中
                            if (route) {
                                router.addRoute("main", route)
                                // newRoutes[0].children.push(route)
                            }
                        })
                    }
                })
                // router.addRoutes(newRoutes)
                store.commit("changeRouteStatus", true)
                next({ ...to, replace: true })
            }).catch(()=>{
                next({
                    path: "/login"
                })
                NProgress.done()
            })
        }
    }
    next()
})

// 路由跳转后钩子函数中 - 执行进度条加载结束
router.afterEach(() => {
    NProgress.done()
});

const menuToRoute = (menu) => {
    if (!menu.component) {
        return null
    }
    // 复制属性
    let route = {
        name: menu.name,
        path: menu.path,
        meta: {
            icon: menu.icon,
            title: menu.title
        }
    }
    route.component = () => import('@/views/' + menu.component + '.vue');
    return route
}

export default router
