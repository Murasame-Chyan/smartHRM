package com.cang.service.impl;

import com.cang.entity.Department;
import com.cang.entity.Employee;
import com.cang.entity.Skill;
import com.cang.entity.dao.DepartmentRepo;
import com.cang.entity.dao.EmployeeDao;
import com.cang.entity.dao.ProjectRepo;
import com.cang.entity.dao.SkillRepo;
import com.cang.entity.dto.SkillDTO;
import com.cang.entity.dto.SkillMatchDTO;
import com.cang.service.SkillMatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillMatchServiceImpl implements SkillMatchService {
	private final EmployeeDao employeeDAO;
	private final DepartmentRepo departmentRepo;
	private final ProjectRepo projectRepo;
	private final SkillRepo skillRepo;

	@Override
	public List<Employee> matchBySkills(List<SkillMatchDTO> reqs) {
		List<Employee> employees = employeeDAO.findBySkillsRequired(reqs);

		for (Employee e : employees) {
			// 1. 补全部门名
			if (e.getDepId() != null) {
				Department dept = departmentRepo.findById(e.getDepId()).orElse(null);
				e.setDeptName(dept != null ? dept.getDepName() : "未分配");
			} else {
				e.setDeptName("未分配");
			}
		}

		return employees;
	}

	// 获取skillDTO 减少传输数据量
	@Override
	public List<SkillDTO> getSkillDTOs(){
		List<Skill> skills = skillRepo.findAll();
		List<SkillDTO> skillDTOs = new ArrayList<SkillDTO>();
		for(Skill s : skills){
			skillDTOs.add(new SkillDTO(s.get_id(), s.getSkillName()));
		}
		return skillDTOs;
	}
}
