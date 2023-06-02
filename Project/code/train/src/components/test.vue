<!--  -->
<template>
  <div>
      <div v-for="data in tableData" :key="data">
    <el-table :data="data" style="width: 100%" border>
      <el-table-column label="车次" width="180">
        <template #default="scope">
          <el-popover
            effect="light"
            trigger="hover"
            placement="auto"
            width="auto"
          >
            <template #default>
              <el-scrollbar height="200px" v-model="visible">
                <el-table
                  :data="scope.row.stationList"
                  border
                  style="width: 100%"
                >
                  <el-table-column prop="station_id" label="站序" width="180" />
                  <el-table-column prop="station" label="站名" width="180" />
                  <el-table-column
                    prop="station_time"
                    label="发车时间"
                    width="180"
                  />
                </el-table>
              </el-scrollbar>
            </template>

            <template #reference>
              <el-tag>{{ scope.row.car_id}}</el-tag>
            </template>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column label="出发地" width="180">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.start }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="到达地" width="180">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.end }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="出发时间" width="180">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.start_time }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="到达时间" width="180">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.end_time }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="一等座" width="100">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.fseat }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="二等座" width="100">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.sseat }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="三等座" width="100">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.tseat }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="票价">
        <template #default="scope">
          <el-button size="small" @click="getPrice(scope.$index, scope.row)"
            >查看价格</el-button
          >
        </template>
      </el-table-column>

      <el-table-column label="购买">
        <template #default="scope">
          <el-button size="small" @click="getBuy(scope.$index, scope.row)"
            >购买</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <br>
      </div>
    {{ this.tickets }}

    <el-dialog v-model="visible" title="票价信息">
      <el-form :model="form">
        <el-form-item>
          成人票:一等座{{ fpSeatPrice }},二等座{{ spSeatPrice }},三等座{{
            tpSeatPrice
          }}
        </el-form-item>
        <el-form-item>
          学生票:一等座{{ fsSeatPrice }},二等座{{ ssSeatPrice }},三等座{{
            tsSeatPrice
          }}
        </el-form-item>
        <el-form-item>
          儿童票:一等座{{ fcSeatPrice }},二等座{{ scSeatPrice }},三等座{{
            tcSeatPrice
          }}
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="visible = false">取消</el-button>
          <el-button type="primary" @click="visible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { Timer } from "@element-plus/icons-vue";
import { Location, Search } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { reactive, ref } from "vue";
import axios from "axios";
export default {
  data() {
    return {
      user_id: null,
      tableData: [[]],
      index:0,
      tickets: [[]],
      stationsData: [],
      ticketTime: "",
      visible: false,
      formLabelWidth: "140px",
      form: {
        seat_type: "",
      },
      fpSeatPrice: null,
      spSeatPrice: null,
      tpSeatPrice: null,
      fsSeatPrice: null,
      ssSeatPrice: null,
      tsSeatPrice: null,
      fcSeatPrice: null,
      scSeatPrice: null,
      tcSeatPrice: null,
    };
  },

  components: {},

  computed: {},

  mounted() {
    this.user_id = this.$route.query.user_id;
    this.tickets = this.$route.query.tickets;
    this.ticketTime = this.$route.query.ticketTime;
    // ElMessage({
    //   type: "warning",
    //   message: this.ticketTime,
    // });

    this.tableData = JSON.parse(this.tickets);
  },

  methods: {
    getPrice(index, row) {
      //成人票
      this.fpSeatPrice =  (row.distance * 0.8).toFixed(2);
      this.spSeatPrice = (row.distance * 0.5).toFixed(2);
      this.tpSeatPrice = (row.distance * 0.25).toFixed(2);

      //学生票
      this.fsSeatPrice = (row.distance * 0.7).toFixed(2);
      this.ssSeatPrice = (row.distance * 0.3).toFixed(2);
      this.tsSeatPrice = (row.distance * 0.2).toFixed(2);

      //儿童票
      this.fcSeatPrice = (row.distance * 0.7).toFixed(2);
      this.scSeatPrice = (row.distance * 0.3).toFixed(2);
      this.tcSeatPrice = (row.distance * 0.2).toFixed(2);

      this.visible = true;
    },
    getBuy(index, row) {},
  },
};
</script>
<style lang='scss' scoped>
.el-button--text {
  margin-right: 15px;
}
.el-select {
  width: 300px;
}
.el-input {
  width: 300px;
}
.dialog-footer button:first-child {
  margin-right: 10px;
}
</style>