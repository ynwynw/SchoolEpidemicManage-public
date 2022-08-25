export default {
    state: {
        isCollapse: false,
        currentMenu: {
            path: '/',
            title: '首页',
            name: 'home',
            parentTitle: "首页"
        },
        tagsList: [
            {
                path: '/',
                title: '首页',
                name: 'index',
                parentTitle: "首页"
            }
        ]
    },
    mutations: {
        collapseMenus(state) {
            state.isCollapse = !state.isCollapse;
        },
        closeTag(state, val){
            let result =  state.tagsList.findIndex(item => item.name === val.name)
            state.tagsList.splice(result, 1)
            state.currentMenu = state.tagsList[state.tagsList.length-1]
            localStorage.setItem('currentTag', JSON.stringify(state.currentMenu))
        },
        addTag(state, val){
            state.currentMenu = val
            let result =  state.tagsList.findIndex(item => item.name === val.name)
            if (result === -1){
                state.tagsList.push(val)
            }
            localStorage.setItem('currentTag', JSON.stringify(state.currentMenu))
        },
        home(state){
            state.currentMenu = {
                path: '/',
                title: '首页',
                name: 'index',
                parentTitle: "首页"
            }
            localStorage.setItem('currentTag', JSON.stringify(state.currentMenu))
        },
        selectTag(state, val){
            let result =  state.tagsList.findIndex(item => item.name === val.name)
            val.parentTitle = state.tagsList[result].parentTitle
            // val.label !== '首页' ? val.title = val.label : ''
            state.currentMenu = val
            localStorage.setItem('currentTag', JSON.stringify(state.currentMenu))
        },
        initTag(state){
            state.tagsList = [
                {
                    path: '/',
                    title: '首页',
                    name: 'index',
                    parentTitle: "首页"
                }
            ]
            state.currentMenu = {
                path: '/',
                title: '首页',
                name: 'index',
                parentTitle: "首页"
            }
        }
    }
}
