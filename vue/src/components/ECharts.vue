<template>
  <div id="myChart" :style="{width: '100%', height: '405px'}"></div>
</template>
<script>
  import echarts from 'echarts'
  import 'echarts/map/js/china'

  const option = {
    title:{
      text:'全国疫情地图',
      show:true,
      // x轴居中
      x:'center',
      textStyle:{
        color:'#2b2b2b'
      }
    },
    tooltip:{
      //   鼠标移入时的提示信息，  一定要知道的是它和series数据是平级的
      tigger:'item',
      formatter:'地区：{b}<br />今日确诊: {c}'
    },
    series:[
//   series配置出来了以后才会有地图显示  所以series是关键
      {
        type:'map',
        //   图标类型
        map:'china',
        //   什么图标
        data:[],
        label:{
          //   设置地图每一个角落的名字
          show:true,
          color:'red'
        },
        zoom: 1.25,
        itemStyle:{
          borderColor:'blue'
        },
        emphasis:{
          label:{
            color:'#fff',
            fontSize:'10px'
          },
          itemStyle:{
            areaColor:'green'
          }
        }
      }
    ],
    visualMap:{
      //   设置类型为分段类型
      type:'piecewise',
      show:true,
      // 设置不用的段位
      pieces: [
        // 不指定 max，表示 max 为无限大（Infinity）。
        {min: 10000},
        {min: 1000, max: 9999},
        {min: 100, max: 999},
        {min: 10, max: 99},
        {min: 1, max: 9},
        {value:0}
      ],
      inRange: {
        // 范围所对应的颜色
        color: ['#fff', '#ffaa85', '#660208']
      }
    }
  }

  export default {
    name: 'ECharts',
    props: {
      list: {
        type: Array
      }
    },
    data () {
      return {
      }
    },
    mounted(){
      this.drawLine()
    },
    methods: {
      drawLine(){
        // 基于准备好的dom，初始化echarts实例
        let myChart = echarts.init(document.getElementById('myChart'))
        // 绘制图表
        option.series[0].data = this.list
        myChart.setOption(option)
      }
    }
  }
</script>

