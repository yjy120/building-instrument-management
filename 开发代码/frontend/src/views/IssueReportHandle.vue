<template>
  <div>
    <div class="card">
      <div class="card-title">异常申请处理</div>
      <el-table :data="reports" border style="width: 100%">
        <el-table-column prop="id" label="申请ID"></el-table-column>
        <el-table-column prop="deviceId" label="设备名称">
          <template #default="scope">
            {{ getDeviceName(scope.row.deviceId) }}
          </template>
        </el-table-column>
        <el-table-column prop="deviceType" label="设备类型">
          <template #default="scope">
            <el-tag :type="scope.row.deviceType === 'LARGE' ? 'primary' : 'success'" size="large">
              {{ scope.row.deviceType === 'LARGE' ? '大型设备' : '小型设备' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="faultDescription" label="故障描述"></el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'PENDING' ? 'warning' : 'success'">
              {{ scope.row.status === 'PENDING' ? '待处理' : '已处理' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="handleType" label="处理方式">
          <template #default="scope">
            <span v-if="scope.row.handleType === 'REPAIR'">报修</span>
            <span v-else-if="scope.row.handleType === 'SCRAP'">报废</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="申请时间"></el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <template v-if="scope.row.status === 'PENDING'">
              <el-button type="primary" @click="handleAsRepair(scope.row.id)">提交报修</el-button>
              <el-button type="danger" @click="handleAsScrap(scope.row.id)">提交报废</el-button>
            </template>
            <span v-else>已处理</span>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { deviceApi, issueReportApi } from '../api'

const reports = ref([])
const devices = ref([])

const getDeviceName = (deviceId) => {
  const device = devices.value.find(d => d.id === deviceId)
  return device ? device.deviceName + ' (' + device.deviceCode + ')' : '未知设备'
}

const loadDevices = async () => {
  try {
    devices.value = await deviceApi.getAll()
  } catch (e) {
    console.error(e)
  }
}

const loadReports = async () => {
  try {
    reports.value = await issueReportApi.getPending()
  } catch (e) {
    console.error(e)
  }
}

const handleAsRepair = async (id) => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  try {
    await issueReportApi.handleRepair(id, user.userId)
    alert('已提交报修审批')
    loadReports()
  } catch (e) {
    alert('操作失败: ' + e.response?.data?.message || e.message)
  }
}

const handleAsScrap = async (id) => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  try {
    await issueReportApi.handleScrap(id, user.userId)
    alert('已提交报废审批')
    loadReports()
  } catch (e) {
    alert('操作失败: ' + e.response?.data?.message || e.message)
  }
}

onMounted(() => {
  loadDevices()
  loadReports()
})
</script>
