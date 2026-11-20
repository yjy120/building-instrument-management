<template>
  <div class="card">
    <div class="card-title">报修管理</div>
    
    <div class="btn-group" v-if="canSubmit">
      <el-button type="primary" @click="openRepairDialog">提交报修</el-button>
    </div>
    
    <div class="table-container">
      <el-table :data="repairList" border style="width: 100%">
        <el-table-column prop="id" label="ID"></el-table-column>
        <el-table-column prop="deviceId" label="设备ID"></el-table-column>
        <el-table-column prop="deviceName" label="设备名称">
          <template #default="scope">
            {{ getDeviceName(scope.row.deviceId) }}
          </template>
        </el-table-column>
        <el-table-column prop="faultDescription" label="故障描述"></el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="repairDate" label="维修日期"></el-table-column>
        <el-table-column prop="repairCost" label="维修费用"></el-table-column>
        <el-table-column prop="repairComment" label="维修备注"></el-table-column>
        <el-table-column label="操作" v-if="isManager">
          <template #default="scope">
            <el-button v-if="scope.row.status === 'PENDING'" type="text" @click="handleRepair(scope.row)">处理</el-button>
            <el-button v-if="scope.row.status === 'REPAIRING'" type="text" @click="completeRepair(scope.row)">完成</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
  
  <el-dialog title="提交报修" v-model="repairDialogVisible" width="400px">
    <el-form :model="repairForm" :rules="repairRules" ref="repairFormRef" label-width="80px">
      <el-form-item label="设备" prop="deviceId">
        <el-select v-model="repairForm.deviceId">
          <el-option v-for="device in deviceList" :key="device.id" :label="device.deviceName" :value="device.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="故障描述" prop="faultDescription">
        <el-input v-model="repairForm.faultDescription" type="textarea"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="repairDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="handleRepairSubmit">确定</el-button>
    </template>
  </el-dialog>
  
  <el-dialog title="处理维修" v-model="handleDialogVisible" width="400px">
    <el-form :model="handleForm" label-width="80px">
      <el-form-item label="维修费用">
        <el-input v-model="handleForm.repairCost" type="number"></el-input>
      </el-form-item>
      <el-form-item label="维修备注">
        <el-input v-model="handleForm.repairComment" type="textarea"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="handleDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="handleHandleSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { repairApi, deviceApi } from '../api'

const repairList = ref([])
const deviceList = ref([])

const userInfo = ref(JSON.parse(localStorage.getItem('user') || '{}'))
const canSubmit = computed(() => ['ADMIN', 'TESTER'].includes(userInfo.value.role))
const isManager = computed(() => userInfo.value.role === 'MANAGER')

const repairDialogVisible = ref(false)
const repairFormRef = ref(null)
const repairForm = reactive({
  deviceId: null,
  faultDescription: ''
})

const repairRules = {
  deviceId: [{ required: true, message: '请选择设备', trigger: 'change' }],
  faultDescription: [{ required: true, message: '请输入故障描述', trigger: 'blur' }]
}

const handleDialogVisible = ref(false)
const currentRepair = ref(null)
const handleForm = reactive({
  repairCost: null,
  repairComment: ''
})

onMounted(async () => {
  await loadRepairs()
  await loadDevices()
})

const loadRepairs = async () => {
  try {
    const userId = userInfo.value.userId
    if (userInfo.value.role === 'TESTER') {
      repairList.value = await repairApi.getByUser(userId)
    } else {
      repairList.value = await repairApi.getAll()
    }
  } catch (error) {
    ElMessage.error('获取维修工单失败')
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

const openRepairDialog = () => {
  repairForm.deviceId = null
  repairForm.faultDescription = ''
  repairDialogVisible.value = true
}

const handleRepairSubmit = async () => {
  if (!repairFormRef.value) return
  await repairFormRef.value.validate(async (valid) => {
    if (valid) {
      const userId = JSON.parse(localStorage.getItem('user') || '{}').userId || 1
      try {
        await repairApi.create(repairForm, userId)
        ElMessage.success('报修提交成功')
        repairDialogVisible.value = false
        loadRepairs()
      } catch (error) {
        ElMessage.error('提交失败')
      }
    }
  })
}

const handleRepair = (repair) => {
  currentRepair.value = repair
  handleForm.repairCost = null
  handleForm.repairComment = ''
  handleDialogVisible.value = true
}

const handleHandleSubmit = async () => {
  if (!currentRepair.value) return
  try {
    await repairApi.updateStatus(currentRepair.value.id, {
      status: 'REPAIRING',
      comment: handleForm.repairComment
    })
    ElMessage.success('开始维修')
    handleDialogVisible.value = false
    loadRepairs()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const completeRepair = async (repair) => {
  try {
    await repairApi.updateStatus(repair.id, {
      status: 'COMPLETED',
      comment: '维修完成'
    })
    ElMessage.success('维修完成')
    loadRepairs()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const getStatusType = (status) => {
  const map = { PENDING: 'warning', REPAIRING: 'info', COMPLETED: 'success', REJECTED: 'danger' }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = { PENDING: '待处理', REPAIRING: '维修中', COMPLETED: '已完成', REJECTED: '已拒绝' }
  return map[status] || status
}
</script>
