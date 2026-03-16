package com.cang.service;

import com.cang.entity.Employee;
import com.cang.entity.dto.SkillDTO;
import com.cang.entity.dto.SkillMatchDTO;

import java.util.List;

public interface SkillMatchService {
	// 按「技能:最小熟练度」字符串列表匹配员工
	List<Employee> matchBySkills(List<SkillMatchDTO> reqs);

	// 获取skillDTO 减少传输数据量
	List<SkillDTO> getSkillDTOs();
}
