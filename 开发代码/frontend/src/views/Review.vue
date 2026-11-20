<template>
  <div class="review-page">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="采购审批" name="purchase">
        <el-table :data="purchaseApplies" style="width: 100%">
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="deviceName" label="设备名称" width="150" />
          <el-table-column prop="quantity" label="数量" width="80" />
          <el-table-column prop="reason" label="申请理由" width="200" />
          <el-table-column prop="applicantName" label="申请人" width="120" />
          <el-table-column prop="applyDate" label="申请日期" width="150" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template #default="{ row }">
              <el-button v-if="row.status === 'PENDING'" size="small" type="primary" @click="approvePurchase(row)">通过</el-button>
              <el-button v-if="row.status === 'PENDING'" size="small" type="danger" @click="rejectPurchase(row)">驳回</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      
      <el-tab-pane label="报废审批" name="scrap">
        <el-table :data="scrapRecords" style="width: 100%">
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="deviceName" label="设备名称" width="150" />
          <el-table-column prop="deviceCode" label="设备编号" width="120" />
          <el-table-column prop="reason" label="报废原因" width="200" />
          <el-table-column prop="applicantName" label="申请人" width="120" />
          <el-table-column prop="applyDate" label="申请日期" width="150" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template #default="{ row }">
              <el-button v-if="row.status === 'PENDING'" size="small" type="primary" @click="approveScrap(row)">通过</el-button>
              <el-button v-if="row.status === 'PENDING'" size="small" type="danger" @click="rejectScrap(row)">驳回</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      
      <el-tab-pane label="报修处理" name="repair">
        <el-table :data="repairOrders" style="width: 100%">
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="deviceName" label="设备名称" width="150" />
          <el-table-column prop="deviceCode" label="设备编号" width="120" />
          <el-table-column prop="problemDescription" label="故障描述" width="200" />
          <el-table-column prop="applicantName" label="申请人" width="120" />
          <el-table-column prop="applyDate" label="申请日期" width="150" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template #default="{ row }">
              <el-button v-if="row.status === 'PENDING'" size="small" type="primary" @click="handleRepair(row)">处理</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { purchaseApi, scrapApi, repairApi } from '../api'

const activeTab = ref('purchase')
const purchaseApplies = ref([])
const scrapRecords = ref([])
const repairOrders = ref([])

const getStatusType = (status) => {
  const types = { PENDING: 'warning', APPROVED: 'success', REJECTED: 'danger', COMPLETED: 'info' }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = { PENDING: '待审批', APPROVED: '已通过', REJECTED: '已驳回', COMPLETED: '已完成' }
  return texts[status] || status
}

const loadPurchaseApplies = async () => {
  try {
    purchaseApplies.value = await purchaseApi.getPendingApplies()
  } catch (e) {
    console.error(e)
  }
}

const loadScrapRecords = async () => {
  try {
    scrapRecords.value = await scrapApi.getPending()
  } catch (e) {
    console.error(e)
  }
}

const loadRepairOrders = async () => {
  try {
    repairOrders.value = await repairApi.getPending()
  } catch (e) {
    console.error(e)
  }
}

const approvePurchase = async (row) => {
  try {
    await purchaseApi.updateApplyStatus(row.id, { status: 'APPROVED' })
    ElMessage.success('审批通过')
    loadPurchaseApplies()
  } catch (e) {
    ElMessage.error(e.response?.data?.message || '操作失败')
  }
}

const rejectPurchase = async (row) => {
  try {
    await purchaseApi.updateApplyStatus(row.id, { status: 'REJECTED' })
    ElMessage.success('已驳回')
    loadPurchaseApplies()
  } catch (e) {
    ElMessage.error(e.response?.data?.message || '操作失败')
  }
}

const approveScrap = async (row) => {
  try {
    const user = JSON.parse(localStorage.getItem('user') || '{}')
    await scrapApi.review(row.id, { status: 'APPROVED', reviewerId: user.userId })
    ElMessage.success('审批通过')
    loadScrapRecords()
  } catch (e) {
    ElMessage.error(e.response?.data?.message || '操作失败')
  }
}

const rejectScrap = async (row) => {
  try {
    const user = JSON.parse(localStorage.getItem('user') || '{}')
    await scrapApi.review(row.id, { status: 'REJECTED', reviewerId: user.userId })
    ElMessage.success('已驳回')
    loadScrapRecords()
  } catch (e) {
    ElMessage.error(e.response?.data?.message || '操作失败')
  }
}

const handleRepair = async (row) => {
  try {
    await repairApi.updateStatus(row.id, { status: 'PROCESSING' })
    ElMessage.success('已开始处理')
    loadRepairOrders()
  } catch (e) {
    ElMessage.error(e.response?.data?.message || '操作失败')
  }
}

onMounted(() => {
  loadPurchaseApplies()
  loadScrapRecords()
  loadRepairOrders()
})
</script>