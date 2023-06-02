<!--  -->
<template>
  <div>
      火车售票系统入口
        <el-rate calss="score"
    v-model="value"
    disabled
    show-score
    text-color="#ff9900"
    score-template="{value} points"
  />
  </div>
</template>

<script>

export default {
    name:"Footer",
  data () {
    return {
      value:4.8,
      user_id:""
    };
  },

  components: {},

  computed: {},

  mounted() {
    this.user_id = this.$route.query.user_id;
  },
  methods: {}
}

</script>
<style lang='scss' scoped>
.footer{
    width: 100%;
    height: 30px;
    background-color: rgb(30, 187, 235);
    text-align: center;
}

</style>