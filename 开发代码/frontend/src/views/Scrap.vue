<template>
  <div class="card">
    <div class="card-title">报废管理</div>
    
    <div class="btn-group" v-if="canManage">
      <el-button type="primary" @click="openScrapDialog">申请报废</el-button>
    </div>
    
    <div class="table-container">
      <el-table :data="scrapList" border style="width: 100%">
        <el-table-column prop="id" label="ID"></el-table-column>
        <el-table-column prop="deviceId" label="设备ID"></el-table-column>
        <el-table-column prop="deviceName" label="设备名称">
          <template #default="scope">
            {{ getDeviceName(scope.row.deviceId) }}
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="报废原因"></el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reviewComment" label="审批意见"></el-table-column>
        <el-table-column prop="scrapDate" label="报废日期"></el-table-column>
        <el-table-column label="操作" v-if="isManager">
          <template #default="scope">
            <el-button v-if="scope.row.status === 'PENDING'" type="text" @click="reviewScrap(scope.row)">审批</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
  
  <el-dialog title="申请报废" v-model="scrapDialogVisible" width="400px">
    <el-form :model="scrapForm" :rules="scrapRules" ref="scrapFormRef" label-width="80px">
      <el-form-item label="设备" prop="deviceId">
        <el-select v-model="scrapForm.deviceId">
          <el-option v-for="device in deviceList" :key="device.id" :label="device.deviceName" :value="device.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="报废原因" prop="reason">
        <el-input v-model="scrapForm.reason" type="textarea"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="scrapDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="handleScrapSubmit">确定</el-button>
    </template>
  </el-dialog>
  
  <el-dialog title="审批报废" v-model="reviewDialogVisible" width="400px">
    <el-form :model="reviewForm" label-width="80px">
      <el-form-item label="审批结果">
        <el-select v-model="reviewForm.status">
          <el-option label="通过" value="APPROVED"></el-option>
          <el-option label="拒绝" value="REJECTED"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="审批意见">
        <el-input v-model="reviewForm.comment" type="textarea"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="reviewDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="handleReviewSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { scrapApi, deviceApi } from '../api'

const scrapList = ref([])
const deviceList = ref([])

const userInfo = ref(JSON.parse(localStorage.getItem('user') || '{}'))
const canManage = computed(() => userInfo.value.role === 'ADMIN')
const isManager = computed(() => userInfo.value.role === 'MANAGER')

const scrapDialogVisible = ref(false)
const scrapFormRef = ref(null)
const scrapForm = reactive({
  deviceId: null,
  reason: ''
})

const scrapRules = {
  deviceId: [{ required: true, message: '请选择设备', trigger: 'change' }],
  reason: [{ required: true, message: '请输入报废原因', trigger: 'blur' }]
}

const reviewDialogVisible = ref(false)
const currentScrap = ref(null)
const reviewForm = reactive({
  status: 'APPROVED',
  comment: ''
})

onMounted(async () => {
  await loadScraps()
  await loadDevices()
})

const loadScraps = async () => {
  try {
    scrapList.value = await scrapApi.getAll()
  } catch (error) {
    ElMessage.error('获取报废记录失败')
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

const openScrapDialog = () => {
  scrapForm.deviceId = null
  scrapForm.reason = ''
  scrapDialogVisible.value = true
}

const handleScrapSubmit = async () => {
  if (!scrapFormRef.value) return
  await scrapFormRef.value.validate(async (valid) => {
    if (valid) {
      const userId = JSON.parse(localStorage.getItem('user') || '{}').userId || 1
      try {
        await scrapApi.create(scrapForm, userId)
        ElMessage.success('报废申请提交成功')
        scrapDialogVisible.value = false
        loadScraps()
      } catch (error) {
        ElMessage.error('提交失败')
      }
    }
  })
}

const reviewScrap = (scrap) => {
  currentScrap.value = scrap
  reviewForm.status = 'APPROVED'
  reviewForm.comment = ''
  reviewDialogVisible.value = true
}

const handleReviewSubmit = async () => {
  if (!currentScrap.value) return
  const userId = JSON.parse(localStorage.getItem('user') || '{}').userId || 1
  try {
    await scrapApi.review(currentScrap.value.id, {
      status: reviewForm.status,
      comment: reviewForm.comment,
      reviewerId: userId
    })
    ElMessage.success('审批成功')
    reviewDialogVisible.value = false
    loadScraps()
    loadDevices()
  } catch (error) {
    ElMessage.error('审批失败')
  }
}

const getStatusType = (status) => {
  const map = { PENDING: 'warning', APPROVED: 'success', REJECTED: 'danger' }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = { PENDING: '待审批', APPROVED: '已通过', REJECTED: '已拒绝' }
  return map[status] || status
}
</script>
