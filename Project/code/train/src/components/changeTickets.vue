<!--  -->
<template>
  <div>
    <h1 @click="open">退票说明:点击查看</h1>
    <h1 @click="open2">改签说明:点击查看</h1>

    <el-table :data="tableData" style="width: 100%" max-height="500">
      <el-table-column label="车次" width="80" fixed>
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
                    prop="arrive_time"
                    label="发车时间"
                    width="180"
                  />
                  <el-table-column
                    prop="station_time"
                    label="发车时间"
                    width="180"
                  />
                </el-table>
              </el-scrollbar>
            </template>

            <template #reference>
              <el-tag>{{ scope.row.car_id }}</el-tag>
            </template>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column label="乘车人" width="200">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.second_user }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="姓名" width="80">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.name }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="出发地" width="100">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.start }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="到达地" width="100">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.end }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="出发时间" width="120">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.start_time }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="到达时间" width="120">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.end_time }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="座位类型" width="100">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.seat_type }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="票种" width="100">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.ticket_type }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="票价" width="100">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.money }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="车票日期" width="120">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.ticketTime }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="座位" width="120">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.seat_string }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="购票时间" width="200">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.buyTime }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="类型" width="100">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.type }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="退票" fixed="right">
        <template #default="scope">
          <el-button
            size="small"
            type="primary"
            @click="handleClick(scope.$index, scope.row)"
            >退票</el-button
          >
        </template>
      </el-table-column>

      <el-table-column label="改签" fixed="right">
        <template #default="scope">
          <el-button
            size="small"
            type="primary"
            @click="handleChange(scope.$index, scope.row)"
            >改签</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      v-model="dialogTableVisible"
      title="请选择改签的日期"
      style="width:100px"
    >
      <div class="block">
        <el-date-picker
          v-model="ticketTime"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="选择日期"
        />
      </div>
      <el-button
        type="primary"
        :icon="Search"
        style="width: 50px"
        @click="this.dialogTableVisible = false"
        >取消</el-button
      >
      <el-button
        type="primary"
        :icon="Search"
        style="width: 50px"
        @click="changeTicket()"
        >改签</el-button
      >
    </el-dialog>
  </div>
</template>

<script>
import { h } from "vue";
import { ElNotification } from "element-plus";
import axios from "axios";
import { ElMessage, ElMessageBox } from "element-plus";
export default {
  data() {
    return {
      user_id: "",
      tableData: [],
      dialogTableVisible: false,
      ticketTime: "",
      ticket: null,
    };
  },

  components: {},

  computed: {},

  mounted() {
    this.user_id = this.$route.query.user_id;
    this.getTickets();
  },

  methods: {
    handleClick(index, row) {
      axios.post("/api/ticket/return", row).then((res) => {
        ElMessage({
          type: "warning",
          message: res.data,
        });
        this.getTickets();
      });
    },
    handleChange(index, row) {
      this.dialogTableVisible = true;
      this.ticket = row;
    },
    changeTicket() {
      if (this.ticketTime == null || this.ticketTime == "") {
        ElMessage({
          type: "warning",
          message: "请选择改签的日期",
        });
      } else {
        this.$router.push({
          path: "childChange",
          query: {
            user_id: this.$route.query.user_id,
            ticketTime: this.ticketTime,
            // // tickets: JSON.stringify(this.tickets),
            // ticketTime: this.Form.time,
            // ticket_start: this.Form.start,
            // ticket_end: this.Form.end,
            // ticket_time: this.Form.time,
            ticket: JSON.stringify(this.ticket),
          },
        });
      }
    },
    open() {
      ElNotification({
        title: "退票说明",
        message:
          "1.开车前8天(含)以上退票的，不收取退票费;2.票面乘车站开车时间前48小时以上的按票价5%计，24小时以上、不足48小时的按票价10%计，不足24小时的按票价20%计;",
        duration: 0,
      });
    },
    open2() {
      ElNotification({
        title: "改签说明",
        message:
          "1、办理“变更到站”不收取手续费，火车票“变更到站”业务只能办理一次。2、办理“变更到站”和“车票改签”业务时，如果新车票票价高于原车票，将补收差额，如果新车票票价低于原车票的，将退还差额，不过差额部分将根据退票费标准收取手续费。",
        duration: 0,
      });
    },
    getTickets() {
      axios
        .post("/api/ticket/getChangeTickets", {
          user_id: this.user_id,
        })
        .then((res) => {
          this.tableData = res.data;
        });
    },
  },
};
</script>
<style lang='scss' scoped>
</style>