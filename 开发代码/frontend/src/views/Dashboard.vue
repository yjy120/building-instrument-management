<template>
  <div>
    <div class="card" style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px;">
      <div class="stat-card">
        <div class="stat-value">{{ overview.totalDevices || 0 }}</div>
        <div class="stat-label">仪器总数</div>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);">
        <div class="stat-value">{{ overview.activeDevices || 0 }}</div>
        <div class="stat-label">在用仪器</div>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #fc4a1a 0%, #f7b733 100%);">
        <div class="stat-value">{{ overview.borrowedDevices || 0 }}</div>
        <div class="stat-label">借用中</div>
      </div>
    </div>
    
    <div class="card" style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px;">
      <div>
        <div class="card-title">待处理事项</div>
        <el-table :data="pendingItems" border style="width: 100%">
          <el-table-column prop="type" label="类型"></el-table-column>
          <el-table-column prop="count" label="数量"></el-table-column>
          <el-table-column prop="action" label="操作">
            <template #default="scope">
              <el-button type="primary" link @click="goToPage(scope.row.page)">查看</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      
      <div>
        <div class="card-title">即将到期校准</div>
        <el-table :data="calibrationDueList" border style="width: 100%">
          <el-table-column prop="deviceCode" label="仪器编码"></el-table-column>
          <el-table-column prop="deviceName" label="仪器名称"></el-table-column>
          <el-table-column prop="nextCalibrationDate" label="下次校准日期"></el-table-column>
        </el-table>
      </div>
    </div>
    
    <div class="card">
      <div class="card-title">最近领用记录</div>
      <el-table :data="recentUseRecords" border style="width: 100%">
        <el-table-column prop="deviceId" label="设备ID"></el-table-column>
        <el-table-column prop="projectName" label="项目名称"></el-table-column>
        <el-table-column prop="borrowTime" label="领用时间"></el-table-column>
        <el-table-column prop="status" label="状态"></el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { dashboardApi } from '../api'

const router = useRouter()

const overview = ref({})
const calibrationDueList = ref([])
const recentUseRecords = ref([])

const pendingItems = ref([
  { type: '采购申请', count: 0, page: '/purchase' },
  { type: '维修工单', count: 0, page: '/repair' },
  { type: '报废申请', count: 0, page: '/scrap' },
  { type: '校准到期', count: 0, page: '/calibration' }
])

onMounted(async () => {
  try {
    overview.value = await dashboardApi.getOverview()
    calibrationDueList.value = await dashboardApi.getCalibrationDueList()
    
    const records = await dashboardApi.getRecentRecords()
    recentUseRecords.value = records.recentUse || []
    
    pendingItems.value = [
      { type: '采购申请', count: overview.value.pendingPurchase || 0, page: '/purchase' },
      { type: '维修工单', count: overview.value.pendingRepair || 0, page: '/repair' },
      { type: '报废申请', count: overview.value.pendingScrap || 0, page: '/scrap' },
      { type: '校准到期', count: overview.value.calibrationDue || 0, page: '/calibration' }
    ]
  } catch (error) {
    console.error('获取仪表盘数据失败', error)
  }
})

const goToPage = (page) => {
  router.push(page)
}
</script>
