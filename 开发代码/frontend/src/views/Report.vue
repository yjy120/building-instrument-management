<template>
  <div>
    <div class="card" style="display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px;">
      <div class="stat-card">
        <div class="stat-value">{{ statistics.device?.totalCount || 0 }}</div>
        <div class="stat-label">设备总数</div>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
        <div class="stat-value">{{ statistics.device?.largeCount || 0 }}</div>
        <div class="stat-label">大型设备</div>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
        <div class="stat-value">{{ statistics.device?.smallCount || 0 }}</div>
        <div class="stat-label">小型设备</div>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
        <div class="stat-value">{{ statistics.device?.borrowedCount || 0 }}</div>
        <div class="stat-label">借用中</div>
      </div>
    </div>
    
    <div class="card" style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px;">
      <div>
        <div class="card-title">设备状态统计</div>
        <el-table :data="statusList" border style="width: 100%">
          <el-table-column prop="name" label="状态"></el-table-column>
          <el-table-column prop="value" label="数量"></el-table-column>
          <el-table-column prop="percent" label="占比">
            <template #default="scope">
              <el-progress :percentage="scope.row.percent" :color="scope.row.color" />
            </template>
          </el-table-column>
        </el-table>
      </div>
      
      <div>
        <div class="card-title">设备类型占比</div>
        <div style="display: flex; justify-content: center; align-items: center; height: 200px;">
          <el-progress type="circle" :percentage="largePercent" :color="largeColor" :stroke-width="15" style="width: 150px;" />
          <el-progress type="circle" :percentage="smallPercent" :color="smallColor" :stroke-width="15" style="width: 150px; margin-left: 20px;" />
        </div>
        <div style="display: flex; justify-content: center;">
          <el-tag type="primary" style="margin-right: 20px;">大型设备 {{ largePercent }}%</el-tag>
          <el-tag type="success">小型设备 {{ smallPercent }}%</el-tag>
        </div>
      </div>
    </div>
    
    <div class="card" style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px;">
      <div>
        <div class="card-title">报修统计</div>
        <el-table :data="repairList" border style="width: 100%">
          <el-table-column prop="name" label="状态"></el-table-column>
          <el-table-column prop="value" label="数量"></el-table-column>
        </el-table>
      </div>
      
      <div>
        <div class="card-title">报废统计</div>
        <el-table :data="scrapList" border style="width: 100%">
          <el-table-column prop="name" label="状态"></el-table-column>
          <el-table-column prop="value" label="数量"></el-table-column>
        </el-table>
      </div>
    </div>
    
    <div class="card">
      <div class="card-title">设备分类统计</div>
      <el-table :data="categoryList" border style="width: 100%">
        <el-table-column prop="name" label="分类"></el-table-column>
        <el-table-column prop="value" label="数量"></el-table-column>
        <el-table-column prop="percent" label="占比">
          <template #default="scope">
            <el-progress :percentage="scope.row.percent" />
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { reportApi } from '../api'

const statistics = ref({})

const statusList = computed(() => {
  const device = statistics.value.device || {}
  const total = device.totalCount || 1
  return [
    { name: '在用', value: device.inUseCount || 0, percent: Math.round(((device.inUseCount || 0) / total) * 100), color: '#67c23a' },
    { name: '维修中', value: device.repairingCount || 0, percent: Math.round(((device.repairingCount || 0) / total) * 100), color: '#409eff' },
    { name: '借用中', value: device.borrowedCount || 0, percent: Math.round(((device.borrowedCount || 0) / total) * 100), color: '#e6a23c' },
    { name: '闲置', value: device.idleCount || 0, percent: Math.round(((device.idleCount || 0) / total) * 100), color: '#909399' }
  ]
})

const largePercent = computed(() => {
  const device = statistics.value.device || {}
  const total = device.totalCount || 1
  return Math.round(((device.largeCount || 0) / total) * 100)
})

const smallPercent = computed(() => {
  return 100 - largePercent.value
})

const largeColor = computed(() => {
  return `#667eea`
})

const smallColor = computed(() => {
  return `#f093fb`
})

const repairList = computed(() => {
  const repair = statistics.value.repair || {}
  return [
    { name: '待审批', value: repair.pendingRepairCount || 0 },
    { name: '维修中', value: repair.repairingRepairCount || 0 },
    { name: '已完成', value: repair.completedRepairCount || 0 },
    { name: '已拒绝', value: repair.rejectedRepairCount || 0 }
  ]
})

const scrapList = computed(() => {
  const scrap = statistics.value.scrap || {}
  return [
    { name: '待审批', value: scrap.pendingScrapCount || 0 },
    { name: '已批准', value: scrap.approvedScrapCount || 0 },
    { name: '已拒绝', value: scrap.rejectedScrapCount || 0 }
  ]
})

const categoryList = computed(() => {
  const device = statistics.value.device || {}
  const categoryStats = device.categoryStats || {}
  const total = device.totalCount || 1
  return Object.keys(categoryStats).map(name => ({
    name,
    value: categoryStats[name],
    percent: Math.round((categoryStats[name] / total) * 100)
  }))
})

const loadStatistics = async () => {
  try {
    statistics.value = await reportApi.getAll()
  } catch (e) {
    console.error('获取统计数据失败', e)
  }
}

onMounted(() => {
  loadStatistics()
})
</script>
