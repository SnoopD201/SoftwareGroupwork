<!--  -->
<template>
  <div>
    <el-scrollbar height="550px">
      <el-table :data="tableData" style="width: 100%">
        <el-table-column label="车次" width="80">
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
                    <el-table-column
                      prop="station_id"
                      label="站序"
                      width="180"
                    />
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
      </el-table>
    </el-scrollbar>
  </div>
</template>

<script>
import axios from "axios";
import { ElMessage, ElMessageBox } from "element-plus";
export default {
  data() {
    return {
      user_id: "",
      tableData: [],
    };
  },

  components: {},

  computed: {},

  mounted() {
    this.user_id = this.$route.query.user_id;
    this.getTickets();
  },

  methods: {
    getTickets() {
      axios
        .post("/api/ticket/buyHistory", {
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