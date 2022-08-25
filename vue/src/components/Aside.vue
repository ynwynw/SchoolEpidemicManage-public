<template>
    <el-scrollbar wrap-class="scrollbar-wrapper" class="sidebar-container">
        <el-menu
                class="el-menu-vertical-demo"
                :collapse="isCollapse"
                background-color="#304156"
                text-color="#bfcbd9"
                :unique-opened="true"
                active-text-color="#409EFF"
                :collapse-transition="true"
                mode="vertical"
                :default-active="$route.name"
        >
            <div v-if="this.$store.state.side.isCollapse">
                <img style="width: 64px;height: 54px" :src="logo" />
            </div>
            <h1 style="text-align: center;color:white;" v-show="!this.$store.state.side.isCollapse">校园疫情防控系统</h1>
            <el-menu-item index="index"
                          class="index"
                          @click="home()"
            >
                <i class="el-icon-s-home"></i>
                <span slot="title" class="stitle">首页</span>
            </el-menu-item>
            <el-submenu v-for="menu in menuList" :index="menu.name" class="submenu">
                <template slot="title">
                    <i :class="menu.icon"></i>
                    <span slot="title" class="stitle">{{menu.title}}</span>
                </template>
                <el-menu-item v-for="item in menu.children"
                              :index="item.name + ''"
                              class="nest-menu"
                              @click="addTag(item, menu.title)"
                >
                    <template slot="title">
                        <i :class="item.icon"></i>
                        <span slot="title" class="stitle">{{item.title}}</span>
                    </template>
                </el-menu-item>
            </el-submenu>
        </el-menu>
    </el-scrollbar>
</template>

<script>
    import logo from '../assets/logo.png'
    export default {
        name: 'Aside',
        data() {
            return {
                logo: undefined
            }
        },
        methods: {
            addTag(item, title){
                this.$router.push(item.path)
                item.parentTitle = title
                this.$store.commit("addTag", item)
            },
            home(){
                this.$router.push("/")
                this.$store.commit("home")
            },
            isActive(name){
                return name === this.$route.name
            }
        },
        computed: {
            isCollapse() {
                return this.$store.state.side.isCollapse;
            },
            menuList: {
                get(){
                    return this.$store.state.menu.menuList;
                }
            }
        },
        created() {
            this.logo = logo
        }
    }
</script>

<style lang="scss">
    .el-menu {
        border-right: 0 !important;
    }

    .el-menu-vertical-demo {
        height: 100% !important;
    }

    .el-menu-vertical-demo:not(.el-menu--collapse) {
        width: 200px;
        min-height: 400px;
    }

    .nest-menu {
        min-width: 210px !important;
        background-color: #1f2d3d !important;
    }

    .nest-menu:hover {
        background-color: #001528 !important;
    }

    .submenu:hover {
        background-color: rgba(0, 0, 0, 0.06) !important;
    }

    .submenu-title-noDropdown,
    .el-submenu__title {
        &:hover {
            background-color: rgba(0, 0, 0, 0.06) !important;
        }
    }

    .stitle {
        margin-left: 8px;
    }
    .scrollbar-wrapper{
        overflow-x: hidden !important;
    }
    .el-scrollbar__bar.is-horizontal{
        display: none !important;
    }
</style>
