<template>
  <h2 style="margin-bottom: 16px">用户登录</h2>
  <div id="userLoginView">
    <a-row>
      <a-form
        style="max-width: 480px; margin: 0 auto"
        label-align="left"
        auto-label-width
        :model="form"
        @submit="handleSubmit"
      >
        <a-form-item field="userAccount" tooltip="userAccount" label="账号">
          <a-input v-model="form.userAccount" placeholder="请输入账号" />
        </a-form-item>
        <a-form-item
          field="userPassword"
          tooltip="userPassword 长度不少于8位"
          label="密码"
        >
          <a-input-password
            v-model="form.userPassword"
            placeholder="请输入密码"
          />
        </a-form-item>

        <a-col>
          <a-button style="width: 128px" href="/user/register"> 注册</a-button>
          <a-button
            html-type="submit"
            type="primary"
            style="width: 128px; float: right"
          >
            登录
          </a-button>
        </a-col>
      </a-form>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { reactive } from "vue";
import { UserControllerService, UserLoginRequest } from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import user from "@/store/user";

const router = useRouter();
const store = useStore();

/**
 * 表单信息
 */
const form = reactive({
  userAccount: "",
  userPassword: "",
} as UserLoginRequest);

/**
 * 提交表单
 */
const handleSubmit = async () => {
  const res = await UserControllerService.loginUsingPost(form);
  if (res.code === 0) {
    // alert("登陆成功");
    await store.dispatch("user/getLoginUser");
    router.push({
      path: "/",
      replace: true,
    });
  } else {
    message.error("登陆失败：" + res.message);
  }
};
</script>

<style>
#userLoginView {
  justify-content: center;
  align-content: center;
  display: flex;
}
</style>
