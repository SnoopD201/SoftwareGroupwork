<!--  -->
<template>
  <div>
    <el-table :data="tableData" style="width: 100%" max-height="500">
      <el-table-column label="车次" width="180">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.car_id }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="一等座" width="180">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.fseat }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="二等座" width="180">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.sseat }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="三等座" width="180">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span style="margin-left: 10px">{{ scope.row.tseat }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="修改">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.$index, scope.row)"
            >修改</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogTableVisible" title="修改座位信息"> 
      一等座  <el-input-number v-model="fseat" :min="1" :max="10000"  />
      <br>
      <br>
       二等座  <el-input-number v-model="sseat" :min="1" :max="10000"  />
       <br>
       <br>
        三等座  <el-input-number v-model="tseat" :min="1" :max="10000"  />
        <br>
        <br>
        <el-button type="primary" @click="this.dialogTableVisible=false">取消</el-button> <el-button type="primary" @click="this.change()">修改</el-button>
    </el-dialog>
  </div>
</template>

<script>
import { Timer } from "@element-plus/icons-vue";
import { Location, Search } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from "element-plus";
import axios from "axios";
export default {
  data() {
    return {
      user_id: "",
      tableData: [],
      dialogTableVisible: false,
      car_id:"",
      fseat:null,
      sseat:null,
      tseat:null,
    };
  },

  components: {},

  computed: {},

  mounted() {
    this.user_id = this.$route.query.user_id;
    this.getSeats();
  },

  methods: {
      change(){
      axios.post("/api/ticket/changeSeats", {
          car_id:this.car_id,
          fseat:this.fseat,
          sseat:this.sseat,
          tseat:this.tseat,
      }).then((res) => {
        this.tableData = res.data;
        this.getSeats();
        this.dialogTableVisible=false;
      });
      },
    handleEdit(index, row) {
      this.dialogTableVisible = true;
      this.car_id=row.car_id;
      this.fseat=row.fseat;
      this.sseat=row.sseat;
      this.tseat=row.tseat;
    },
    getSeats() {
      axios.post("/api/ticket/getSeats", {}).then((res) => {
        this.tableData = res.data;
      });
    },
  },
};
</script>
<style lang='scss' scoped>
</style>