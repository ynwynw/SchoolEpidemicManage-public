<template>
    <div>
        <el-row :gutter="20" style="text-align: center" v-if="vaccineTopData !== undefined">
            <el-col :span="8">
                <div class="local" style="background-color: #f1f8f4">
                    <div class="title">全球累计接种</div>
                    <div style="color: #178b50">
                        <span class="count-data">{{((vaccineTopData.全球.total_vaccinations)/100000000).toFixed(1) }}</span>
                        <span class="unit">亿剂</span>
                    </div>
                </div>
                <div class="local" style="background-color: #f1f8f4; margin-top: 10px">
                    <div class="title">中国累计接种</div>
                    <div style="color: #178b50">
                        <span class="count-data">{{((vaccineTopData.中国.total_vaccinations)/100000000).toFixed(1) }}</span>
                        <span class="unit">亿剂</span>
                    </div>
                </div>
            </el-col>
            <el-col :span="8">
                <div class="local" style="background-color: #fffaf7">
                    <div class="title">全球较上日新增</div>
                    <div style="color: #e57631">
                        <span class="count-data">{{ ((vaccineTopData.全球.new_vaccinations)/10000).toFixed(1) }}</span>
                        <span class="unit">万剂</span>
                    </div>
                </div>
                <div class="local" style="background-color: #fffaf7;margin-top: 10px">
                    <div class="title">中国较上日新增</div>
                    <div style="color: #e57631">
                        <span class="count-data">{{ ((vaccineTopData.中国.new_vaccinations)/10000).toFixed(1) }}</span>
                        <span class="unit">万剂</span>
                    </div>
                </div>
            </el-col>
            <el-col :span="8">
                <div class="local" style="background-color: #f3f6f8">
                    <div class="title">全球每百人接种</div>
                    <div style="color: #4e5a65">
                        <span class="count-data">{{ vaccineTopData.全球.total_vaccinations_per_hundred }}</span>
                        <span class="unit">剂</span>
                    </div>
                </div>
                <div class="local" style="background-color: #f3f6f8;margin-top: 10px">
                    <div class="title">中国每百人接种</div>
                    <div style="color: #4e5a65">
                        <span class="count-data">{{ vaccineTopData.中国.total_vaccinations_per_hundred }}</span>
                        <span class="unit">剂</span>
                    </div>
                </div>
            </el-col>
            <el-col :span="24" style="margin-top: 15px">
                <el-carousel indicator-position="outside" :autoplay="false" trigger="click">
                    <el-carousel-item>
                        <div style="width: 100%" id="chart-box">
                            <div id="chart1" style="height: 300px"></div>
                        </div>
                    </el-carousel-item>
                    <el-carousel-item>
                        <div style="width: 100%">
                            <div id="chart2" style="height: 300px"></div>
                        </div>
                    </el-carousel-item>
                </el-carousel>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    import echarts from "echarts";

    const option1 = {
        title: {
            text: '中国疫苗累计接种趋势'
        },
        tooltip: {
            trigger: 'axis',
            formatter:function(params){
                return `日期：${params[0].axisValue}<br />接种人数：${((params[0].data)/100000000).toFixed(2)}亿`
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'category',
            data: []
        },
        yAxis: {
            axisLabel: {
                formatter: function (value, index) {
                    if (value >= 100000000) {
                        return value / 100000000 + "亿";
                    } else if (value >= 10000) {
                        return value / 10000 + "万";
                    } else {
                        return value;
                    }
                },
            },
            type: 'value'
        },
        series: [
            {
                data: [],
                type: 'line',
                smooth: true
            }
        ]
    };
    const option2 = {
        title: {
            text: '中国疫苗每百人接种趋势'
        },
        tooltip: {
            trigger: 'axis',
            formatter:function(params){
                return `日期：${params[0].axisValue}<br />每百人接种：${params[0].data}剂`
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'category',
            data: []
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                data: [],
                type: 'line',
                smooth: true
            }
        ]
    };

    export default {
        name: "Vaccine",
        data(){
            return{
                vaccineTopData: undefined,
                dateList: []
            }
        },
        methods: {
            getVaccineTopData(){
                this.$axios.get("/vaccineTopData").then(res => {
                    this.vaccineTopData = res.data.data
                })
            },
            drawLine(){
                setTimeout(()=> {
                    this.$axios.get("/chinaVaccineTrendData").then(res => {
                        let trendData = res.data.data
                        trendData.forEach(item => {
                            this.dateList.push(item.date)
                            option1.series[0].data.push(item.total_vaccinations)
                            option2.series[0].data.push(item.total_vaccinations_per_hundred)
                        })
                        option1.xAxis.data = this.dateList
                        option2.xAxis.data = this.dateList
                        let trueWidth = document.getElementById('chart-box').offsetWidth + 'px'
                        document.getElementById('chart1').style.width = trueWidth
                        let myChart = echarts.init(document.getElementById('chart1'))
                        myChart.setOption(option1)
                        document.getElementById('chart2').style.width = trueWidth
                        let myChart2 = echarts.init(document.getElementById('chart2'))
                        myChart2.setOption(option2)
                    })
                }, 100)
            }
        },
        created() {
            this.getVaccineTopData()
        },
        mounted() {
            this.drawLine()
        }
    }
</script>

<style scoped>
    .title{
        font-size: 19px;
        color: #222;
        letter-spacing: 0;
        margin-bottom: 14px;
    }
    .count-data{
        font-weight: 600;
        line-height: 19px;
        font-size: 29px;
    }
    .unit{
        font-weight: 600;
        text-align: center;
        vertical-align: bottom;
        font-size: 17px;
    }
</style>
