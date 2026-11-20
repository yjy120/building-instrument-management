<template>
  <div class="main-container">
    <aside class="sidebar">
      <div class="sidebar-header">设备管理系统</div>
      <el-menu class="sidebar-menu" :default-active="activeMenu" background-color="#001529" text-color="#fff" active-text-color="#409EFF" @select="handleMenuSelect">
        <el-menu-item index="/dashboard">
          <el-icon><HomeFilled /></el-icon>
          <span>仪表盘</span>
        </el-menu-item>
        
        <!-- 设备管理员菜单 -->
        <template v-if="userInfo.role === 'ADMIN'">
          <el-menu-item index="/device">
            <el-icon><Monitor /></el-icon>
            <span>仪器档案管理</span>
          </el-menu-item>
          <el-menu-item index="/stock">
            <el-icon><Grid /></el-icon>
            <span>库存管理</span>
          </el-menu-item>
          <el-menu-item index="/purchase">
            <el-icon><ShoppingCart /></el-icon>
            <span>采购录入</span>
          </el-menu-item>
          <el-menu-item index="/use">
            <el-icon><User /></el-icon>
            <span>领用管理</span>
          </el-menu-item>
          <el-menu-item index="/calibration">
            <el-icon><Calendar /></el-icon>
            <span>校准管理</span>
          </el-menu-item>
          <el-menu-item index="/issue-handle">
            <el-icon><Tools /></el-icon>
            <span>异常申请处理</span>
          </el-menu-item>
          <el-menu-item index="/user">
            <el-icon><Setting /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
        </template>
        
        <!-- 试验室主管菜单 -->
        <template v-if="userInfo.role === 'MANAGER'">
          <el-menu-item index="/review">
            <el-icon><List /></el-icon>
            <span>审批管理</span>
          </el-menu-item>
          <el-menu-item index="/report">
            <el-icon><Document /></el-icon>
            <span>报表统计</span>
          </el-menu-item>
          <el-menu-item index="/user">
            <el-icon><Setting /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
        </template>
        
        <!-- 项目检测员菜单 -->
        <template v-if="userInfo.role === 'TESTER'">
          <el-menu-item index="/use">
            <el-icon><User /></el-icon>
            <span>领用申请</span>
          </el-menu-item>
          <el-menu-item index="/issue-report">
            <el-icon><Tools /></el-icon>
            <span>异常申请</span>
          </el-menu-item>
          <el-menu-item index="/my-records">
            <el-icon><Document /></el-icon>
            <span>我的记录</span>
          </el-menu-item>
        </template>
      </el-menu>
    </aside>
    <div class="content-area">
      <header class="top-header">
        <span class="header-title">{{ currentPageTitle }}</span>
        <div class="user-info">
          <span>{{ userInfo.realName }} ({{ roleText }})</span>
          <el-button type="text" @click="handleLogout">退出登录</el-button>
        </div>
      </header>
      <main class="page-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { HomeFilled, Monitor, Grid, ShoppingCart, User, Calendar, Tools, Delete, Setting, List, Document } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

const activeMenu = computed(() => route.path)

const userInfo = ref(JSON.parse(localStorage.getItem('user') || '{}'))

const roleText = computed(() => {
  const roleMap = {
    ADMIN: '设备管理员',
    MANAGER: '试验室主管',
    TESTER: '项目检测员'
  }
  return roleMap[userInfo.value.role] || userInfo.value.role
})

const currentPageTitle = computed(() => {
  const titleMap = {
    '/dashboard': '仪表盘',
    '/device': '仪器档案管理',
    '/stock': '库存管理',
    '/purchase': '采购流程',
    '/use': '领用记录',
    '/calibration': '校准管理',
    '/repair': '报修管理',
    '/scrap': '报废管理',
    '/user': '用户管理',
    '/review': '审批管理',
    '/my-records': '我的记录',
    '/issue-report': '设备异常申请',
    '/issue-handle': '异常申请处理',
    '/report': '报表统计'
  }
  return titleMap[route.path] || '设备管理系统'
})

const handleMenuSelect = (index) => {
  router.push(index)
}

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  ElMessage.success('退出成功')
  router.push('/login')
}
</script>
