<template>
  <div class="card">
    <div class="card-title">仪器档案管理</div>
    
    <div class="search-form">
      <el-form-item label="仪器编码">
        <el-input v-model="searchForm.deviceCode" placeholder="请输入仪器编码"></el-input>
      </el-form-item>
      <el-form-item label="仪器名称">
        <el-input v-model="searchForm.deviceName" placeholder="请输入仪器名称"></el-input>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="searchForm.status" placeholder="请选择状态">
          <el-option label="在用" value="IN_USE"></el-option>
          <el-option label="维修中" value="REPAIRING"></el-option>
          <el-option label="已报废" value="SCRAP"></el-option>
        </el-select>
      </el-form-item>
      <el-button type="primary" @click="handleSearch">查询</el-button>
      <el-button @click="resetSearch">重置</el-button>
    </div>
    
    <div class="btn-group" v-if="canManage">
      <el-button type="primary" @click="openAddDialog">添加仪器</el-button>
      <el-button type="danger" @click="handleBatchDelete">批量删除</el-button>
    </div>
    
    <div class="table-container">
      <el-table :data="deviceList" border style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" v-if="canManage"></el-table-column>
        <el-table-column prop="id" label="ID"></el-table-column>
        <el-table-column prop="deviceCode" label="仪器编码"></el-table-column>
        <el-table-column prop="deviceName" label="仪器名称"></el-table-column>
        <el-table-column prop="specification" label="规格型号"></el-table-column>
        <el-table-column prop="supplier" label="供应商"></el-table-column>
        <el-table-column prop="purchasePrice" label="采购价格"></el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="nextCalibrationDate" label="下次校准日期"></el-table-column>
        <el-table-column label="操作" v-if="canManage">
          <template #default="scope">
            <el-button type="text" @click="openEditDialog(scope.row)">编辑</el-button>
            <el-button type="text" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
  </div>
  
  <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px">
    <el-form :model="form" :rules="formRules" ref="formRef" label-width="100px">
      <el-form-item label="仪器编码" prop="deviceCode">
        <el-input v-model="form.deviceCode"></el-input>
      </el-form-item>
      <el-form-item label="仪器名称" prop="deviceName">
        <el-input v-model="form.deviceName"></el-input>
      </el-form-item>
      <el-form-item label="规格型号">
        <el-input v-model="form.specification"></el-input>
      </el-form-item>
      <el-form-item label="供应商">
        <el-input v-model="form.supplier"></el-input>
      </el-form-item>
      <el-form-item label="采购价格">
        <el-input v-model="form.purchasePrice" type="number"></el-input>
      </el-form-item>
      <el-form-item label="制造商">
        <el-input v-model="form.manufacturer"></el-input>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="form.status">
          <el-option label="在用" value="IN_USE"></el-option>
          <el-option label="维修中" value="REPAIRING"></el-option>
          <el-option label="已报废" value="SCRAP"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="存放位置">
        <el-input v-model="form.location"></el-input>
      </el-form-item>
      <el-form-item label="类别">
        <el-input v-model="form.category"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="handleSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { deviceApi } from '../api'

const deviceList = ref([])
const selectedIds = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const userInfo = ref(JSON.parse(localStorage.getItem('user') || '{}'))
const canManage = computed(() => userInfo.value.role === 'ADMIN')

const searchForm = reactive({
  deviceCode: '',
  deviceName: '',
  status: ''
})

const dialogVisible = ref(false)
const dialogTitle = ref('添加仪器')
const formRef = ref(null)
const form = reactive({
  id: null,
  deviceCode: '',
  deviceName: '',
  specification: '',
  supplier: '',
  purchasePrice: null,
  manufacturer: '',
  status: 'IN_USE',
  location: '',
  category: ''
})

const formRules = {
  deviceCode: [{ required: true, message: '请输入仪器编码', trigger: 'blur' }],
  deviceName: [{ required: true, message: '请输入仪器名称', trigger: 'blur' }]
}

onMounted(() => {
  loadDevices()
})

const loadDevices = async () => {
  try {
    const data = await deviceApi.search(searchForm)
    deviceList.value = data
    total.value = data.length
  } catch (error) {
    ElMessage.error('获取仪器列表失败')
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadDevices()
}

const resetSearch = () => {
  searchForm.deviceCode = ''
  searchForm.deviceName = ''
  searchForm.status = ''
  loadDevices()
}

const handleSelectionChange = (val) => {
  selectedIds.value = val.map(item => item.id)
}

const openAddDialog = () => {
  dialogTitle.value = '添加仪器'
  form.id = null
  form.deviceCode = ''
  form.deviceName = ''
  form.specification = ''
  form.supplier = ''
  form.purchasePrice = null
  form.manufacturer = ''
  form.status = 'IN_USE'
  form.location = ''
  form.category = ''
  dialogVisible.value = true
}

const openEditDialog = (row) => {
  dialogTitle.value = '编辑仪器'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (form.id) {
          await deviceApi.update(form.id, form)
          ElMessage.success('更新成功')
        } else {
          await deviceApi.create(form)
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        loadDevices()
      } catch (error) {
        ElMessage.error(error.response?.data?.message || '操作失败')
      }
    }
  })
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该仪器吗？', '提示', { type: 'warning' })
    await deviceApi.delete(id)
    ElMessage.success('删除成功')
    loadDevices()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleBatchDelete = async () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请选择要删除的仪器')
    return
  }
  try {
    await ElMessageBox.confirm('确定要删除选中的仪器吗？', '提示', { type: 'warning' })
    for (const id of selectedIds.value) {
      await deviceApi.delete(id)
    }
    ElMessage.success('批量删除成功')
    loadDevices()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const getStatusType = (status) => {
  const map = { IN_USE: 'success', REPAIRING: 'warning', SCRAP: 'danger' }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = { IN_USE: '在用', REPAIRING: '维修中', SCRAP: '已报废' }
  return map[status] || status
}

const handleSizeChange = (size) => {
  pageSize.value = size
}

const handleCurrentChange = (page) => {
  currentPage.value = page
}
</script>
