<template>
  <div class="user-page">
    <div class="page-header">
      <h2>用户管理</h2>
      <el-button type="primary" @click="showAddDialog">新增用户</el-button>
    </div>
    
    <el-table :data="users" style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" width="120" />
      <el-table-column prop="realName" label="姓名" width="120" />
      <el-table-column prop="role" label="角色" width="120">
        <template #default="{ row }">
          <span>{{ roleText(row.role) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="department" label="部门" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '正常' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button size="small" @click="showEditDialog(row)" :disabled="!canEditUser(row)">编辑</el-button>
          <el-button size="small" type="warning" @click="toggleStatus(row)" :disabled="!canEditUser(row)">
            {{ row.status === 1 ? '停用' : '启用' }}
          </el-button>
          <el-button size="small" type="danger" @click="deleteUser(row)" :disabled="!canDeleteUser(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑用户' : '新增用户'" width="500">
      <el-form :model="form" label-width="80">
        <el-form-item label="用户名" v-if="!isEdit">
          <el-input v-model="form.username" />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="form.realName" />
        </el-form-item>
        <el-form-item label="角色" v-if="!isEdit">
          <el-select v-model="form.role" :disabled="!canSelectRole">
            <el-option label="项目检测员" value="TESTER" />
            <el-option label="设备管理员" value="ADMIN" v-if="userInfo.role === 'MANAGER'" />
            <el-option label="试验室主管" value="MANAGER" v-if="userInfo.role === 'MANAGER'" />
          </el-select>
        </el-form-item>
        <el-form-item label="部门">
          <el-input v-model="form.department" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="不修改请留空" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const users = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = ref({
  username: '',
  realName: '',
  role: 'TESTER',
  department: '',
  phone: '',
  email: '',
  password: ''
})

const userInfo = ref(JSON.parse(localStorage.getItem('user') || '{}'))
const token = localStorage.getItem('token')

const roleText = (role) => {
  const map = { ADMIN: '设备管理员', MANAGER: '试验室主管', TESTER: '项目检测员' }
  return map[role] || role
}

// ADMIN只能管理TESTER，MANAGER可以管理所有人
const canEditUser = (user) => {
  if (userInfo.value.role === 'MANAGER') return true
  if (userInfo.value.role === 'ADMIN' && user.role === 'TESTER') return true
  return false
}

const canDeleteUser = (user) => {
  if (user.id === userInfo.value.id) return false // 不能删除自己
  return canEditUser(user)
}

const canSelectRole = computed(() => {
  // MANAGER可以选择任何角色，ADMIN只能选择TESTER
  return userInfo.value.role === 'MANAGER'
})

const loadUsers = async () => {
  try {
    const res = await axios.get('/api/user', {
      headers: { Authorization: `Bearer ${token}` }
    })
    users.value = res.data
  } catch (e) {
    ElMessage.error('加载用户列表失败')
  }
}

const showAddDialog = () => {
  isEdit.value = false
  form.value = {
    username: '',
    realName: '',
    role: userInfo.value.role === 'MANAGER' ? 'TESTER' : 'TESTER',
    department: '',
    phone: '',
    email: '',
    password: ''
  }
  dialogVisible.value = true
}

const showEditDialog = (user) => {
  isEdit.value = true
  form.value = {
    id: user.id,
    username: user.username,
    realName: user.realName,
    role: user.role,
    department: user.department,
    phone: user.phone,
    email: user.email,
    password: ''
  }
  dialogVisible.value = true
}

const submitForm = async () => {
  try {
    if (isEdit.value) {
      await axios.put(`/api/user/${form.value.id}`, form.value, {
        headers: { Authorization: `Bearer ${token}` }
      })
      ElMessage.success('修改成功')
    } else {
      if (!form.value.username || !form.value.password) {
        ElMessage.warning('请填写用户名和密码')
        return
      }
      await axios.post('/api/user', form.value, {
        headers: { Authorization: `Bearer ${token}` }
      })
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadUsers()
  } catch (e) {
    ElMessage.error(e.response?.data?.message || '操作失败')
  }
}

const toggleStatus = async (user) => {
  try {
    await axios.put(`/api/user/${user.id}/toggle-status`, null, {
      headers: { Authorization: `Bearer ${token}` }
    })
    ElMessage.success('状态已更新')
    loadUsers()
  } catch (e) {
    ElMessage.error(e.response?.data?.message || '操作失败')
  }
}

const deleteUser = async (user) => {
  try {
    await ElMessageBox.confirm('确定删除该用户？', '提示', { type: 'warning' })
    await axios.delete(`/api/user/${user.id}`, {
      headers: { Authorization: `Bearer ${token}` }
    })
    ElMessage.success('删除成功')
    loadUsers()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error(e.response?.data?.message || '删除失败')
    }
  }
}

onMounted(loadUsers)
</script>

<style scoped>
.user-page {
  padding: 20px;
}
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
</style>