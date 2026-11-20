<template>
  <div>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="采购申请" name="apply">
        <div class="card">
          <div class="btn-group" v-if="canManage">
            <el-button type="primary" @click="openApplyDialog">新建申请</el-button>
          </div>
          
          <div class="table-container">
            <el-table :data="applyList" border style="width: 100%">
              <el-table-column prop="id" label="ID"></el-table-column>
              <el-table-column prop="deviceName" label="设备名称"></el-table-column>
              <el-table-column prop="quantity" label="数量"></el-table-column>
              <el-table-column prop="projectUsage" label="用途"></el-table-column>
              <el-table-column prop="status" label="状态">
                <template #default="scope">
                  <el-tag :type="getStatusType(scope.row.status)">
                    {{ getStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="reviewComment" label="审批意见"></el-table-column>
              <el-table-column prop="createTime" label="申请时间"></el-table-column>
              <el-table-column label="操作" v-if="isManager">
                <template #default="scope">
                  <el-button v-if="scope.row.status === 'PENDING'" type="text" @click="reviewApply(scope.row)">审批</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </el-tab-pane>
      
      <el-tab-pane label="采购汇总" name="total" v-if="isManager">
        <div class="card">
          <div class="btn-group">
            <el-button type="primary" @click="createTotal">创建汇总</el-button>
          </div>
          
          <div class="table-container">
            <el-table :data="totalList" border style="width: 100%">
              <el-table-column prop="id" label="ID"></el-table-column>
              <el-table-column prop="totalList" label="汇总列表"></el-table-column>
              <el-table-column prop="status" label="状态">
                <template #default="scope">
                  <el-tag :type="getStatusType(scope.row.status)">
                    {{ getStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="reviewComment" label="审批意见"></el-table-column>
              <el-table-column prop="createTime" label="创建时间"></el-table-column>
              <el-table-column label="操作">
                <template #default="scope">
                  <el-button v-if="scope.row.status === 'PENDING'" type="text" @click="reviewTotal(scope.row)">审批</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
  
  <el-dialog title="新建采购申请" v-model="applyDialogVisible" width="400px">
    <el-form :model="applyForm" :rules="applyRules" ref="applyFormRef" label-width="80px">
      <el-form-item label="设备名称" prop="deviceName">
        <el-input v-model="applyForm.deviceName"></el-input>
      </el-form-item>
      <el-form-item label="数量" prop="quantity">
        <el-input v-model="applyForm.quantity" type="number"></el-input>
      </el-form-item>
      <el-form-item label="用途" prop="projectUsage">
        <el-input v-model="applyForm.projectUsage"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="applyDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="handleApplySubmit">确定</el-button>
    </template>
  </el-dialog>
  
  <el-dialog title="审批申请" v-model="reviewDialogVisible" width="400px">
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
import { purchaseApi } from '../api'

const activeTab = ref('apply')
const applyList = ref([])
const totalList = ref([])

const userInfo = ref(JSON.parse(localStorage.getItem('user') || '{}'))
const canManage = computed(() => userInfo.value.role === 'ADMIN')
const isManager = computed(() => userInfo.value.role === 'MANAGER')

const applyDialogVisible = ref(false)
const applyFormRef = ref(null)
const applyForm = reactive({
  deviceName: '',
  quantity: 1,
  projectUsage: ''
})

const applyRules = {
  deviceName: [{ required: true, message: '请输入设备名称', trigger: 'blur' }],
  quantity: [{ required: true, message: '请输入数量', trigger: 'blur' }]
}

const reviewDialogVisible = ref(false)
const currentApply = ref(null)
const reviewForm = reactive({
  status: 'APPROVED',
  comment: ''
})

onMounted(async () => {
  await loadApplies()
  await loadTotals()
})

const loadApplies = async () => {
  try {
    applyList.value = await purchaseApi.getAllApplies()
  } catch (error) {
    ElMessage.error('获取采购申请失败')
  }
}

const loadTotals = async () => {
  try {
    totalList.value = await purchaseApi.getAllTotals()
  } catch (error) {
    ElMessage.error('获取采购汇总失败')
  }
}

const openApplyDialog = () => {
  applyForm.deviceName = ''
  applyForm.quantity = 1
  applyForm.projectUsage = ''
  applyDialogVisible.value = true
}

const handleApplySubmit = async () => {
  if (!applyFormRef.value) return
  await applyFormRef.value.validate(async (valid) => {
    if (valid) {
      const userId = JSON.parse(localStorage.getItem('user') || '{}').userId || 1
      try {
        await purchaseApi.createApply(applyForm, userId)
        ElMessage.success('申请成功')
        applyDialogVisible.value = false
        loadApplies()
      } catch (error) {
        ElMessage.error('申请失败')
      }
    }
  })
}

const reviewApply = (apply) => {
  currentApply.value = apply
  reviewForm.status = 'APPROVED'
  reviewForm.comment = ''
  reviewDialogVisible.value = true
}

const handleReviewSubmit = async () => {
  if (!currentApply.value) return
  try {
    await purchaseApi.updateApplyStatus(currentApply.value.id, {
      status: reviewForm.status,
      comment: reviewForm.comment
    })
    ElMessage.success('审批成功')
    reviewDialogVisible.value = false
    loadApplies()
  } catch (error) {
    ElMessage.error('审批失败')
  }
}

const createTotal = async () => {
  const pendingApplies = applyList.value.filter(a => a.status === 'PENDING')
  if (pendingApplies.length === 0) {
    ElMessage.warning('没有待审批的申请')
    return
  }
  try {
    await purchaseApi.createTotal(pendingApplies.map(a => a.id))
    ElMessage.success('汇总成功')
    loadTotals()
  } catch (error) {
    ElMessage.error('汇总失败')
  }
}

const reviewTotal = (total) => {
  currentApply.value = total
  reviewForm.status = 'APPROVED'
  reviewForm.comment = ''
  reviewDialogVisible.value = true
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
