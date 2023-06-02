<!--  -->
<template>
  <h1>余票查询</h1>
  <el-date-picker
    v-model="ticketTime"
    type="date"
    value-format="YYYY-MM-DD"
    placeholder="选择日期"
  />

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
      ticketTime: "",
      car_id: "",
      cars: [],
      datas: [],
    };
  },

  components: {},

  computed: {},

  mounted() {
    this.user_id = this.$route.query.user_id;
    this.draw();
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
      } else {
        axios
          .post(
            `/api/ticket/remainTicket?ticketTime=${this.ticketTime}&car_id=${this.car_id}`
          )
          .then((res) => {

            //   先获得站点
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
      var app = {};

      var chartDom = document.getElementById("main");
      var myChart = echarts.init(chartDom);
      var option;

      option = {
        legend: {},
        tooltip: {},
        dataset: {
          dimensions: ["station", "fseat", "sseat", "tseat"],
          source:this.chartData
        },
        xAxis: { type: "category" },
        yAxis: {},
        // Declare several bar series, each will be mapped
        // to a column of dataset.source by default.
        series: [{ type: "bar" }, { type: "bar" }, { type: "bar" }],
      };

      option && myChart.setOption(option);
    },
  },
};
</script>
<style lang='scss' scoped>
</style>