<template>
  <div class="card">
    <div class="card-title">校准管理</div>
    
    <div class="btn-group" v-if="canManage">
      <el-button type="primary" @click="openCalibrationDialog">录入校准记录</el-button>
    </div>
    
    <div class="table-container">
      <el-table :data="calibrationList" border style="width: 100%">
        <el-table-column prop="id" label="ID"></el-table-column>
        <el-table-column prop="deviceId" label="设备ID"></el-table-column>
        <el-table-column prop="deviceName" label="设备名称">
          <template #default="scope">
            {{ getDeviceName(scope.row.deviceId) }}
          </template>
        </el-table-column>
        <el-table-column prop="calibrationDate" label="校准日期"></el-table-column>
        <el-table-column prop="nextCalibrationDate" label="下次校准日期"></el-table-column>
        <el-table-column prop="calibrationOrganization" label="校准机构"></el-table-column>
        <el-table-column prop="result" label="校准结果">
          <template #default="scope">
            <el-tag :type="scope.row.result === 'PASS' ? 'success' : 'danger'">
              {{ scope.row.result === 'PASS' ? '合格' : '不合格' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="certificateUrl" label="证书链接"></el-table-column>
      </el-table>
    </div>
  </div>
  
  <el-dialog title="录入校准记录" v-model="calibrationDialogVisible" width="500px">
    <el-form :model="calibrationForm" :rules="calibrationRules" ref="calibrationFormRef" label-width="100px">
      <el-form-item label="设备" prop="deviceId">
        <el-select v-model="calibrationForm.deviceId">
          <el-option v-for="device in deviceList" :key="device.id" :label="device.deviceName" :value="device.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="校准日期" prop="calibrationDate">
        <el-date-picker v-model="calibrationForm.calibrationDate" type="date"></el-date-picker>
      </el-form-item>
      <el-form-item label="下次校准日期" prop="nextCalibrationDate">
        <el-date-picker v-model="calibrationForm.nextCalibrationDate" type="date"></el-date-picker>
      </el-form-item>
      <el-form-item label="校准机构">
        <el-input v-model="calibrationForm.calibrationOrganization"></el-input>
      </el-form-item>
      <el-form-item label="校准结果" prop="result">
        <el-select v-model="calibrationForm.result">
          <el-option label="合格" value="PASS"></el-option>
          <el-option label="不合格" value="FAIL"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="证书链接">
        <el-input v-model="calibrationForm.certificateUrl"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="calibrationDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="handleCalibrationSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { calibrationApi, deviceApi } from '../api'

const calibrationList = ref([])
const deviceList = ref([])

const userInfo = ref(JSON.parse(localStorage.getItem('user') || '{}'))
const canManage = computed(() => userInfo.value.role === 'ADMIN')

const calibrationDialogVisible = ref(false)
const calibrationFormRef = ref(null)
const calibrationForm = reactive({
  deviceId: null,
  calibrationDate: null,
  nextCalibrationDate: null,
  calibrationOrganization: '',
  result: 'PASS',
  certificateUrl: ''
})

const calibrationRules = {
  deviceId: [{ required: true, message: '请选择设备', trigger: 'change' }],
  calibrationDate: [{ required: true, message: '请选择校准日期', trigger: 'change' }],
  result: [{ required: true, message: '请选择校准结果', trigger: 'change' }]
}

onMounted(async () => {
  await loadCalibrations()
  await loadDevices()
})

const loadCalibrations = async () => {
  try {
    calibrationList.value = await calibrationApi.getAll()
  } catch (error) {
    ElMessage.error('获取校准记录失败')
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

const openCalibrationDialog = () => {
  calibrationForm.deviceId = null
  calibrationForm.calibrationDate = null
  calibrationForm.nextCalibrationDate = null
  calibrationForm.calibrationOrganization = ''
  calibrationForm.result = 'PASS'
  calibrationForm.certificateUrl = ''
  calibrationDialogVisible.value = true
}

const handleCalibrationSubmit = async () => {
  if (!calibrationFormRef.value) return
  await calibrationFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await calibrationApi.create(calibrationForm)
        ElMessage.success('校准记录录入成功')
        calibrationDialogVisible.value = false
        loadCalibrations()
      } catch (error) {
        ElMessage.error('录入失败')
      }
    }
  })
}
</script>
