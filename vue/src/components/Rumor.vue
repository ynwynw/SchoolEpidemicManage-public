<template>
    <el-scrollbar style="height: 445px" ref="scrollbar" class="sidebar" wrap-class="scrollbar-wrapper">
        <div v-for="item in dataList">
            <div style="position: relative;display: flex" class="main-class">
                <div>
                    <a :href="'https://c.m.163.com/news/a/' + item.docid + '.html'" target="_blank">
                        <img width="120px" height="75px" :src="item.imgsrc" :alt="item.title"/>
                    </a>
                </div>
                <div style="padding: 0 0 10px 30px">
                    <div class="desc">
                        <a :href="'https://c.m.163.com/news/a/' + item.docid + '.html'" target="_blank">{{ item.title }}</a>
                    </div>
                    <div class="time">{{ item.ptime }}</div>
                </div>
            </div>
            <el-divider></el-divider>
        </div>
    </el-scrollbar>
</template>

<script>
    export default {
        name: "Rumor",
        data(){
            return{
                dataList: [],
                offset: 0,
                loading: false
            }
        },
        methods: {
            getList(){
                this.loading = true
                this.$axios.get("/rumor", {params:{
                        offset: this.offset
                    }}).then(res => {
                        if (res.data.code === 200){
                            this.offset += 6
                            this.dataList = this.dataList.concat(JSON.parse(res.data.data))
                        }
                    this.loading = false
                })
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
                        this.getList()
                    }
                }
            }
        },
        created() {
            this.getList()
        }
    }
</script>

<style lang="scss" scoped>
.desc{
    font-size: 20px;
}
.time{
    color: #858585;
    position:absolute;
    bottom: 0;
    font-size: 16px;
}
.sidebar {
    z-index: 1;
    height: 100%;
    top: 0;
    bottom: 0;
    left: 0;
}
a {
    color: inherit;
    text-decoration: none;
    display: block;
    border: none;
    -webkit-tap-highlight-color: rgba(255, 255, 255, 0);
    -webkit-user-select: none;
    -moz-user-select: none;
}

a:hover {
    color: #409eff;
}

a:active, a:visited, a:link, a:focus {
    -webkit-tap-highlight-color: transparent;
    outline: none;
    background: none;
    text-decoration: none;
}

::selection {
    background: #FFF;
    color: #333;
}

::-moz-selection {
    background: #FFF;
    color: #333;
}
</style>
