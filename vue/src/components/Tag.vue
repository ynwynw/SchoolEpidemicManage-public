<template>
    <div class="tabs">
        <el-scrollbar ref="scrollContainer" :vertical="false" class="scroll"
                      @wheel.native.prevent="handleScroll">
            <el-tag
                    v-for="tag in tags"
                    :key="tag.path"
                    size="medium"
                    :closable="tag.name!=='index'"
                    class="tags-view-item"
                    :disable-transitions="false"
                    :class="isActive(tag)? 'active' : ''"
                    @close="handleClose(tag)"
                    @click="selectTag(tag)"
            >
                {{tag.title}}
            </el-tag>
        </el-scrollbar>
    </div>
</template>

<script>
    import {mapState, mapMutations} from 'vuex'

    export default {
        name: "Tag",
        data() {
            return {
                left: 0
            }
        },
        computed: {
            ...mapState({
                tags: (state) => state.side.tagsList
            }),
            scrollWrapper() {
                return this.$refs.scrollContainer.$refs.wrap
            }
        },
        mounted() {
            this.scrollWrapper.addEventListener('scroll', this.emitScroll, true)
        },
        beforeDestroy() {
            this.scrollWrapper.removeEventListener('scroll', this.emitScroll)
        },
        methods: {
            ...mapMutations({
                close: 'closeTag'
            }),
            handleScroll(e) {
                const eventDelta = e.wheelDelta || -e.deltaY * 40
                const $scrollWrapper = this.scrollWrapper
                $scrollWrapper.scrollLeft = $scrollWrapper.scrollLeft + eventDelta / 4
            },
            emitScroll() {
                this.$emit('scroll')
            },
            handleClose(tag) {
                this.close(tag)
                if (tag.name !== this.$route.name) {
                    return;
                }
                this.$router.push(this.$store.state.side.tagsList[this.$store.state.side.tagsList.length-1].path)
            },
            selectTag(tag){
                this.$router.push(tag.path)
                this.$store.commit("selectTag", tag)
            },
            isActive(tag){
                return tag.name === this.$route.name
            }
        },
        created() {
            let tag = JSON.parse(localStorage.getItem('currentTag'))
            this.$store.commit("addTag", tag)
            this.$router.push(tag.path)
        }
    }
</script>

<style lang="scss" scoped>
    .tabs {
        padding: 10px 10px 0 10px;
        background: #fff;
        border-bottom: 1px solid #d8dce5;
        box-shadow: 0 1px 3px 0 rgba(0, 0, 0, .12), 0 0 3px 0 rgba(0, 0, 0, .04);
    }

    .el-tag + .el-tag {
        margin-left: 10px;
    }

    .scroll {
        white-space: nowrap;
        position: relative;
        width: 100%;
        margin-bottom: 2px;
        ::v-deep {
            .el-scrollbar__bar {
                bottom: 0;
            }

            .el-scrollbar__wrap {
                height: 49px;
            }
        }
    }

    .tags-view-item {
        display: inline-block;
        position: relative;
        cursor: pointer;
        height: 26px;
        line-height: 26px;
        border: 1px solid #d8dce5;
        border-radius: 3px;
        color: #495060;
        background: #fff;
        padding: 0 8px;
        font-size: 12px;

        &.active {
            background-color: #409eff;
            color: #fff;
            border-color: #409eff;

            &::before {
                content: '';
                background: #fff;
                display: inline-block;
                width: 8px;
                height: 8px;
                border-radius: 50%;
                position: relative;
                margin-right: 3px;
            }
        }
    }
</style>
<style lang="scss">
    .tags-view-item {
        .el-icon-close {
            width: 16px;
            height: 16px;
            vertical-align: 0;
            border-radius: 50%;
            text-align: center;
            transition: all .3s cubic-bezier(.645, .045, .355, 1);
            transform-origin: 100% 50%;

            &:before {
                display: inline-block;
                vertical-align: -1px;
            }

            &:hover {
                background-color: #b4bccc !important;
                color: #fff !important;
            }
        }
    }

    .el-tag .el-tag__close {
        color: #b4bccc !important;
    }
</style>
