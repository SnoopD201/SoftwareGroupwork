<!--  -->
<template>
  <div
    class="header1"
  >
    <div
      style="
        width: 500px;
        padding-left: 30px;
        font-weight: bold;
        color: white;
        ont-family: Helvetica Neue;
        font-size: 20px;
      "
    >
      火车售票系统
    </div>
    <div style="flex: 1; margin-left: 600px; color: white">
      {{ nowDate }},{{ nowTime }}
    </div>

    <el-avatar
      :src="'data:image/png;base64,' + image"
      :size="45"
      :fit="fit"
    ></el-avatar>
    <el-button
      @click="this.dialogVisible=true"
      type="primary"
      style="height: 40px; margin-top: 6px; margin-left: 3px"
      >更换头像</el-button
    >
    <el-dialog
      title="更换头像"
      v-model="dialogVisible"
      width="25%"
      :before-close="handleClose"
    >
      <div>
        <el-upload
          class="avatar-uploader"
          action="/api/user/toUpdateAvatar"
          :data="{ user_id:this.user_id }"
          :show-file-list="false"
          accept=".jpg,.png"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
        >
          <img v-if="imageUrl" :src="imageUrl" class="avatar" />

          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </div>
      <div style="margin-left: 40%">
        <el-button @click="dialogVisible = false" type="primary"
          >取 消</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";
import { ElMessage, ElMessageBox } from "element-plus";
export default {
  name: "Header",
  data() {
    return {
      nowDate: null, //存放年月日变量
      nowTime: null, //存放时分秒变量
      timer: "", //定义一个定时器的变量
      currentTime: new Date(), // 获取当前时间
      image: "",
      fit: "contain",
      dialogVisible: false,
      imageUrl: "",
      user_id: "",
    };
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer); // 在Vue实例销毁前，清除定时器
    }
  },
  components: {},
  mounted() {
    this.user_id = this.$route.query.user_id;
    this.getProfile();
  },
  computed: {},
  created() {
    this.timer = setInterval(this.getTime, 1000);
  },
  methods: {
    getTime() {
      const date = new Date();
      const year = date.getFullYear();
      const month = date.getMonth() + 1;
      const day = date.getDate();
      const hour = date.getHours();
      const minute = date.getMinutes();
      const second = date.getSeconds();
      const str = "";
      if (this.hour > 12) {
        this.hour -= 12;
        this.str = " PM";
      } else {
        this.str = " AM";
      }
      this.month = check(month);
      this.day = check(day);
      this.hour = check(hour);
      this.minute = check(minute);
      this.second = check(second);
      function check(i) {
        const num = i < 10 ? "0" + i : i;
        return num;
      }
      this.nowDate = year + "年" + this.month + "月" + this.day + "日";
      this.nowTime =
        this.hour + ":" + this.minute + ":" + this.second + this.str;
    },
    changeprofile() {
    this.dialogVisible=true;
    },
    handleAvatarSuccess(res, file) {
      this.imageUrl = URL.createObjectURL(file.raw);

      this.dialogVisible = false;
      this.getProfile();
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg";
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error("上传头像图片只能是 JPG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isJPG && isLt2M;
    },
    getProfile() {
      //获得头像
      axios.get(`/api/user/profile?user_id=${this.user_id}`).then((res) => {
        this.image = res.data.profile;
      });
    },
    change() {
      this.$router.push({
        path: "/person",
        query: {
          username: this.username,
        },
      });
    },
    handleClose(done) {
      this.$confirm("确认关闭？")
        .then((_) => {
          done();
        })
        .catch((_) => {});
    },
  },
};
</script>
<style >
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
.header1{
        height: 60px;
      line-height: 50px;
      border-bottom: 1px solid #ccc;
      display: flex;

      background: #555555;
  background-image: url("../assets/images/banner10.jpg");
  background-attachment: fixed;
  background-size: 30%,10%;
}

</style>
