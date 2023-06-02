
<template>
  <div class="entrance">
    <div class="header">
      <h2>火车售票系统入口</h2>
    </div>
    <div class="content">
      <div
        class="box"
        :class="{ hasClick: hasClick, hasClickOff: !hasClick && goBack }"
      >
        <p class="title">注册入口</p>
        <div class="input_c">
          <div class="shuru">
            <div class="icon">
              <img src="@/assets/images/user.png" alt="" />
            </div>
            <div>
              <input
                v-model="formData.user_id"
                type="text"
                placeholder="请输入身份证"
              />
            </div>
          </div>
          <div class="shuru">
            <div class="icon">
              <img src="@/assets/images/user.png" alt="" />
            </div>
            <div>
              <input
                v-model="formData.name"
                type="text"
                placeholder="请输入姓名"
              />
            </div>
          </div>
          <div class="shuru">
            <div class="icon">
              <img src="@/assets/images/password.png" alt="" />
            </div>
            <div>
              <input
                v-model="formData.password"
                type="password"
                placeholder="请输入密码"
              />
            </div>
          </div>
          <div class="shuru">
            <div class="icon">
              <img src="@/assets/images/password.png" alt="" />
            </div>
            <div>
              <input
                v-model="formData.second_password"
                type="password"
                placeholder="请确认密码"
              />
            </div>
          </div>
          <div class="shuru">
            <!-- 验证码 -->
            <div
              style="width: 200px padding-left:20px"
              class="s-canvas"
              @click="createdCode"
            >
              <canvas
                id="s-canvas"
                :width="contentWidth"
                :height="contentHeight"
              ></canvas>
            </div>
          </div>

          <div class="shuru">
            <div class="icon">
              <img src="@/assets/images/code.png" alt="" />
            </div>

            <div>
              <input
                v-model="formData.identifyCode"
                type="text"
                placeholder="请输入验证码"
              />
            </div>
          </div>
        </div>

        <div class="foot">
          <p class="goin" style="margin-left: 10px" @click="goIn()">登录</p>
          <p class="goin" style="margin-left: 10px" @click="sign()">注册</p>
          <p class="goin" style="margin-left: 10px" @click="forget()">
            忘记密码
          </p>
        </div>
      </div>
      <div
        class="authen"
        :class="{ appear: hasClick, appearOff: !hasClick && goBack }"
      >
        <div class="circle">
          <div class="bg"></div>
        </div>
        <p class="zi">认证中...</p>
      </div>
    </div>
  </div>
</template>
<script>
import axios from "axios";

export default {
  // 验证码
  props: {
    fontSizeMin: {
      type: Number,
      default: 25,
    },
    fontSizeMax: {
      type: Number,
      default: 30,
    },
    backgroundColorMin: {
      type: Number,
      default: 255,
    },
    backgroundColorMax: {
      type: Number,
      default: 255,
    },
    colorMin: {
      type: Number,
      default: 0,
    },
    colorMax: {
      type: Number,
      default: 160,
    },
    lineColorMin: {
      type: Number,
      default: 100,
    },
    lineColorMax: {
      type: Number,
      default: 255,
    },
    dotColorMin: {
      type: Number,
      default: 0,
    },
    dotColorMax: {
      type: Number,
      default: 255,
    },
    contentWidth: {
      type: Number,
      default: 120,
    },
    contentHeight: {
      type: Number,
      default: 34,
    },
  },

  name: "entrance",
  data() {
    return {
      hasClick: false,
      goBack: false,
      formData: {
        user_id: "",
        name: "",
        password: "",
        second_password: "",
        identifyCode: "",
      },
    };
  },
  components: {},
  mounted() {
    this.createdCode();
    this.formData.user_id='';
    this.formData.name="";
  },
  methods: {
    goIn() {
      this.$router.push({
        name: "login",
      });
    },
    forget() {
      this.$router.push({
        name: "forget",
      });
    },
    sign() {
      this.hasClick = true;

      if (
        this.formData.user_id != "" &&
        this.formData.password != "" &&
        this.formData.identifyCode != "" &&
        this.formData.name != "" &&
        this.formData.second_password != ""
      ) {
        if (this.identifyCode != this.formData.identifyCode) {
          setTimeout(() => {
            this.$confirm("请输入正确的验证码！", "提示", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              showCancelButton: false,
              type: "warning",
            }).then(() => {
              this.createdCode();
              this.formData.identifyCode = "";
              this.hasClick = false;
              this.goBack = true;
            });
          }, 1500);
        } else if (this.formData.password != this.formData.second_password) {
          setTimeout(() => {
            this.$confirm("两次输入的密码不一致！", "提示", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              showCancelButton: false,
              type: "warning",
            }).then(() => {
              this.createdCode();
              this.formData.identifyCode = "";
              this.hasClick = false;
              this.goBack = true;
            });
          }, 1500);
        } else if(this.formData.user_id.length!=18){
          setTimeout(() => {
            this.$confirm("用户名长度不正确！", "提示", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              showCancelButton: false,
              type: "warning",
            }).then(() => {
              this.createdCode();
              this.formData.identifyCode = "";
              this.hasClick = false;
              this.goBack = true;
            });
          }, 1500);
        }else {
          setTimeout(() => {
            // 请求后台进行验证...
            axios
              .post("/api/user/sign", {
                user_id: this.formData.user_id,
                name: this.formData.name,
                password: this.formData.password,
              })
              .then((res) => {
                if (res.data == "注册成功") {
                  this.$router.push({
                    name: "login",
                  });
                } else {
                  setTimeout(() => {
                    this.$confirm(res.data, "提示", {
                      confirmButtonText: "确定",
                      cancelButtonText: "取消",
                      showCancelButton: false,
                      type: "warning",
                    }).then(() => {
                                      this.createdCode();
              this.formData.identifyCode = "";
                      this.hasClick = false;
                      this.goBack = true;
                    });
                  }, 1500);
                }
              });
            this.hasClick = false;
          }, 1500);
        }
      } else {
        setTimeout(() => {
          this.$confirm("请输入相关信息！", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            showCancelButton: false,
            type: "warning",
          }).then(() => {
                            this.createdCode();
              this.formData.identifyCode = "";
            this.hasClick = false;
            this.goBack = true;
          });
        }, 1500);
      }
    },
    // 验证码

    // 生成4个随机数
    createdCode() {
      const len = 4;
      const codeList = [];
      const chars = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz0123456789";
      const charsLen = chars.length;
      for (let i = 0; i < len; i++) {
        codeList.push(chars.charAt(Math.floor(Math.random() * charsLen)));
      }
      this.identifyCode = codeList.join("");
      this.$emit("getIdentifyCode", this.identifyCode.toLowerCase());
      this.drawPic();
    },

    // 生成一个随机数
    randomNum(min, max) {
      return Math.floor(Math.random() * (max - min) + min);
    },
    // 生成一个随机的颜色
    randomColor(min, max) {
      const r = this.randomNum(min, max);
      const g = this.randomNum(min, max);
      const b = this.randomNum(min, max);
      return "rgb(" + r + "," + g + "," + b + ")";
    },

    drawPic() {
      const canvas = document.getElementById("s-canvas");
      const ctx = canvas.getContext("2d");
      ctx.textBaseline = "bottom";
      // 绘制背景
      ctx.fillStyle = this.randomColor(
        this.backgroundColorMin,
        this.backgroundColorMax
      );
      ctx.fillRect(0, 0, this.contentWidth, this.contentHeight);
      // 绘制文字
      for (let i = 0; i < this.identifyCode.length; i++) {
        this.drawText(ctx, this.identifyCode[i], i);
      }
      this.drawLine(ctx);
      this.drawDot(ctx);
    },

    drawText(ctx, txt, i) {
      ctx.fillStyle = this.randomColor(this.colorMin, this.colorMax);
      ctx.font =
        this.randomNum(this.fontSizeMin, this.fontSizeMax) + "px SimHei";
      const x = (i + 1) * (this.contentWidth / (this.identifyCode.length + 1));
      const y = this.randomNum(this.fontSizeMax, this.contentHeight - 5);
      var deg = this.randomNum(-45, 45);
      // 修改坐标原点和旋转角度
      ctx.translate(x, y);
      ctx.rotate((deg * Math.PI) / 180);
      ctx.fillText(txt, 0, 0);
      // 恢复坐标原点和旋转角度
      ctx.rotate((-deg * Math.PI) / 180);
      ctx.translate(-x, -y);
    },

    // 绘制干扰线
    drawLine(ctx) {
      for (let i = 0; i < 5; i++) {
        ctx.strokeStyle = this.randomColor(
          this.lineColorMin,
          this.lineColorMax
        );
        ctx.beginPath();
        ctx.moveTo(
          this.randomNum(0, this.contentWidth),
          this.randomNum(0, this.contentHeight)
        );
        ctx.lineTo(
          this.randomNum(0, this.contentWidth),
          this.randomNum(0, this.contentHeight)
        );
        ctx.stroke();
      }
    },

    // 绘制干扰点
    drawDot(ctx) {
      for (let i = 0; i < 80; i++) {
        ctx.fillStyle = this.randomColor(0, 255);
        ctx.beginPath();
        ctx.arc(
          this.randomNum(0, this.contentWidth),
          this.randomNum(0, this.contentHeight),
          1,
          0,
          2 * Math.PI
        );
        ctx.fill();
      }
    },
  },
};
</script>

<style lang='scss' scoped>
.entrance {
  width: 100%;
  height: 700px;
  background: url("../assets/images/login.jpeg") center no-repeat;
  background-size: 100% 150%;
  overflow: hidden;
  .header {
    width: 100%;
    height: 12%;
    margin-top: 3%;
    // background: url("../assets/images/login.jpeg") center no-repeat;
    background-size: 100% 150%;
    h2 {
      font-size: r(32);
      color: #d8ecff;
      text-align: center;
      line-height: r(95);
    }
  }
  .content {
    width: calc(100% - 30px);
    height: r(600);
    padding: 15px;
    display: flex;
    justify-content: center;
    align-items: center;
    perspective: 800;
    -webkit-perspective: 800;
    position: relative;
    .box {
      width: r(360);
      height: r(400);
      background: linear-gradient(
        230deg,
        rgba(53, 57, 74, 0) 0%,
        rgb(0, 0, 0) 100%
      );
      box-shadow: -15px 15px 15px rgba(6, 17, 47, 70%);
      transition: all 1s;
      &.hasClick {
        animation: animal1 1s linear 1 forwards;
        pointer-events: none;
      }
      &.hasClickOff {
        animation: animal11 1s linear 1 forwards;
        pointer-events: auto;
      }
      .title {
        color: #f4f4fc;
        font-size: r(20);
        line-height: r(60);
        text-align: center;
        margin-top: r(30);
      }
      .input_c {
        margin-top: r(20);
        .shuru {
          width: r(360);
          height: r(75);
          display: flex;
          justify-content: center;
          align-items: center;
          color: #f4f4fc;
          .icon img {
            width: r(26);
            height: r(26);
          }
          input {
            width: r(240);
            height: r(35);
            margin-top: -2px;
            background: rgba(57, 61, 82, 0);
            left: 0;
            padding: 2px 5px;
            border-top: 2px solid rgba(57, 61, 82, 0);
            border-bottom: 2px solid rgba(57, 61, 82, 0);
            border-right: none;
            border-left: none;
            outline: none;
            font-family: "Microsoft Yahei", sans-serif;
            box-shadow: none;
            color: #61bfff !important;
          }
          input::-webkit-input-placeholder {
            color: #c5c5c9;
          }
          input::-moz-placeholder {
            //Mozilla Firefox 4 to 18
            color: #c5c5c9;
          }
          input::-moz-placeholder {
            //Mozilla Firefox 19+
            color: #c5c5c9;
          }
          input::-ms-input-placeholder {
            //Internet Explorer 10+
            color: #c5c5c9;
          }
        }
      }
      .foot {
        width: 100%;
        height: r(50);
        display: flex;
        justify-content: center;
        align-items: center;
        margin-top: r(10);
        .goin {
          width: r(200);
          height: r(40);
          margin-top: r(50);
          line-height: r(40);
          border-radius: r(25);
          background: transparent;
          border: 2px solid #4fa1d9;
          color: #4fa1d9;
          text-align: center;
          font-size: r(20);
          cursor: pointer;
          &:hover {
            background: #4fa1d9;
            color: #fff;
          }
        }
      }
    }
    .authen {
      width: 240px;
      height: 120px;
      position: absolute;
      z-index: inherit;
      top: 55%;
      left: 50%;
      margin-left: -100px;
      background: linear-gradient(
        230deg,
        rgba(53, 57, 74, 0) 0%,
        rgb(0, 0, 0) 100%
      );
      box-shadow: -15px 15px 15px rgba(6, 17, 47, 70%);
      transition: all 1s;
      transform: scale(0);
      &.appear {
        animation: animal2 1s linear 1 forwards;
      }
      &.appearOff {
        animation: animal22 1s linear 1 forwards;
      }
      .circle {
        width: 100%;
        height: 60px;
        display: flex;
        justify-content: center;
        align-items: center;
        margin-top: 10px;
        .bg {
          width: 50px;
          height: 50px;
          background: url("../assets/images/logo.png") center no-repeat;
          background-size: 100% 100%;
          animation: animal 1s infinite linear;
        }
      }
      .zi {
        font-size: r(20);
        color: #fff;
        text-align: center;
        line-height: 40px;
      }
    }
  }
}
@keyframes animal {
  0% {
    transform: rotate(0deg);
    -ms-transform: rotate(0deg);
    -webkit-transform: rotate(0deg);
  }
  100% {
    transform: rotate(-360deg);
    -ms-transform: rotate(-360deg);
    -webkit-transform: rotate(-360deg);
  }
}
@keyframes animal1 {
  0% {
    transform: rotateX(0deg);
  }
  50% {
    transform: rotateX(70deg);
    transform-origin: center bottom;
  }
  100% {
    transform: rotateX(70deg) translateX(-200px) scale(0.8);
    transform-origin: center bottom;
  }
}
@keyframes animal11 {
  0% {
    transform: rotateX(70deg) translateX(-200px) scale(0.8);
    transform-origin: center bottom;
  }
  50% {
    transform: rotateX(0deg);
    transform-origin: center bottom;
  }
  100% {
    transform: rotateX(0deg) translateX(0) scale(1);
    transform-origin: center bottom;
  }
}
@keyframes animal2 {
  0% {
    transform: scale(0);
  }
  50% {
    transform: scale(1);
  }
  100% {
    transform: scale(1) translateX(100px);
  }
}
@keyframes animal22 {
  0% {
    transform: scale(1) translateX(100px);
  }
  50% {
    transform: scale(1) translateX(0);
  }
  100% {
    transform: scale(0) translateX(0);
  }
}
</style>