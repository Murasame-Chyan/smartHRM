<template>
  <div class="employees-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">
            <el-icon><User /></el-icon>
            员工管理
          </span>
          <div class="header-actions">
            <el-input
              v-model="searchForm.empName"
              placeholder="输入员工姓名搜索"
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
              新增员工
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
        <el-table-column prop="_id" label="员工ID" width="100" />
        <el-table-column prop="empName" label="员工姓名" width="120" />
        <el-table-column label="所属部门" width="150">
          <template #default="{ row }">
            <el-tag :type="getDeptTagType(row.deptType)">
              {{ row.deptName || '未分配' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="技能列表" min-width="200">
          <template #default="{ row }">
            <el-tag
              v-for="(skill, index) in row.skillList"
              :key="index"
              type="info"
              style="margin-right: 5px; margin-bottom: 5px"
            >
              技能{{ skill.skillId }}（{{ skill.proficiency }}级）
            </el-tag>
            <span v-if="!row.skillList || row.skillList.length === 0" class="text-muted">无</span>
          </template>
        </el-table-column>
        <el-table-column label="参与项目" min-width="150">
          <template #default="{ row }">
            <el-tag
              v-for="(proj, index) in row.projects"
              :key="index"
              type="success"
              style="margin-right: 5px; margin-bottom: 5px"
            >
              项目{{ proj.projId }}
            </el-tag>
            <span v-if="!row.projects || row.projects.length === 0" class="text-muted">无</span>
          </template>
        </el-table-column>
        <el-table-column prop="joinDate" label="加入时间" width="120">
          <template #default="{ row }">
            {{ formatDate(row.joinDate) }}
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
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="120px"
      >
        <el-form-item label="员工姓名" prop="name">
          <el-input v-model="formData.name" placeholder="请输入员工姓名" />
        </el-form-item>
        
        <el-form-item label="所属部门">
          <el-select
            v-model="formData.department"
            placeholder="请选择部门"
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="dept in departments"
              :key="dept.id"
              :label="dept.depName"
              :value="dept.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="加入时间">
          <el-date-picker
            v-model="formData.joinDate"
            type="datetime"
            placeholder="选择加入时间"
            style="width: 100%"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DDTHH:mm:ss"
          />
        </el-form-item>

        <el-form-item label="技能列表">
          <el-input
            v-model="formData.skills"
            type="textarea"
            :rows="3"
            placeholder="格式：skillId:proficiency,skillId:proficiency（如：1:4,2:3）"
          />
          <div class="form-tip">多个技能用逗号分隔，格式：技能ID:熟练度（1-5级）</div>
        </el-form-item>

        <el-form-item label="关联项目">
          <el-select
            v-model="formData.newProjectIds"
            multiple
            placeholder="请选择项目"
            style="width: 100%"
          >
            <el-option
              v-for="proj in allProjects"
              :key="proj._id"
              :label="proj.projName"
              :value="proj._id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="负责任务">
          <el-select
            v-model="formData.newManagerTaskIds"
            multiple
            placeholder="请选择任务"
            style="width: 100%"
          >
            <el-option
              v-for="task in allTasks"
              :key="task._id"
              :label="task.taskName || `任务${task._id}`"
              :value="task._id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="参与培训">
          <el-select
            v-model="formData.newTrainingIds"
            multiple
            placeholder="请选择培训"
            style="width: 100%"
          >
            <el-option
              v-for="training in allTrainings"
              :key="training._id"
              :label="training.trainName"
              :value="training._id"
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
import {Delete, Edit, Plus, Search, User} from '@element-plus/icons-vue'
import {addEmployee, deleteEmployee, updateEmployee} from '@/api/employee'
import {getAllDepartments} from '@/api/department'
import {getAllProjects} from '@/api/project'

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增员工')
const formRef = ref(null)

const searchForm = reactive({
  empName: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref([])
const departments = ref([])
const allProjects = ref([])
const allTasks = ref([])
const allTrainings = ref([])

const formData = reactive({
  id: null,
  name: '',
  department: '',
  joinDate: null,
  skills: '',
  newProjectIds: [],
  newManagerTaskIds: [],
  newTrainingIds: []
})

const formRules = {
  name: [
    { required: true, message: '请输入员工姓名', trigger: 'blur' }
  ]
}

const getDeptTagType = (type) => {
  const typeMap = {
    normal: '',
    deleted: 'danger',
    unassigned: 'info'
  }
  return typeMap[type] || 'info'
}

const formatDate = (date) => {
  if (!date) return '未设置'
  const d = new Date(date)
  return d.toLocaleDateString('zh-CN')
}

const loadData = async () => {
  loading.value = true
  try {
    // 注意：后端返回的是Thymeleaf渲染的HTML，需要改为API调用
    // 这里假设后端有对应的REST API
    const params = {
      empName: searchForm.empName || undefined,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    }
    
    // 临时：由于后端主要是Thymeleaf，这里需要根据实际情况调整
    // 可能需要先在后端添加REST API接口
    const response = await fetch(`/employees/?empName=${params.empName || ''}&pageNum=${params.pageNum}&pageSize=${params.pageSize}`)
    const text = await response.text()
    
    // 这里需要解析HTML或等待后端提供JSON API
    // 暂时使用模拟数据
    ElMessage.warning('请先在后端添加REST API接口，当前使用模拟数据')
    
    // 模拟数据
    tableData.value = []
    pagination.total = 0
    
  } catch (error) {
    ElMessage.error('加载数据失败：' + error.message)
  } finally {
    loading.value = false
  }
}

const loadOptions = async () => {
  try {
    departments.value = await getAllDepartments() || []
    allProjects.value = await getAllProjects() || []
    // allTasks 和 allTrainings 需要对应的API
  } catch (error) {
    console.error('加载选项失败:', error)
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
  dialogTitle.value = '新增员工'
  Object.assign(formData, {
    id: null,
    name: '',
    department: '',
    joinDate: null,
    skills: '',
    newProjectIds: [],
    newManagerTaskIds: [],
    newTrainingIds: []
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑员工'
  Object.assign(formData, {
    id: row._id,
    name: row.empName,
    department: row.depId ? row.depId.toString() : '',
    joinDate: row.joinDate,
    skills: row.skillList ? row.skillList.map(s => `${s.skillId}:${s.proficiency}`).join(',') : '',
    newProjectIds: row.projects ? row.projects.map(p => p.projId) : [],
    newManagerTaskIds: [],
    newTrainingIds: []
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
        name: formData.name,
        department: formData.department || null,
        joinDate: formData.joinDate,
        skills: formData.skills,
        newProjectIds: formData.newProjectIds,
        newManagerTaskIds: formData.newManagerTaskIds,
        newTrainingIds: formData.newTrainingIds
      }
      
      if (formData.id) {
        await updateEmployee(data)
      } else {
        await addEmployee(data)
      }
      
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
      `确定要删除员工「${row.empName}」吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await deleteEmployee(row._id, {
      pageNum: pagination.pageNum,
      empName: searchForm.empName
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
  loadOptions()
})
</script>

<style scoped>
.employees-container {
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

.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}
</style>

