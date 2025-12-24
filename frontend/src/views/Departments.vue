<template>
  <div class="departments-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">
            <el-icon><OfficeBuilding /></el-icon>
            部门管理
          </span>
          <div class="header-actions">
            <el-input
              v-model="searchForm.searchKey"
              placeholder="输入部门名称搜索"
              style="width: 250px; margin-right: 10px"
              clearable
              @clear="handleSearch"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
            <el-button type="primary" @click="handleSearch">
              <el-icon><Search /></el-icon>
              查询
            </el-button>
            <el-button type="success" @click="handleAdd">
              <el-icon><Plus /></el-icon>
              新增部门
            </el-button>
          </div>
        </div>
      </template>

      <el-table
        v-loading="loading"
        :data="tableData"
        stripe
        style="width: 100%"
        border
      >
        <el-table-column prop="id" label="部门ID" width="100" />
        <el-table-column prop="depName" label="部门名称" width="150" />
        <el-table-column label="负责人" width="120">
          <template #default="{ row }">
            {{ row.managerName || '未设置' }}
          </template>
        </el-table-column>
        <el-table-column label="员工数量" width="100">
          <template #default="{ row }">
            <el-tag type="info">{{ row.employeeCount || 0 }}人</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="员工列表" min-width="200">
          <template #default="{ row }">
            <el-tag
              v-for="(emp, index) in row.employeeList"
              :key="index"
              type="success"
              style="margin-right: 5px; margin-bottom: 5px"
            >
              {{ emp.empName || `员工${emp.empId}` }}
            </el-tag>
            <span v-if="!row.employeeList || row.employeeList.length === 0" class="text-muted">无</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.pageNum"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="700px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="120px"
      >
        <el-form-item label="部门ID" prop="id">
          <el-input
            v-model="formData.id"
            :disabled="!!formData.id"
            placeholder="新增时留空自动生成"
          />
        </el-form-item>
        
        <el-form-item label="部门名称" prop="depName">
          <el-input v-model="formData.depName" placeholder="请输入部门名称" />
        </el-form-item>

        <el-form-item label="负责人">
          <el-select
            v-model="formData.managerId"
            placeholder="请选择负责人"
            clearable
            filterable
            style="width: 100%"
          >
            <el-option
              v-for="emp in allEmployees"
              :key="emp._id"
              :label="emp.empName"
              :value="emp._id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="部门员工">
          <el-select
            v-model="formData.employeeIds"
            multiple
            placeholder="请选择部门员工"
            filterable
            style="width: 100%"
          >
            <el-option
              v-for="emp in allEmployees"
              :key="emp._id"
              :label="emp.empName"
              :value="emp._id"
            />
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {onMounted, reactive, ref} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import {Delete, Edit, OfficeBuilding, Plus, Search} from '@element-plus/icons-vue'
import {deleteDepartment, saveDepartment} from '@/api/department'
import {getAllEmployees} from '@/api/employee'

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增部门')
const formRef = ref(null)

const searchForm = reactive({
  searchKey: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref([])
const allEmployees = ref([])

const formData = reactive({
  id: null,
  depName: '',
  managerId: null,
  employeeIds: []
})

const formRules = {
  depName: [
    { required: true, message: '请输入部门名称', trigger: 'blur' }
  ]
}

const loadData = async () => {
  loading.value = true
  try {
    const params = {
      searchKey: searchForm.searchKey || undefined,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    }
    
    // 注意：需要后端提供REST API，当前使用模拟数据
    ElMessage.warning('请先在后端添加REST API接口')
    tableData.value = []
    pagination.total = 0
    
  } catch (error) {
    ElMessage.error('加载数据失败：' + error.message)
  } finally {
    loading.value = false
  }
}

const loadEmployees = async () => {
  try {
    allEmployees.value = await getAllEmployees() || []
  } catch (error) {
    console.error('加载员工列表失败:', error)
  }
}

const handleSearch = () => {
  pagination.pageNum = 1
  loadData()
}

const handleSizeChange = (size) => {
  pagination.pageSize = size
  pagination.pageNum = 1
  loadData()
}

const handlePageChange = (page) => {
  pagination.pageNum = page
  loadData()
}

const handleAdd = () => {
  dialogTitle.value = '新增部门'
  Object.assign(formData, {
    id: null,
    depName: '',
    managerId: null,
    employeeIds: []
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑部门'
  Object.assign(formData, {
    id: row.id,
    depName: row.depName,
    managerId: row.managerId,
    employeeIds: row.employeeList ? row.employeeList.map(e => e.empId) : []
  })
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitting.value = true
    try {
      const data = {
        id: formData.id,
        depName: formData.depName,
        managerId: formData.managerId,
        empList: formData.employeeIds.map(id => ({ empId: id }))
      }
      
      await saveDepartment(data)
      dialogVisible.value = false
      loadData()
    } catch (error) {
      console.error('提交失败:', error)
    } finally {
      submitting.value = false
    }
  })
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除部门「${row.depName}」吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await deleteDepartment(row.id, {
      pageNum: pagination.pageNum,
      searchKey: searchForm.searchKey
    })
    
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

onMounted(() => {
  loadData()
  loadEmployees()
})
</script>

<style scoped>
.departments-container {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  font-weight: 600;
}

.header-actions {
  display: flex;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.text-muted {
  color: #909399;
  font-size: 12px;
}
</style>

