<!--  -->
<template>
  <div>
    <el-empty :image-size="200" />
  </div>
</template>

<script>
export default {
  data() {
    return {
      user_id: "",
    };
  },

  components: {},

  computed: {},
  mounted() {
    this.user_id = this.$route.query.user_id;
  },

  methods: {},
};
</script>
<style lang='scss' scoped>
</style>