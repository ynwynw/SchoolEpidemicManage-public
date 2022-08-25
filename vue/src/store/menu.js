export default {
    state: {
        menuList: [],
        authoritys: [],
        hasRoute: false,
        menus: []
    },
    mutations: {
        setMenuList(state, menuList) {
            state.menuList = menuList
            let temp = []
            menuList.forEach(menu => {
                let parentTitle = menu.title
                if (menu.children !== null || menu.children !== ''){
                    menu.children.forEach(child => {
                        if (child.path !== '' && child.path !== null){
                            temp.push({
                                name: child.name,
                                title: child.title,
                                path: child.path,
                                parentTitle: parentTitle
                            })
                        }
                    })
                }
            })
            state.menus = temp
        },
        setAuthoritys(state, authoritys) {
            state.authoritys = authoritys
        },
        changeRouteStatus(state, hasRoute) {
            state.hasRoute = hasRoute
        }
    }
}
