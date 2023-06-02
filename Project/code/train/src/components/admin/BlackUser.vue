<!--  -->
<template>
  <div>
    <el-table :data="tableData" style="width: 100%" max-height="500">
      <el-table-column label="用户" width="200">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.user_id }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="姓名" width="180">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.name }}</span>
          </div>
        </template>
      </el-table-column>
            <el-table-column label="手机" width="180">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.phone }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="注册时间" width="180">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.time }}</span>
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

      <el-table-column label="是否移出黑名单">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.$index, scope.row)"
            >移出</el-button
          >
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { Location, Search } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from "element-plus";
import axios from "axios";
export default {
  data() {
    return {
      user_id: "",
      tableData:[],
    };
  },

  components: {},

  computed: {},

  mounted() {
    this.user_id = this.$route.query.user_id;
    this.getUser();
  },

  methods: {
      handleEdit(index, row){
      axios
        .post("/api/user/removeBlack",row
        
        )
        .then((res) => {
         this.getUser();
        });
      },
    getUser() {
      axios
        .post("/api/user/BlackUser", {
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