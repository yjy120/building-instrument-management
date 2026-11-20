<template>
  <div class="login-container">
    <div class="login-form">
      <h2>建筑检测机构仪器设备管理系统</h2>
      <el-form :model="loginForm" ref="loginFormRef" :rules="rules" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width: 100%" @click="handleLogin">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { authApi } from '../api'

const loginFormRef = ref(null)
const loginForm = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const response = await authApi.login(loginForm)
        localStorage.setItem('token', response.token)
        localStorage.setItem('user', JSON.stringify(response))
        ElMessage.success('登录成功')
        window.location.href = '/'
      } catch (error) {
        ElMessage.error(error.response?.data?.message || '登录失败')
      }
    }
  })
}
</script>
