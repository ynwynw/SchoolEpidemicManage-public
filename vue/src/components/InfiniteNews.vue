<template>
    <el-scrollbar style="height: 445px" ref="scrollbar" class="sidebar" wrap-class="scrollbar-wrapper">
        <el-timeline>
            <el-timeline-item :timestamp="timestampToTime(item.time)" placement="top" v-for="item in dataList">
                <el-card>
                    <a :href="item.article.url" class="article" target="_blank">{{ item.title }}</a>
                    <span class="source_name">{{ item.article.source_name }}</span>
                </el-card>
            </el-timeline-item>
        </el-timeline>
    </el-scrollbar>
</template>

<script>
    export default {
        name: "InfiniteNews",
        data(){
            return{
                loading: false,
                id: undefined,
                dataList: []
            }
        },
        mounted() {
            this.$refs.scrollbar.handleScroll=()=>{
                let wrap = this.$refs.scrollbar.wrap;
                this.$refs.scrollbar.moveY = wrap.scrollTop * 100 / wrap.clientHeight;
                this.$refs.scrollbar.moveX = wrap.scrollLeft * 100 / wrap.clientWidth;
                let poor = wrap.scrollHeight - wrap.clientHeight
                if( poor === parseInt(wrap.scrollTop) || poor === Math.ceil(wrap.scrollTop) || poor === Math.floor(wrap.scrollTop) ){
                    if (!this.loading){
                        this.add()
                    }
                }
            }
        },
        methods: {
            add(){
                this.getData()
            },
            getData(){
                this.loading = true
                this.$axios.get("/infiniteNews", {params :{
                        id: this.id
                    }}).then(res => {
                        this.dataList = this.dataList.concat(res.data.data)
                        this.id = this.dataList[this.dataList.length - 1].article.id
                    this.loading = false
                })
            },
            timestampToTime(timestamp) {
                let date = new Date(parseInt(timestamp));
                let Y = date.getFullYear() + '年';
                let M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '月';
                let D = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + '日 ';
                let h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + '时';
                let m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + '分';
                let s = (date.getSeconds() < 10 ? '0'+date.getSeconds() : date.getSeconds()) + '秒';
                return Y + M + D + h + m + s;
            },
        },
        created() {
            this.getData()
        }
    }
</script>

<style lang="scss" scoped>
    .sidebar {
        z-index: 1;
        height: 100%;
        top: 0;
        bottom: 0;
        left: 0;
    }
    .source_name{
        font-size: 12px;
        color: rgba(0,0,0,.4);
        line-height: 15px;
        font-weight: 700;
        margin-top: 6px;
    }
    .article{
        font-weight: 700;
        font-size: 17px;
        line-height: 20px;
    }
</style>
