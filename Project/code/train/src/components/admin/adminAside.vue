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
      <el-sub-menu index="2">
        <template #title>
          <el-icon><location /></el-icon>
          <span>用户数据中心</span>
        </template>
        <el-menu-item-group>
          <el-menu-item
            index="/allUsers"
            :route="{
              path: '/allUsers',
              query: { user_id: this.user_id },
            }"
            >已注册用户</el-menu-item
          >
                    <el-menu-item
            index="/BlackUser"
            :route="{
              path: '/BlackUser',
              query: { user_id: this.user_id },
            }"
            >乘车黑名单</el-menu-item
          >
        </el-menu-item-group>
        <el-menu-item
          index="/seats"
          :route="{
            path: '/seats',
            query: { user_id: this.user_id },
          }"
          >座位管理
        </el-menu-item>
        <el-menu-item
          index="/TicketCenter"
          :route="{
            path: '/TicketCenter',
            query: { user_id: this.user_id },
          }"
          >运营中心</el-menu-item
        >
      </el-sub-menu>

      <el-sub-menu index="3">
        <template #title>
          <el-icon><location /></el-icon>
          <span>数据中心</span>
        </template>
        <el-menu-item-group>
          <el-menu-item
            index="/admin_info_ticket"
            :route="{
              path: '/admin_info_ticket',
              query: { user_id: this.user_id },
            }"
            >销量查询</el-menu-item
          >
                    <el-menu-item
            index="/admin_ticket"
            :route="{
              path: '/admin_ticket',
              query: { user_id: this.user_id },
            }"
            >余票查询</el-menu-item
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