<!--  -->
<template>
  <div class="aside">
    <el-radio-group v-model="isCollapse" style="margin-bottom: 20px">
      <el-radio-button :label="false">展开</el-radio-button>
      <el-radio-button :label="true">折叠</el-radio-button>
    </el-radio-group>
    <el-menu
      default-active="2"
      class="el-menu-vertical-demo"
      :collapse="isCollapse"
      @open="handleOpen"
      @close="handleClose"
      router
    >
      <el-sub-menu index="1">
        <template #title>
          <el-icon><location /></el-icon>
          <span>个人中心</span>
        </template>
        <el-menu-item-group>
          <el-menu-item
            index="/main"
            :route="{
              path: '/main',
              query: { user_id: this.user_id },
            }"
            >用户首页</el-menu-item
          >
          <el-menu-item
            index="/loginTime"
            :route="{
              path: '/loginTime',
              query: { user_id: this.user_id },
            }"
            >登录日志</el-menu-item
          >
        </el-menu-item-group>
        <el-menu-item
          index="/MyPerson"
          :route="{
            path: '/MyPerson',
            query: { user_id: this.user_id },
          }"
          >账户设置</el-menu-item
        >
      </el-sub-menu>

      <el-sub-menu index="2">
        <template #title>
          <el-icon><location /></el-icon>
          <span>车票</span>
        </template>
        <el-menu-item-group>
          <el-menu-item
            index="/buyTickets"
            :route="{
              path: '/buyTickets',
              query: { user_id: this.user_id },
            }"
            >购票中心</el-menu-item
          >
        </el-menu-item-group>
        <el-menu-item
          index="/changeTickets"
          :route="{
            path: '/changeTickets',
            query: { user_id: this.user_id },
          }"
          >退改签</el-menu-item
        >
        <el-sub-menu>
          <template #title><span>常用查询</span></template>
          <el-menu-item>路线查询</el-menu-item>
          <el-menu-item
            index="/ticketCharts"
            :route="{
              path: '/ticketCharts',
              query: { user_id: this.user_id },
            }"
            >余票查询</el-menu-item
          >
          <el-menu-item>销售查询</el-menu-item>
          <el-menu-item
            index="/buyHistory"
            :route="{
              path: '/buyHistory',
              query: { user_id: this.user_id },
            }"
            >订单查询</el-menu-item
          >
        </el-sub-menu>
      </el-sub-menu>

      <el-sub-menu index="3">
        <template #title>
          <el-icon><location /></el-icon>
          <span>数据中心</span>
        </template>
        <el-menu-item-group>
          <el-menu-item
            index="/info_ticket"
            :route="{
              path: '/info_ticket',
              query: { user_id: this.user_id },
            }"
            >销量查询</el-menu-item
          >
        </el-menu-item-group>

      </el-sub-menu>
    </el-menu>
  </div>
</template>

<script >
import { ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
const isCollapse = ref(true);
// const handleOpen = (key: string, keyPath: string[]) => {
//   console.log(key, keyPath)
// }
// const handleClose = (key: string, keyPath: string[]) => {
//   console.log(key, keyPath)
// }
export default {
  name: "Aside",
  data() {
    return {
      isCollapse: true,
      user_id: "",
    };
  },

  components: {},

  computed: {},

  mounted() {
    this.user_id = this.$route.query.user_id;
  },
  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
  },
};
</script>
<style lang='scss' scoped>
.aside {
  width: 15%;
  height: 670px;

}

.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}
</style>