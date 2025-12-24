import request from './request'

/**
 * 获取员工列表（分页）
 * 对应接口：GET /employees/
 * 查询参数：empName(可选)，pageNum(默认1)，pageSize(默认10)
 */
export function getEmployees(params) {
  return request({
    url: '/employees/',
    method: 'get',
    params
  })
}

// 预留：如后端提供纯数据接口，可在此实现
export function getAllEmployees() {
  return request({
    url: '/employees/all',
    method: 'get'
  })
}

// 预留：如后端提供 REST 详情接口，可在此实现
export function getEmployeeById(id) {
  return request({
    url: `/employees/${id}`,
    method: 'get'
  })
}

/**
 * 提交新增员工
 * 对应接口：POST /employees/add
 * 说明：后端使用表单参数绑定 DTO，因此这里使用 params 传参
 * 参数示例：{
 *   name: '张三',
 *   department: '4',
 *   joinDate: '2023-02-10',
 *   skills: '1:4,2:5',         // 技能ID:熟练度，用英文逗号分隔
 *   newProjectIds: '1,3'       // 新关联项目ID，英文逗号分隔
 * }
 */
export function addEmployee(params) {
  return request({
    url: '/employees/add',
    method: 'post',
    params
  })
}

/**
 * 提交编辑员工
 * 对应接口：POST /employees/mod
 * 说明：同样使用表单参数，支持附带 pageNum、empName 便于后端重定向回原分页
 * 必填：id
 */
export function updateEmployee(params) {
  return request({
    url: '/employees/mod',
    method: 'post',
    params
  })
}

/**
 * 删除员工
 * 对应接口：POST /employees/del
 * 参数：id（必填），其余额外参数透传给后端
 */
export function deleteEmployee(id, extraParams) {
  return request({
    url: '/employees/del',
    method: 'post',
    params: { id, ...(extraParams || {}) }
  })
}

