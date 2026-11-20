<template>
  <div class="card">
    <div class="card-title">库存管理</div>
    
    <div class="search-form">
      <el-form-item label="仪器编码">
        <el-input v-model="searchForm.deviceCode" placeholder="请输入仪器编码"></el-input>
      </el-form-item>
      <el-form-item label="仪器名称">
        <el-input v-model="searchForm.deviceName" placeholder="请输入仪器名称"></el-input>
      </el-form-item>
      <el-button type="primary" @click="handleSearch">查询</el-button>
      <el-button @click="resetSearch">重置</el-button>
    </div>
    
    <div class="table-container">
      <el-table :data="stockList" border style="width: 100%">
        <el-table-column prop="id" label="ID"></el-table-column>
        <el-table-column prop="deviceId" label="设备ID"></el-table-column>
        <el-table-column prop="deviceName" label="设备名称">
          <template #default="scope">
            {{ getDeviceName(scope.row.deviceId) }}
          </template>
        </el-table-column>
        <el-table-column prop="quantity" label="库存数量"></el-table-column>
        <el-table-column prop="warehouseLocation" label="库位"></el-table-column>
        <el-table-column label="操作" v-if="canManage">
          <template #default="scope">
            <el-button type="text" @click="openAdjustDialog(scope.row)">调整库存</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
  
  <div class="card">
    <div class="card-title">库存调整记录</div>
    <div class="table-container">
      <el-table :data="stockRecords" border style="width: 100%">
        <el-table-column prop="id" label="ID"></el-table-column>
        <el-table-column prop="deviceId" label="设备ID"></el-table-column>
        <el-table-column prop="type" label="类型">
          <template #default="scope">
            <el-tag :type="scope.row.type === 'IN' ? 'success' : 'warning'">
              {{ scope.row.type === 'IN' ? '入库' : '出库' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="quantity" label="数量"></el-table-column>
        <el-table-column prop="reason" label="原因"></el-table-column>
        <el-table-column prop="createTime" label="操作时间"></el-table-column>
      </el-table>
    </div>
  </div>
  
  <el-dialog title="调整库存" v-model="adjustDialogVisible" width="400px">
    <el-form :model="adjustForm" label-width="80px">
      <el-form-item label="调整数量">
        <el-input v-model="adjustForm.delta" type="number"></el-input>
        <span style="color: #666; font-size: 12px;">正数入库，负数出库</span>
      </el-form-item>
      <el-form-item label="调整原因">
        <el-input v-model="adjustForm.reason"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="adjustDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="handleAdjust">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { stockApi, deviceApi } from '../api'

const stockList = ref([])
const stockRecords = ref([])
const deviceList = ref([])

const userInfo = ref(JSON.parse(localStorage.getItem('user') || '{}'))
const canManage = computed(() => userInfo.value.role === 'ADMIN')

const searchForm = reactive({
  deviceCode: '',
  deviceName: ''
})

const adjustDialogVisible = ref(false)
const currentStock = ref(null)
const adjustForm = reactive({
  delta: 0,
  reason: ''
})

onMounted(async () => {
  await loadStocks()
  await loadDevices()
  await loadStockRecords()
})

const loadStocks = async () => {
  try {
    stockList.value = await stockApi.getAll()
  } catch (error) {
    ElMessage.error('获取库存列表失败')
  }
}

const loadDevices = async () => {
  try {
    deviceList.value = await deviceApi.getAll()
  } catch (error) {
    console.error('获取设备列表失败', error)
  }
}

const loadStockRecords = async () => {
  try {
    stockRecords.value = []
    for (const stock of stockList.value) {
      const records = await stockApi.getRecords(stock.deviceId)
      stockRecords.value = [...stockRecords.value, ...records]
    }
  } catch (error) {
    console.error('获取库存记录失败', error)
  }
}

const getDeviceName = (deviceId) => {
  const device = deviceList.value.find(d => d.id === deviceId)
  return device ? device.deviceName : '未知'
}

const handleSearch = () => {
  loadStocks()
}

const resetSearch = () => {
  searchForm.deviceCode = ''
  searchForm.deviceName = ''
  loadStocks()
}

const openAdjustDialog = (stock) => {
  currentStock.value = stock
  adjustForm.delta = 0
  adjustForm.reason = ''
  adjustDialogVisible.value = true
}

const handleAdjust = async () => {
  if (!currentStock.value) return
  const userId = JSON.parse(localStorage.getItem('user') || '{}').userId || 1
  try {
    await stockApi.adjust({
      deviceId: currentStock.value.deviceId,
      delta: adjustForm.delta,
      reason: adjustForm.reason,
      operatorId: userId
    })
    ElMessage.success('库存调整成功')
    adjustDialogVisible.value = false
    loadStocks()
    loadStockRecords()
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '调整失败')
  }
}
</script>
