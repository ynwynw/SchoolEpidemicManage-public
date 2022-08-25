<template>
    <el-carousel indicator-position="outside" :autoplay="false" trigger="click">
        <el-carousel-item>
            <div style="width: 100%" id="line-box">
                <div id="line" style="height: 300px"></div>
            </div>
        </el-carousel-item>
        <el-carousel-item>
            <div style="width: 100%">
                <div id="line2" style="height: 300px"></div>
            </div>
        </el-carousel-item>
        <el-carousel-item>
            <div style="width: 100%">
                <div id="line3" style="height: 300px"></div>
            </div>
        </el-carousel-item>
    </el-carousel>
</template>

<script>
    import echarts from "echarts";

    const option1 = {
        title: {
            text: '全国疫情新增趋势'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: ['确诊', '疑似']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: [],
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name: '确诊',
                type: 'line',
                data: [],
                smooth: true
            },
            {
                name: '疑似',
                type: 'line',
                data: [],
                smooth: true
            }
        ]
    };

    const option2 = {
        title: {
            text: '全国累计/现有确诊趋势'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: ['累计确诊', '现有确诊']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: [],
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name: '累计确诊',
                type: 'line',
                data: [],
                smooth: true,
                itemStyle: {
                    normal: {
                        color: '#971b12', //改变折线点的颜色
                        lineStyle: {
                            color: '#971b12' //改变折线颜色
                        }
                    }
                }
            },
            {
                name: '现有确诊',
                type: 'line',
                data: [],
                smooth: true,
                itemStyle: {
                    normal: {
                        color: '#e44a3d', //改变折线点的颜色
                        lineStyle: {
                            color: '#e44a3d' //改变折线颜色
                        }
                    }
                }
            }
        ]
    };

    const option3 = {
        title: {
            text: '全国治愈/死亡趋势'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: ['累计治愈', '累计死亡']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: [],
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name: '累计治愈',
                type: 'line',
                data: [],
                smooth: true,
                itemStyle: {
                    normal: {
                        color: '#58a97a', //改变折线点的颜色
                        lineStyle: {
                            color: '#58a97a' //改变折线颜色
                        }
                    }
                }
            },
            {
                name: '累计死亡',
                type: 'line',
                data: [],
                smooth: true,
                itemStyle: {
                    normal: {
                        color: '#8f8f8f', //改变折线点的颜色
                        lineStyle: {
                            color: '#8f8f8f' //改变折线颜色
                        }
                    }
                }
            }
        ]
    };

    export default {
        name: "ChartThree",
        data(){
            return{

            }
        },
        mounted(){
            this.drawLine()
        },
        methods: {
            drawLine(){
                setTimeout(()=> {
                    this.$axios.get("/history").then(res => {
                        let history = JSON.parse(res.data.data)
                        let dateList = []
                        history.forEach(item => {
                            dateList.push(item.date)
                            option1.series[0].data.push(item.today.confirm)
                            option1.series[1].data.push(item.today.suspect)
                            option2.series[0].data.push(item.total.confirm)
                            option2.series[1].data.push(item.total.storeConfirm)
                            option3.series[0].data.push(item.total.heal)
                            option3.series[1].data.push(item.total.dead)
                        })
                        option1.xAxis.data = dateList
                        option2.xAxis.data = dateList
                        option3.xAxis.data = dateList
                        let trueWidth = document.getElementById('line-box').offsetWidth + 'px'

                        document.getElementById('line').style.width = trueWidth
                        let myChart = echarts.init(document.getElementById('line'))
                        myChart.setOption(option1)

                        document.getElementById('line2').style.width = trueWidth
                        let myChart2 = echarts.init(document.getElementById('line2'))
                        myChart2.setOption(option2)

                        document.getElementById('line3').style.width = trueWidth
                        let myChart3 = echarts.init(document.getElementById('line3'))
                        myChart3.setOption(option3)
                    })
                }, 100)
            }
        }
    }
</script>

<style scoped>

</style>
