<template>
  <div style="height: 390px">
    <el-descriptions
      class="margin-top"
      title="个人简介"
      :column="3"
      :size="size"
      border
    >
      <template #extra>
        <el-button type="primary" @click="visible = true"
          >修改个人信息</el-button
        >
        <el-button type="primary" @click="this.alipayvisible = true"
          >充值</el-button
        >
      </template>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon :style="iconStyle">
              <user />
            </el-icon>
            身份证
          </div>
        </template>
        {{ this.user.user_id }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon :style="iconStyle">
              <iphone />
            </el-icon>
            手机号码
          </div>
        </template>
        {{ this.user.phone }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon :style="iconStyle">
              <user />
            </el-icon>
            姓名
          </div>
        </template>
        {{ this.user.name }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon><Money /></el-icon>
            账户余额
          </div>
        </template>
        {{ this.user.money }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon><Tickets /></el-icon>
            证件类型
          </div>
        </template>
        {{ this.user.id_type }}
      </el-descriptions-item>
    </el-descriptions>

    <div style="margin: 20px" />
    <el-dialog
      v-model="visible"
      title="修改个人信息"
      width="30%"
      :before-close="handleClose"
    >
      <el-form
        :label-position="labelPosition"
        label-width="100px"
        :model="formLabelAlign"
        style="max-width: 460px"
      >
        <el-form-item label="身份证">
          <el-input v-model="form.user_id" disabled />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="手机号码">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="证件类型">
          <el-input v-model="form.id_type" disabled />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="visible = false">取消</el-button>
          <el-button
            type="primary"
            @click="changePerson()"
            style="margin-left: 120px"
            >修改</el-button
          >
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-descriptions
      class="margin-top"
      title="常用乘车人"
      :column="3"
      :size="size"
      border
    >
      <template #extra>
        <el-button type="primary" @click="dialogVisible = true"
          >添加乘车人</el-button
        >
      </template>

      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon><Tickets /></el-icon>
            购买人信息
          </div>
        </template>
        {{ this.user.user_id }}
      </el-descriptions-item>
    </el-descriptions>

    <!-- 乘车人 -->
    <el-scrollbar height="300px">
      <el-table :data="tableData" style="width: 100%">
        <el-table-column label="身份证" width="250">
          <template #default="scope">
            <div style="display: flex; align-items: center">
              <span style="margin-left: 10px">{{ scope.row.second_user }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="姓名" width="100">
          <template #default="scope">
            <div style="display: flex; align-items: center">
              <span style="margin-left: 10px">{{ scope.row.name }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="证件类型" width="200">
          <template #default="scope">
            <div style="display: flex; align-items: center">
              <span style="margin-left: 10px">{{ scope.row.id_type }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="手机号码" width="200">
          <template #default="scope">
            <div style="display: flex; align-items: center">
              <span style="margin-left: 10px">{{ scope.row.phone }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="票种" width="100">
          <template #default="scope">
            <div style="display: flex; align-items: center">
              <span style="margin-left: 10px">{{ scope.row.ticket_type }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="删除">
          <template #default="scope">
            <el-button
              size="small"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)"
              >Delete</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-scrollbar>

    <!-- 添加乘车人 -->
    <div style="margin: 20px" />
    <el-dialog
      v-model="dialogVisible"
      title="添加乘车人"
      width="30%"
      :before-close="handleClose"
    >
      <el-form
        :label-position="labelPosition"
        label-width="100px"
        :model="diaForm"
        style="max-width: 460px"
      >
        <el-form-item label="身份证">
          <el-input v-model="diaForm.second_user" />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="diaForm.name" />
        </el-form-item>
        <el-form-item label="手机号码">
          <el-input v-model="diaForm.phone" />
        </el-form-item>
        <el-form-item label="证件类型">
          <el-select
            v-model="this.diaForm.id_type"
            size="large"
            placeholder="证件类型"
          >
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="票种">
          <el-select
            v-model="this.diaForm.ticket_type"
            size="large"
            placeholder="票种"
          >
            <el-option
              v-for="item in ticketoptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="dialogVisible = false"
            >取消</el-button
          >
          <el-button
            type="primary"
            @click="addUser()"
            style="margin-left: 120px"
            >添加</el-button
          >
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-dialog v-model="alipayvisible" title="充值">
      <el-input v-model="alipay_money" placeholder="请选择你要充值的金额" />
      <el-button type="primary" @click="this.alipayvisible = false"
        >取消</el-button
      >
      <el-button type="primary" @click="addMoney()">充值</el-button>
    </el-dialog>
  </div>
</template>

<script>
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from "element-plus";
import axios from "axios";
export default {
  name: "MyPerson",
  data() {
    return {
      user_id: "",
      user: {},
      visible: false,
      form: {},
      tableData: [],
      dialogVisible: false,
      diaForm: {
        second_user: null,
        name: null,
        phone: null,
        id_type: null,
        ticket_type: null,
      },
      options: [
        { value: "中国居民身份证", label: "中国居民身份证" },
        { value: "港澳居民来往内地通行证", label: "港澳居民来往内地通行证" },
        { value: "台湾居民来往大陆通行证", label: "台湾居民来往大陆通行证" },
        { value: "护照", label: "护照" },
        { value: "外国人永久居留身份证", label: "外国人永久居留身份证" },
        { value: "港澳台居民居住证", label: "港澳台居民居住证" },
      ],
      ticketoptions: [
        {
          value: "成人票",
          label: "成人票",
        },
        {
          value: "儿童票",
          label: "儿童票",
        },
        {
          value: "学生票",
          label: "学生票",
        },
      ],
      alipay_money: "",
      alipayvisible: false,
    };
  },
  mounted() {
    this.user_id = this.$route.query.user_id;
    this.getUser();
    this.getForm();
    this.getSecondUser();
  },
  methods: {
    addMoney() {
      var reg = /^(([1-9]{1}\d*)|(0{1}))(\.\d{2})$/;
      if (this.alipay_money == "") {
        ElMessage({
          type: "warning",
          message: "请输入要充值的金额",
        });
      } else if (reg.test(this.alipay_money)) {
      window.open("http://localhost:9090/alipay/pay?user_id=" + this.user_id + "&money=" + this.alipay_money)
        
      } else {
        ElMessage({
          type: "warning",
          message: "输入的格式不对",
        });
      }
    },
    addUser() {
      if (
        this.diaForm.second_user == "" ||
        this.diaForm.phone == "" ||
        this.diaForm.id_type == "" ||
        this.diaForm.name == "" ||
        this.diaForm.ticket_type == ""
      ) {
        ElMessage({
          type: "warning",
          message: "请填写相关信息",
        });
      } else if (isNaN(this.diaForm.phone)) {
        ElMessage({
          type: "warning",
          message: "输入的手机号码格式不正确",
        });
      } else {
        axios
          .post("/api/user/addSecondUser", {
            user_id: this.user_id,
            second_user: this.diaForm.second_user,
            phone: this.diaForm.phone,
            id_type: this.diaForm.id_type,
            name: this.diaForm.name,
            ticket_type: this.diaForm.ticket_type,
          })
          .then((res) => {
            this.getSecondUser();
            if (res.data == "添加成功") {
              this.dialogVisible = false;
              this.diaForm = {};
            } else {
              ElMessage({
                type: "warning",
                message: res.data,
              });
            }
          });
      }
    },
    handleDelete(index, row) {
      axios
        .post("/api/user/deleteSecondUser", {
          user_id: this.user_id,
          second_user: row.second_user,
        })
        .then((res) => {
          this.getSecondUser();
        });
    },
    getSecondUser() {
      axios
        .post(`/api/user/getSecondUser?user_id=${this.user_id}`, {})
        .then((res) => {
          this.tableData = res.data;
        });
    },
    getUser() {
      axios
        .post(`/api/user/getUser?user_id=${this.user_id}`, {})
        .then((res) => {
          this.user = res.data;
        });
    },
    getForm() {
      axios
        .post(`/api/user/getUser?user_id=${this.user_id}`, {})
        .then((res) => {
          this.form = res.data;
        });
    },
    changePerson() {
      if (
        this.form.name == "" ||
        this.form.phone == "" ||
        this.form.password == ""
      ) {
        ElMessage({
          type: "warning",
          message: "请填写相关信息",
        });
      } else if (this.form.password.length < 6) {
        ElMessage({
          type: "warning",
          message: "密码安全度太低",
        });
      } else if (isNaN(this.form.phone)) {
        ElMessage({
          type: "warning",
          message: "输入的手机号码格式不正确",
        });
      } else {
        axios
          .post("/api/user/changeUser", {
            user_id: this.form.user_id,
            name: this.form.name,
            phone: this.form.phone,
            password: this.form.password,
          })
          .then((res) => {

            ElMessage({
              type: "warning",
              message: res.data,
            });
            if(res.data=="修改成功"){
              this.getUser();
            }
          });
      }
      this.getUser();
    },
  },
};
</script>

<style scoped>
.el-descriptions {
  margin-top: 20px;
}
.cell-item {
  display: flex;
  align-items: center;
}
.margin-top {
  margin-top: 20px;
}
</style>
