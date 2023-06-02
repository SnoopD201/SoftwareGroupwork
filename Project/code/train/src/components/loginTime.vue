<!-- 登录日志 -->
<template>
  <el-scrollbar height="630px">
    <el-table :data="tableData" style="width: 100%">
      <el-table-column label="Date" width="500">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <el-icon><timer /></el-icon>
            <span style="margin-left: 10px">{{ scope.row.loginTime }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="Name" width="500">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.user_id }}</span>
          </div>
        </template>
      </el-table-column>
    </el-table>
  </el-scrollbar>
</template>

<script lang="scope">
import axios from "axios";
import { ElMessage, ElMessageBox } from "element-plus";
import { Timer } from '@element-plus/icons-vue'
export default {
  data() {
    return {
      user_id: "",
      tableData:null,

    };
  },

  components: {},

  computed(){
  },
  mounted() {
    this.getLoginTime();
    this.user_id = this.$route.query.user_id;
  },

  methods: {
    getLoginTime() {
      axios
        .post(`/api/user/loginTime?user_id=${this.$route.query.user_id}`, {})
        .then((res) => {
          this.tableData = res.data;
        });
    },
  },
};
</script>
<style lang='scss' scoped>
</style>