<template>
  <div class="my-records-page">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="领用记录" name="use">
        <el-table :data="useRecords" style="width: 100%">
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="deviceName" label="设备名称" width="150" />
          <el-table-column prop="deviceCode" label="设备编号" width="120" />
          <el-table-column prop="projectName" label="项目名称" width="150" />
          <el-table-column prop="useDate" label="领用日期" width="150" />
          <el-table-column prop="returnDate" label="归还日期" width="150" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.status === 'BORROWED' ? 'warning' : 'success'">
                {{ row.status === 'BORROWED' ? '使用中' : '已归还' }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      
      <el-tab-pane label="报修记录" name="repair">
        <el-table :data="repairOrders" style="width: 100%">
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="deviceName" label="设备名称" width="150" />
          <el-table-column prop="deviceCode" label="设备编号" width="120" />
          <el-table-column prop="problemDescription" label="故障描述" width="200" />
          <el-table-column prop="applyDate" label="申请日期" width="150" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRecordApi, repairApi } from '../api'

const activeTab = ref('use')
const useRecords = ref([])
const repairOrders = ref([])

const getStatusType = (status) => {
  const types = { PENDING: 'warning', REPAIRING: 'info', COMPLETED: 'success', REJECTED: 'danger' }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = { PENDING: '待处理', REPAIRING: '维修中', COMPLETED: '已完成', REJECTED: '已拒绝' }
  return texts[status] || status
}

const loadUseRecords = async () => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  try {
    useRecords.value = await useRecordApi.getByUser(user.userId)
  } catch (e) {
    console.error(e)
  }
}

const loadRepairOrders = async () => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  try {
    repairOrders.value = await repairApi.getByUser(user.userId)
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  loadUseRecords()
  loadRepairOrders()
})
</script>