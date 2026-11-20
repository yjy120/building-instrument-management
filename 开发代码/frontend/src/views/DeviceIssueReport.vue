<template>
  <div>
    <div class="card">
      <div class="card-title">设备异常申请</div>
      <el-form :model="form" label-width="100px" style="width: 100%;">
        <el-form-item label="选择设备" required style="width: 100%;">
          <el-select v-model="form.deviceId" placeholder="请选择设备" style="width: 100%;">
            <el-option v-for="device in devices" :key="device.id" :label="device.deviceName + ' (' + device.deviceCode + ')'" :value="device.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="故障描述" required style="width: 100%;">
          <textarea v-model="form.faultDescription" rows="4" placeholder="请详细描述设备故障情况" style="width: 100%; min-height: 120px; padding: 10px; border: 1px solid #dcdfe6; border-radius: 4px; resize: vertical;"></textarea>
        </el-form-item>
        <el-form-item style="width: 100%;">
          <el-button type="primary" @click="submitReport">提交异常申请</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <div class="card">
      <div class="card-title">我的异常申请记录</div>
      <el-table :data="reports" border style="width: 100%">
        <el-table-column prop="deviceId" label="设备ID"></el-table-column>
        <el-table-column prop="faultDescription" label="故障描述"></el-table-column>
        <el-table-column prop="deviceType" label="设备类型">
          <template #default="scope">
            <el-tag :type="scope.row.deviceType === 'LARGE' ? 'primary' : 'success'">
              {{ scope.row.deviceType === 'LARGE' ? '大型设备' : '小型设备' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="handleType" label="处理方式">
          <template #default="scope">
            <span v-if="scope.row.handleType === 'REPAIR'">报修</span>
            <span v-else-if="scope.row.handleType === 'SCRAP'">报废</span>
            <span v-else>待处理</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="申请时间"></el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { deviceApi, issueReportApi } from '../api'

const form = ref({
  deviceId: '',
  faultDescription: ''
})

const devices = ref([])
const reports = ref([])

const getStatusType = (status) => {
  const types = { PENDING: 'warning', PROCESSED: 'success' }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = { PENDING: '待处理', PROCESSED: '已处理' }
  return texts[status] || status
}

const loadDevices = async () => {
  try {
    devices.value = await deviceApi.getActive()
  } catch (e) {
    console.error(e)
  }
}

const loadReports = async () => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  try {
    reports.value = await issueReportApi.getByUser(user.userId)
  } catch (e) {
    console.error(e)
  }
}

const submitReport = async () => {
  if (!form.value.deviceId || !form.value.faultDescription) {
    alert('请填写完整信息')
    return
  }
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  try {
    await issueReportApi.create(form.value, user.userId)
    alert('提交成功')
    form.value = { deviceId: '', faultDescription: '' }
    loadReports()
  } catch (e) {
    alert('提交失败: ' + (e.response?.data?.message || e.message || '未知错误'))
  }
}

onMounted(() => {
  loadDevices()
  loadReports()
})
</script>
