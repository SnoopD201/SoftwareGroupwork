<!--  -->
<template>
  <div>
    <el-scrollbar height="550px">
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

            <el-table-column label="距离" width="100">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.distance }}</span>
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
      </el-scrollbar>
    <!-- {{ this.users }} -->

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

    <!-- 购买票 -->

    <el-dialog v-model="dialogTableVisible" title="购票信息">
      日期:{{ ticketTime }},{{ car_id }}次:{{ start }}站({{ start_time }})---{{
        end
      }}站({{ end_time }})
      <br />
      <br />
      选择座位:
      <el-radio-group v-model="radio">
        <el-radio :label="1"
          ><el-icon><Place /></el-icon>A座</el-radio
        >
        <el-radio :label="2"
          ><el-icon><Place /></el-icon>B座</el-radio
        >
        <el-radio :label="3"
          ><el-icon><Place /></el-icon>C座</el-radio
        >

        <el-radio :label="4" style="margin-left: 50px"
          ><el-icon><Place /></el-icon>E座</el-radio
        >
        <el-radio :label="5"
          ><el-icon><Place /></el-icon>F座</el-radio
        >
      </el-radio-group>
      <br />
      <br />
      选择乘车人:
      <el-checkbox-group v-model="checkedUsers">
        <el-checkbox
          v-for="(user, index) in users"
          :key="index"
          :label="user.name"
          @click="handleClick(user, $event, index)"
          >{{ user.name }}</el-checkbox
        >
      </el-checkbox-group>
      <br />

      <!-- 显示乘车人的信息 -->
      <el-table :data="usersData" style="width: 100%">
        <el-table-column label="日期" width="150">
          <div style="display: flex; align-items: center">
            <el-icon><timer /></el-icon>
            {{ this.ticketTime }}
          </div>
        </el-table-column>
        <el-table-column label="身份证" width="200">
          <template #default="scope">
            <div style="display: flex; align-items: center">
              <span style="margin-left: 10px">{{ scope.row.user_id }}</span>
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

        <el-table-column label="票种" width="80">
          <template #default="scope">
            <div style="display: flex; align-items: center">
              <span style="margin-left: 10px">{{ scope.row.ticket_type }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="证件类型" width="180">
          <template #default="scope">
            <div style="display: flex; align-items: center">
              <span style="margin-left: 10px">{{ scope.row.id_type }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="座位" width="180">
          <template #default="scope">
            <div style="display: flex; align-items: center">
              <span style="margin-left: 10px">
                <el-select
                  v-model="scope.row.seat_type"
                  placeholder="座位"
                  @change="handleChange(scope.row)"
                >
                  <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                    :disabled="item.disabled"
                  />
                </el-select>
              </span>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="票价" width="180">
          <template #default="scope">
            <div style="display: flex; align-items: center">
              <span style="margin-left: 10px">{{ scope.row.money }}</span>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <el-button @click="buy()">支付</el-button>
    </el-dialog>
  </div>
</template>

<script>
import { Timer } from "@element-plus/icons-vue";
import { Location, Search } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { reactive, ref } from "vue";
import axios from "axios";
import { fa } from "element-plus/es/locale";
export default {
  data() {
    return {
      user_id: null,
      tableData: null,
      tickets: [],
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

      dialogTableVisible: false,
      car_id: "",
      start: "",
      end: "",
      start_time: "",
      end_time: "",
      users: [],
      radio: "",
      checkedUsers: [],
      usersData: [],
      flag: [],
      options: [
        {
          value: "一等",
          label: "一等",
        },
        {
          value: "二等",
          label: "二等",
        },
        {
          value: "三等",
          label: "三等",
        },
      ],
      distance: 0,
      ticket_start: "",
      ticket_end: "",
      ticket_time: "",
    };
  },

  components: {},

  computed: {},

  mounted() {
    this.user_id = this.$route.query.user_id;
    // this.tickets = this.$route.query.tickets;
    this.ticketTime = this.$route.query.ticketTime;
    // this.tableData = JSON.parse(this.tickets);
    this.ticket_start = this.$route.query.ticket_start;
    this.ticket_end = this.$route.query.ticket_end;
    this.getSecondUser();
    //初始化乘车人，每个乘车人都没有被选择
    this.setUsers();
    this.getTickets();
  },

  methods: {
    setUsers() {
      for (var i = 0; i < this.users.length; i++) {
        this.flag[i] = false;
      }
    },
    handleClick(user, e, index) {
      //点击事件触发两次，这个方法避免发生两次点击事件
      if (e.target.tagName === "INPUT") return;
      this.flag[index] = !this.flag[index];
      this.usersData = [];
      var k = 0;
      var i = 0;
      for (i; i < this.users.length; i++) {
        if (this.flag[i]) {
          this.usersData[k] = this.users[i];
          k++;
        }
      }
    },
    handleChange(row) {
      if (row.seat_type == "一等") {
        if (row.ticket_type == "成人票") {
          row.money = (this.distance * 0.8).toFixed(2);
        } else if (row.ticket_type == "学生票") {
          row.money = (this.distance * 0.7).toFixed(2);
        } else if (row.ticket_type == "儿童票") {
          row.money = (this.distance * 0.7).toFixed(2);
        }
      } else if (row.seat_type == "二等") {
        if (row.ticket_type == "成人票") {
          row.money = (this.distance * 0.5).toFixed(2);
        } else if (row.ticket_type == "学生票") {
          row.money = (this.distance * 0.3).toFixed(2);
        } else if (row.ticket_type == "儿童票") {
          row.money = (this.distance * 0.3).toFixed(2);
        }
      } else if (row.seat_type == "三等") {
        if (row.ticket_type == "成人票") {
          row.money = (this.distance * 0.25).toFixed(2);
        } else if (row.ticket_type == "学生票") {
          row.money = (this.distance * 0.2).toFixed(2);
        } else if (row.ticket_type == "儿童票") {
          row.money = (this.distance * 0.2).toFixed(2);
        }
      }
    },
    getTickets() {
        
      axios
        .post("/api/ticket/sort", {
          start: this.ticket_start,
          end: this.ticket_end,
          ticketTime: this.ticketTime,
        })
        .then((res) => {
          this.tickets = res.data;
          this.tableData = res.data;
        //         ElMessage({
        //   type: "warning",
        //   message: res.data.length,
        // });
        });
    },
    buy() {
      var j = false;
      for (var i = 0; i < this.usersData.length; i++) {
        if (this.usersData[i].ticket_type == null||this.usersData[i].seat_type=="") {
          j = true;
        }
      }
      if (j) {
        ElMessage({
          type: "warning",
          message: "请选择相应的座位信息",
        });
      } else {
        axios
          .post(
            `/api/ticket/buyTicket?seat_location=${this.radio}&ticketTime=${this.ticketTime}&car_id=${this.car_id}&start=${this.start}&end=${this.end}`,
            this.usersData
          )
          .then((res) => {
            if (res.data == "购票成功") {
              this.dialogTableVisible = false;
              this.clear;
            }
            ElMessage({
              type: "warning",
              message: res.data,
            });
          });
        this.getTickets();
      }
    },
    clear() {
      //清空
      this.checkedUsers = [];
      this.setUsers();
      this.usersData = [];
      this.radio = "";
      for (var i = 0; i < this.users.length; i++) {
        this.users[i].seat_type = null;
        this.users[i].money = 0;
      }
    },
    getPrice(index, row) {
      //成人票
      this.fpSeatPrice = (row.distance * 0.8).toFixed(2);
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
    //获得票价
    getBuy(index, row) {
      //购买成人票
      this.dialogTableVisible = true;
      this.car_id = row.car_id;
      this.start = row.start;
      this.end = row.end;
      this.start_time = row.start_time;
      this.end_time = row.end_time;
      this.distance = row.distance;
    },
    getSecondUser() {
      axios
        .post(`/api/user/getSecondUser?user_id=${this.user_id}`, {})
        .then((res) => {
          this.users = res.data;
        });
    },
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