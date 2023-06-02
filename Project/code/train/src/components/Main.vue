<!-- 用户欢迎界面 -->
<template>
  <div class="main">
    <div class="title">
      欢迎来到铁路交通售票入口
    </div>

    <!-- 轮播图 -->
    <el-carousel :interval="4000" type="card" height="400px">
      <el-carousel-item>
        <img src="../assets/images/train2.jpg" />
      </el-carousel-item>
      <el-carousel-item>
        <img src="../assets/images/train4.jpg" />
      </el-carousel-item>
      <el-carousel-item>
        <img src="../assets/images/train1.jpg" />
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script>
export default {
  data() {
    return {
      user_id: "",
      cardList: [
        "../assets/images/banner20200707.jpg",
       "../assets/images/banner20201223.png",
       "../assets/images/banner26.jpg",
       "../assets/images/banner10.jpg",
       "../assets/images/banner12.jpg",
      ],
    };
  },

  components: {},

  computed: {},

  mounted() {
    this.user_id = this.$route.query.user_id;
  },
};
</script>

<style scoped>
.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
  text-align: center;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}

.title{

  text-align: center;
  color: rgb(11, 236, 161);
  font-size: 30px;
}
.main{
  color: rgb(222, rgb(21, 156, 235), blue);
}
</style>