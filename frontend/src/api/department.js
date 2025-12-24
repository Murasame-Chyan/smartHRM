import request from './request'

/**
 * 获取部门列表（分页）
 * 对应接口：GET /departments/
 * 查询参数：searchKey(模糊查询部门名)，pageNum，pageSize
 */
export function getDepartments(params) {
  return request({
    url: '/departments/',
    method: 'get',
    params
  })
}

/**
 * 获取所有部门（用于下拉选项等）
 * 当前后端没有 /departments/all JSON 接口，
 * 这里复用 SkillMatchController 提供的：GET /skillmatch/departments
 */
export function getAllDepartments() {
  return request({
    url: '/skillmatch/departments',
    method: 'get'
  })
}

// 预留：如后端提供 REST 详情接口，可在此实现
export function getDepartmentById(id) {
  return request({
    url: `/departments/${id}`,
    method: 'get'
  })
}

// 预留：如后端提供 REST 部门员工接口，可在此实现
export function getDepartmentEmployees(id) {
  return request({
    url: `/departments/${id}/employees`,
    method: 'get'
  })
}

/**
 * 保存部门（新增 / 编辑）
 * 对应接口：POST /departments/save
 * 说明：后台使用表单参数绑定 Department 实体，这里用 params 传参
 * 常用参数：
 *  - id: 新增时为空 / 不传；编辑时为部门ID
 *  - depName: 部门名称
 *  - managerId: 负责人ID
 *  - empIds: '11,12'  员工ID，英文逗号分隔
 */
export function saveDepartment(params) {
  return request({
    url: '/departments/save',
    method: 'post',
    params
  })
}

/**
 * 删除部门
 * 对应接口：POST /departments/delete
 * 参数：id（必填），其余参数按需透传（如 pageNum、searchKey）
 */
export function deleteDepartment(id, extraParams) {
  return request({
    url: '/departments/delete',
    method: 'post',
    params: { id, ...(extraParams || {}) }
  })
}

