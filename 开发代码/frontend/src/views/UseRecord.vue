<template>
  <div class="card">
    <div class="card-title">领用记录</div>
    
    <div class="btn-group" v-if="canSubmit">
      <el-button type="primary" @click="openBorrowDialog">领用仪器</el-button>
    </div>
    
    <div class="table-container">
      <el-table :data="useRecordList" border style="width: 100%">
        <el-table-column prop="id" label="ID"></el-table-column>
        <el-table-column prop="deviceId" label="设备ID"></el-table-column>
        <el-table-column prop="deviceName" label="设备名称">
          <template #default="scope">
            {{ getDeviceName(scope.row.deviceId) }}
          </template>
        </el-table-column>
        <el-table-column prop="projectName" label="项目名称"></el-table-column>
        <el-table-column prop="borrowTime" label="领用时间"></el-table-column>
        <el-table-column prop="returnTime" label="归还时间"></el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'BORROWED' ? 'warning' : 'success'">
              {{ scope.row.status === 'BORROWED' ? '借用中' : '已归还' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" v-if="canSubmit">
          <template #default="scope">
            <el-button v-if="scope.row.status === 'BORROWED'" type="text" @click="handleReturn(scope.row)">归还</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
  
  <el-dialog title="领用仪器" v-model="borrowDialogVisible" width="400px">
    <el-form :model="borrowForm" :rules="borrowRules" ref="borrowFormRef" label-width="80px">
      <el-form-item label="设备" prop="deviceId">
        <el-select v-model="borrowForm.deviceId">
          <el-option v-for="device in availableDevices" :key="device.id" :label="device.deviceName" :value="device.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="项目名称" prop="projectName">
        <el-input v-model="borrowForm.projectName"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="borrowDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="handleBorrow">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { useRecordApi, deviceApi } from '../api'

const useRecordList = ref([])
const deviceList = ref([])

const userInfo = ref(JSON.parse(localStorage.getItem('user') || '{}'))
const canSubmit = computed(() => ['ADMIN', 'TESTER'].includes(userInfo.value.role))

const borrowDialogVisible = ref(false)
const borrowFormRef = ref(null)
const borrowForm = reactive({
  deviceId: null,
  projectName: ''
})

const borrowRules = {
  deviceId: [{ required: true, message: '请选择设备', trigger: 'change' }],
  projectName: [{ required: true, message: '请输入项目名称', trigger: 'blur' }]
}

const availableDevices = computed(() => {
  const borrowedIds = useRecordList.value
    .filter(r => r.status === 'BORROWED')
    .map(r => r.deviceId)
  return deviceList.value.filter(d => !borrowedIds.includes(d.id))
})

onMounted(async () => {
  await loadRecords()
  await loadDevices()
})

const loadRecords = async () => {
  try {
    const userId = userInfo.value.userId
    if (userInfo.value.role === 'TESTER') {
      useRecordList.value = await useRecordApi.getByUser(userId)
    } else {
      useRecordList.value = await useRecordApi.getAll()
    }
  } catch (error) {
    ElMessage.error('获取领用记录失败')
  }
}

const loadDevices = async () => {
  try {
    deviceList.value = await deviceApi.getActive()
  } catch (error) {
    console.error('获取设备列表失败', error)
  }
}

const getDeviceName = (deviceId) => {
  const device = deviceList.value.find(d => d.id === deviceId)
  return device ? device.deviceName : '未知'
}

const openBorrowDialog = () => {
  borrowForm.deviceId = null
  borrowForm.projectName = ''
  borrowDialogVisible.value = true
}

const handleBorrow = async () => {
  if (!borrowFormRef.value) return
  await borrowFormRef.value.validate(async (valid) => {
    if (valid) {
      const userId = JSON.parse(localStorage.getItem('user') || '{}').userId || 1
      try {
        await useRecordApi.create(borrowForm, userId)
        ElMessage.success('领用成功')
        borrowDialogVisible.value = false
        loadRecords()
      } catch (error) {
        ElMessage.error('领用失败')
      }
    }
  })
}

const handleReturn = async (record) => {
  try {
    await useRecordApi.returnDevice(record.id)
    ElMessage.success('归还成功')
    loadRecords()
  } catch (error) {
    ElMessage.error('归还失败')
  }
}
</script>
