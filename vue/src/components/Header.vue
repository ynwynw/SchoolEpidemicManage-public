<template>
    <div class="navbar">
        <Hamburger class="hamburger-container"></Hamburger>
        <el-breadcrumb separator="/">
            <el-breadcrumb-item v-if="currentMenu.parentTitle">{{currentMenu.parentTitle}}</el-breadcrumb-item>
            <el-breadcrumb-item v-if="currentMenu.title!==currentMenu.parentTitle">{{currentMenu.title}}
            </el-breadcrumb-item>
        </el-breadcrumb>
        <div class="right-menu">
            <el-dropdown class="avatar-container" trigger="hover">
                <div class="avatar-wrapper">
                    <img :src="getUserInfo().avatar" class="user-avatar">
                </div>
                <el-dropdown-menu slot="dropdown" class="user-dropdown">
                    <div @click="addTag()">
                        <el-dropdown-item>
                            个人中心
                        </el-dropdown-item>
                    </div>
                    <div @click="logout()">
                        <el-dropdown-item>
                            <span style="display:block;">退出登陆</span>
                        </el-dropdown-item>
                    </div>
                </el-dropdown-menu>
            </el-dropdown>
        </div>
        <div class="right-menu" style="margin-right: 17px">{{ getUserInfo().nickname }}</div>
        <div class="right-menu" style="margin-right: 17px;margin-top: 1.5px">
            <Screenfull/>
        </div>
        <div class="right-menu" style="margin-right: 17px;margin-top: 1.5px">
            <SearchHeader/>
        </div>
    </div>
</template>

<script>
    import Hamburger from "./Hamburger";
    import Screenfull from "./Screenfull";
    import SearchHeader from "./SearchHeader";

    export default {
        name: "Header",
        components: {SearchHeader, Screenfull, Hamburger},
        computed: {
            currentMenu() {
                return this.$store.state.side.currentMenu
            }
        },
        methods: {
            getUserInfo() {
                let info = this.$store.state.userInfo
                if (info === null || info === '' || info === undefined){
                    info = JSON.parse(localStorage.getItem('userInfo'))
                }
                return info
            },
            addTag() {
                this.$router.push('/userCenter')
                this.$store.commit("addTag", {
                    path: '/userCenter',
                    title: '个人中心',
                    name: 'userCenter',
                    parentTitle: '系统设置'
                })
            },
            logout() {
                this.modal.confirm('确定要注销并退出系统吗？').then(function () {
                }).then(() => {
                    this.$axios.post("/logout").then(res => {
                        localStorage.clear()
                        sessionStorage.clear();
                        this.$store.commit('clearData')
                        this.$store.commit('initTag')
                        this.$store.commit("changeRouteStatus", false)
                        this.$router.push('/login')
                    })
                }).catch(function () {
                    this.modal.msgError("退出失败")
                })
            }
        }
    }
</script>

<style lang="scss">
    .navbar {
        height: 50px;
        overflow: hidden;
        position: relative;
        background: #fff;
        box-shadow: 0 1px 4px rgba(0, 21, 41, .08);

        .hamburger-container {
            line-height: 46px;
            height: 100%;
            float: left;
            cursor: pointer;
            transition: background .3s;
            -webkit-tap-highlight-color: transparent;

            &:hover {
                background: rgba(0, 0, 0, .025)
            }
        }

        .el-breadcrumb {
            display: inline-block;
            font-size: 14px;
            line-height: 50px;
            margin-left: 8px;

            .no-redirect {
                color: #97a8be;
                cursor: text;
            }
        }

        .right-menu {
            float: right;
            height: 100%;
            line-height: 50px;

            &:focus {
                outline: none;
            }

            .right-menu-item {
                display: inline-block;
                padding: 0 8px;
                height: 100%;
                font-size: 18px;
                color: #5a5e66;
                vertical-align: text-bottom;

                &.hover-effect {
                    cursor: pointer;
                    transition: background .3s;

                    &:hover {
                        background: rgba(0, 0, 0, .025)
                    }
                }
            }

            .avatar-container {
                margin-right: 13px;

                .avatar-wrapper {
                    margin-top: 5px;
                    position: relative;

                    .user-avatar {
                        cursor: pointer;
                        width: 40px;
                        height: 40px;
                        border-radius: 10px;
                    }
                }
            }
        }
    }

    .el-popper {
        margin-top: 1px !important;
    }
</style>
