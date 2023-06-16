<!--  -->
<template>
  <h1>销量查询</h1>
  <el-select v-model="year" placeholder="请选择查询的年份">
    <el-option label="2020" value="2020" />
    <el-option label="2021" value="2021" />
    <el-option label="2022" value="2022" />
    <el-option label="2023" value="2023" />
  </el-select>
  <el-select v-model="car_id" placeholder="请选择要查询的车辆">
    <el-option
      v-for="(car, index) in cars"
      :key="index"
      :label="car"
      :value="car"
    />
  </el-select>
          <el-button
          type="primary"
          :icon="Search"
          style="width: 300px"
          @click="gerCharData()"
          >Search</el-button
        >
  <div id="main" style="width: 90%; height: 400px"></div>
</template>

<script>
import * as echarts from "echarts";
import { Location, Search } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from "element-plus";
import axios from "axios";
export default {
  data() {
    return {
      user_id: "",
      chartData: [],
      year: "",
      car_id: "",
      cars: [],
    };
  },

  components: {},

  computed: {},

  mounted() {
    this.user_id = this.$route.query.user_id;
    this.getInfo();
  },
  methods: {
    gerCharData() {
      if (this.car_id == "") {
        ElMessage({
          type: "warning",
          message: "请选择相应的车辆",
        });
      } else if (this.year == "") {
        ElMessage({
          type: "warning",
          message: "请选择相应的年份",
        });
      } else {
        axios.post(`/api/ticket/year?year=${this.year}&car_id=${this.car_id}`, {}).then((res) => {
          this.chartData = res.data;
          this.draw();
        });
      }
    },
    getInfo() {
      axios.post("/api/ticket/cars", {}).then((res) => {
        this.cars = res.data;
      });
    },
    draw() {
      var chartDom = document.getElementById("main");
      var myChart = echarts.init(chartDom);
      var option;

      option = {
        xAxis: {
          type: "category",
          data: [
            "一月",
            "二月",
            "三月",
            "四月",
            "五月",
            "六月",
            "七月",
            "八月",
            "九月",
            "十月",
            "十一月",
            "十二月",
          ],
        },
        yAxis: {
          type: "value",
        },
        series: [
          {
            data: this.chartData,
            type: "line",
            smooth: true,
          },
        ],
      };

      option && myChart.setOption(option);
    },
  },
};
</script>
<style lang='scss' scoped>
</style>