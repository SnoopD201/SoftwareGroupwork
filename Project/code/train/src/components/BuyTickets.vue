<!--  -->
<template>
  <div>
    <el-form class="form_css">
      <el-form-item class="form_item_css" label="出  发  地" prop="start">
        <el-input
          v-model="Form.start"
          type="text"
          autocomplete="off"
          :prefix-icon="Location"
        />
      </el-form-item>
      <el-form-item class="form_item_css" label="到  达  地" prop="end">
        <el-input
          v-model="Form.end"
          type="text"
          autocomplete="off"
          :prefix-icon="Location"
        />
      </el-form-item>
      <el-form-item label="出发时间" prop="time">
        <div class="demo-date-picker">
          <div class="block">
            <el-date-picker
              v-model="Form.time"
              type="date"
              value-format="YYYY-MM-DD"
              placeholder="选择日期"
            />
          </div>
        </div>
      </el-form-item>
      <el-form-item class="form_item_css">
        <el-switch
          v-model="value"
          size="large"
          active-text="转站"
          inactive-text="直达"
        />
        <br />
        <el-button
          type="primary"
          :icon="Search"
          style="width: 300px"
          @click="findAllShort"
          >查询最短路线</el-button
        >
        <br />
        <el-button
          type="primary"
          :icon="Search"
          style="width: 300px"
          @click="submit"
          >Search</el-button
        >
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
// 一个用户图像+密码锁图标
import { Location, Search } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from "element-plus";
import axios from "axios";
export default {
  data() {
    return {
      Form: {
        start: "",
        end: "",
        time: null,
      },
      tickets: [],
      value: false,
    };
  },
  setup() {
    return {
      Location,
      Search,
    };
  },

  components: {},

  computed: {},

  mounted() {
    this.user_id = this.$route.query.user_id;
  },

  methods: {
    findAllShort() {
      if (this.Form.start == "") {
        ElMessage({
          type: "warning",
          message: "请输入出发地",
        });
      } else if (this.Form.end == "") {
        ElMessage({
          type: "warning",
          message: "请输入到达地",
        });
      } else if (this.Form.time == null) {
        ElMessage({
          type: "warning",
          message: "请输入出发时间",
        });
      } else {
        this.$router.push({
          path: "ShortDis",
          query: {
            user_id: this.$route.query.user_id,
            // tickets: JSON.stringify(this.tickets),
            ticketTime: this.Form.time,
            ticket_start: this.Form.start,
            ticket_end: this.Form.end,
            ticket_time: this.Form.time,
          },
        });
      }
    },
    submit() {
      if (this.Form.start == "") {
        ElMessage({
          type: "warning",
          message: "请输入出发地",
        });
      } else if (this.Form.end == "") {
        ElMessage({
          type: "warning",
          message: "请输入到达地",
        });
      } else if (this.Form.time == null) {
        ElMessage({
          type: "warning",
          message: "请输入出发时间",
        });
      } else {
        //对数据进行提交
        //直达
        if (!this.value) {
          axios
            .post("/api/ticket/find", {
              user_id: this.user_id,
              start: this.Form.start,
              end: this.Form.end,
              ticketTime: this.Form.time,
            })
            .then((res) => {
              this.tickets = res.data;
              if (res.data.length == 0) {
                this.$router.push({
                  path: "emptyData",
                  query: {
                    user_id: this.$route.query.user_id,
                  },
                });
              } else {
                this.$router.push({
                  path: "Tickets",
                  query: {
                    user_id: this.$route.query.user_id,
                    // tickets: JSON.stringify(this.tickets),
                    ticketTime: this.Form.time,
                    ticket_start: this.Form.start,
                    ticket_end: this.Form.end,
                    ticket_time: this.Form.time,
                  },
                });
              }
            });
        } else {
          //需要转站
          axios
            .post("/api/ticket/findSecond", {
              user_id: this.user_id,
              start: this.Form.start,
              end: this.Form.end,
              ticketTime: this.Form.time,
            })
            .then((res) => {
              this.tickets = res.data;
              if (res.data.length == 0) {
                this.$router.push({
                  path: "emptyData",
                  query: {
                    user_id: this.$route.query.user_id,
                  },
                });
              } else {
                this.$router.push({
                  path: "SecondTickets",
                  query: {
                    user_id: this.$route.query.user_id,
                    // tickets: JSON.stringify(this.tickets),
                    ticketTime: this.Form.time,
                    ticket_start: this.Form.start,
                    ticket_end: this.Form.end,
                    ticket_time: this.Form.time,
                  },
                });
              }
            });
        }
      }
    },
  },
};
</script>
<style lang='scss' scoped>
.demo-date-picker {
  display: flex;
  width: 100%;
  padding: 0;
  flex-wrap: wrap;
}
.form_css {
  display: flex;
  width: 30%;
  padding: 0;
  flex-wrap: wrap;
  margin-left: 30%;
}
.form_item_css {
  display: flex;
  width: 77%;
  padding: 0;
  flex-wrap: wrap;
}
</style>