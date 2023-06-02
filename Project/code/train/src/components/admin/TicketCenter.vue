<!--  -->
<template>
  <div>
    <input
      type="file"
      accept=".xls,.xlsx"
      class="upload-file"
      @change="changeExcel($event)"
    />

    <el-scrollbar height="550px">
      <el-card class="box-card" v-for="data in tableData" :key="data">
        <template #header>
          <div class="card-header">
            <span>{{ data[0].car_id }}</span>
            车辆状态:{{ data[0].state }}
            <el-button class="button" @click="stop(data[0].car_id)" text
              >停运</el-button
            >
            <el-button class="button" text @click="changeState(data[0].car_id)"
              >恢复</el-button
            >
            <el-button class="button" text>添加站点</el-button>
            <el-button class="button" text>车次介绍</el-button>
          </div>
        </template>
        <!-- <div v-for="o in 4" :key="o" class="text item">
        {{ "List item " + o }}
      </div> -->
        <el-table :data="data" style="width: 100%" max-height="500">
          <el-table-column label="车次" width="200">
            <template #default="scope">
              <div style="display: flex; align-items: center">
                <span style="margin-left: 10px">{{ scope.row.car_id }}</span>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="站序" width="180">
            <template #default="scope">
              <div style="display: flex; align-items: center">
                <span style="margin-left: 10px">{{
                  scope.row.station_id
                }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="站名" width="180">
            <template #default="scope">
              <div style="display: flex; align-items: center">
                <span style="margin-left: 10px">{{ scope.row.station }}</span>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="到站时间" width="180">
            <template #default="scope">
              <div style="display: flex; align-items: center">
                <span style="margin-left: 10px">{{
                  scope.row.arrive_time
                }}</span>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="发车时间" width="180">
            <template #default="scope">
              <div style="display: flex; align-items: center">
                <span style="margin-left: 10px">{{
                  scope.row.station_time
                }}</span>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="是否进行修改">
            <template #default="scope">
              <el-button
                size="small"
                @click="handleEdit(scope.$index, scope.row)"
                >修改</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </el-scrollbar>
    <br />
    <el-dialog v-model="dialogTableVisible" title="修改站点信息">
      车站名称:<el-input
        v-model="car_id"
        disabled
        placeholder="Please input"
        style="width: 200px"
      />
      <br />
      <br />站点名称:
      <el-select v-model="station" placeholder="请选择要查询的车辆">
        <el-option
          v-for="(s, index) in stations"
          :key="index"
          :label="s"
          :value="s"
        />
      </el-select>
      <br />
      <br />
      到站时间:
      <el-time-picker
        v-model="arrive_time"
        arrow-control
        placeholder="到站时间"
        value-format="HH:mm:ss"
      />
      <br />
      <br />
      发车时间:
      <el-time-picker
        v-model="station_time"
        arrow-control
        placeholder=" 发车时间"
        value-format="HH:mm:ss"
      />
      <br />
      <br />
      <br />
      <el-button size="large" @click="changeStation()">修改</el-button>
    </el-dialog>
  </div>
</template>

<script>
import { ref } from "vue";
import { Location, Search } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from "element-plus";
import axios from "axios";
import * as XLSX from "xlsx"; // vue3可用此引入

export default {
  data() {
    return {
      user_id: "",
      tableData: [],
      data: [],
      dialogTableVisible: false,
      car_id: null,
      station: null,
      station_time: null,
      arrive_time: null,
      stations: [],
      station_id: null,
      excelData: [],
    };
  },

  components: {},

  computed: {},

  mounted() {
    this.user_id = this.$route.query.user_id;
    this.getTicket();
  },

  methods: {
    changeExcel(e) {
      const files = e.target.files;
      if (files.length <= 0) {
        return false;
      } else if (!/\.(xls|xlsx)$/.test(files[0].name.toLowerCase())) {
        console.log("上传格式不正确，请上传xls或者xlsx格式");
        return false;
      }
      // 读取表格
      const fileReader = new FileReader();
      fileReader.onload = (ev) => {
        const workbook = XLSX.read(ev.target.result, {
          type: "binary",
        });
        const wsname = workbook.SheetNames[0];
        const ws = XLSX.utils.sheet_to_json(workbook.Sheets[wsname]);
        console.log("ws:", ws); // 转换成json的数据
        this.dealExcel(ws); //...对数据进行自己需要的操作
        this.excelData= ws;
      };
      fileReader.readAsBinaryString(files[0]);
    },
    dealExcel(ws) {
      let keymap = {
        // 我们要转换的开头
        车次: "car_id",
        站点: "station",
        距离: "distance",
        站点序号: "station_id",
        发车时间: "station_time",
        到站时间: "arrive_time",
        城市: "city",
      };
      ws.forEach((sourceObj) => {
        Object.keys(sourceObj).map((keys) => {
          let newKey = keymap[keys];
          if (newKey) {
            sourceObj[newKey] = sourceObj[keys];
            delete sourceObj[keys];
          }
        });
      });
      console.log("excelData:",ws);

    axios
        .post("/api/ticket/doExcel", ws)
        .then((res) => {
          this.getTicket();
          ElMessage({
          type: "warning",
          message: res.data,
        });
        });
    },

    stop(car_id) {
      axios.post(`/api/ticket/stopState?car_id=${car_id}`, {}).then((res) => {
        ElMessage({
          type: "warning",
          message: res.data,
        });
        this.getTicket();
      });
    },
    changeState(car_id) {
      axios.post(`/api/ticket/changeState?car_id=${car_id}`, {}).then((res) => {
        ElMessage({
          type: "warning",
          message: "已恢复",
        });
        this.getTicket();
      });
    },
    changeStation() {
      if (
        this.station_time == "" ||
        this.station_time == null ||
        this.arrive_time == "" ||
        this.arrive_time == null
      ) {
        ElMessage({
          type: "warning",
          message: "所有信息不能为空",
        });
      } else {
        axios
          .post("/api/ticket/changeStations", {
            car_id: this.car_id,
            station: this.station,
            station_time: this.station_time,
            arrive_time: this.arrive_time,
            station_id: this.station_id,
          })
          .then((res) => {
            ElMessage({
              type: "warning",
              message: res.data,
            });
            this.getTicket();
          });
      }
    },
    getStations() {
      axios
        .post(`/api/ticket/getStations?car_id=${this.car_id}`, {})
        .then((res) => {
          this.stations = res.data;
        });
    },
    handleEdit(index, row) {
      this.dialogTableVisible = true;
      this.car_id = row.car_id;
      this.station = row.station;
      this.station_id = row.station_id;
      this.getStations();
    },

    getTicket() {
      axios.post("/api/ticket/allCars", {}).then((res) => {
        this.tableData = res.data;
      });
    },
  },
};
</script>
<style lang='scss' scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.box-card {
  width: 98%;
}
</style>