<template>
    <div>
        <i :class="isFullscreen ? 'el-icon-copy-document' : 'el-icon-full-screen'"
           class="screenfull-svg"
           @click="click"
        />
    </div>
</template>

<script>
    import screenfull from 'screenfull'

    export default {
        name: "Screenfull",
        data() {
            return {
                isFullscreen: false
            }
        },
        mounted() {
            this.init()
        },
        beforeDestroy() {
            this.destroy()
        },
        methods: {
            click() {
                if (!screenfull.enabled) {
                    this.$message({
                        message: '您的浏览器不支持',
                        type: 'warning'
                    })
                    return false
                }
                screenfull.toggle()
            },
            change() {
                this.isFullscreen = screenfull.isFullscreen
            },
            init() {
                if (screenfull.enabled) {
                    screenfull.on('change', this.change)
                }
            },
            destroy() {
                if (screenfull.enabled) {
                    screenfull.off('change', this.change)
                }
            }
        }
    }
</script>

<style scoped>
    .screenfull-svg {
        display: inline-block;
        cursor: pointer;
        font-size: 18px;
        width: 20px;
        height: 20px;
    }
</style>
