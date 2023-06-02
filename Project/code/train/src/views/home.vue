<!-- 用户主页 -->
<template>
  <div>
    <Header class="header" :user_id="user_id"></Header>
    <el-container class="all">
      <el-container>
        <Aside class="aside" :user_id="user_id"></Aside>
        <el-container>
          <el-main class="main">
            <router-view style="flex: 1" />
          </el-main>
        </el-container>
      </el-container>
    </el-container>
    <Footer class="footer" :user_id="user_id"></Footer>
  </div>
</template>



<script>
import Header from "../components/Header.vue";
import Aside from "../components/Aside.vue";
import Footer from "../components/Footer.vue";
import { ElMessage, ElMessageBox } from "element-plus";
export default {
  name: "home",
  data() {
    return {
      user_id: "",
    };
  },

  components: { Header, Aside, Footer},

  computed: {},

  // created() {
  //   this.user_id=this.$route.query.user_id

  // },
  mounted() {
    this.user_id = this.$route.query.user_id;
    //最后要
    // this.$router.push({
    //   path: "main",
    //   query: {
    //     user_id: this.$route.query.user_id,
    //   },
    // });
  },
  methods: {},
};
</script>
<style lang='scss' scoped>

</style>