<template>
    <div :class="{'show':show}" class="header-search">
        <i class="el-icon-search search-icon" @click.stop="click" />
        <el-select
                ref="headerSearchSelect"
                v-model="search"
                :remote-method="querySearch"
                filterable
                default-first-option
                remote
                placeholder="搜索系统功能"
                class="header-search-select"
                @change="change"
        >
            <el-option v-for="option in options"
                   :key="option.item.path"
                   :value="option.item"
                   :label="option.item.parentTitle + ' / ' + option.item.title"
            />
        </el-select>
    </div>
</template>

<script>
    import Fuse from 'fuse.js/dist/fuse.min.js'

    export default {
        name: "SearchHeader",
        data(){
            return {
                search: '',
                options: [],
                searchPool: [],
                show: false,
                fuse: undefined
            }
        },
        computed: {
            routes() {
                return this.$store.state.menu.menus
            }
        },
        mounted() {
            this.searchPool = this.routes
        },
        watch: {
            routes() {
                this.searchPool = this.routes
            },
            searchPool(list) {
                this.initFuse(list)
            },
            show(value) {
                if (value) {
                    document.body.addEventListener('click', this.close)
                } else {
                    document.body.removeEventListener('click', this.close)
                }
            }
        },
        methods:{
            click() {
                this.show = !this.show
                if (this.show) {
                    this.$refs.headerSearchSelect && this.$refs.headerSearchSelect.focus()
                }
            },
            querySearch(query) {
                if (query !== '') {
                    this.options = this.fuse.search(query)
                } else {
                    this.options = []
                }
            },
            change(val) {
                this.addTag(val)
                this.search = ''
                this.options = []
                this.$nextTick(() => {
                    this.show = false
                })
            },
            addTag(val) {
                this.$router.push(val.path)
                this.$store.commit("addTag", {
                    path: val.path,
                    title: val.title,
                    name: val.name,
                    parentTitle: val.parentTitle
                })
            },
            initFuse(list) {
                this.fuse = new Fuse(list, {
                    shouldSort: true,
                    threshold: 0.4,
                    location: 0,
                    distance: 100,
                    maxPatternLength: 32,
                    minMatchCharLength: 1,
                    keys: [{
                        name: 'title',
                        weight: 0.7
                    }]
                    // {
                    // name: 'path',
                    // weight: 0.3
                    // }
                })
            },
            close() {
                this.$refs.headerSearchSelect && this.$refs.headerSearchSelect.blur()
                this.options = []
                this.show = false
            },
        }
    }
</script>

<style lang="scss" scoped>
    .header-search {
        font-size: 0 !important;

        .search-icon {
            cursor: pointer;
            font-size: 18px;
            vertical-align: middle;
        }

        .header-search-select {
            font-size: 18px;
            transition: width 0.2s;
            width: 0;
            overflow: hidden;
            background: transparent;
            border-radius: 0;
            display: inline-block;
            vertical-align: middle;

            ::v-deep .el-input__inner {
                border-radius: 0;
                border: 0;
                padding-left: 0;
                padding-right: 0;
                box-shadow: none !important;
                border-bottom: 1px solid #d9d9d9;
                vertical-align: middle;
            }
        }

        &.show {
            .header-search-select {
                width: 180px;
                margin-left: 10px;
            }
        }
    }
</style>
