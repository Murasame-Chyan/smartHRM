package com.murasame.smarthrm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ModEmployeeDTO {
	@NotNull(message = "id不可为空")
	private int id;

	@NotBlank(message = "名字不可为空")
	private String name;

	@NotBlank(message = "部门不可为空")
	private String department;

	List<Map<Integer, Integer>> skills;  // 技能及其熟练度列表（暂均用id）

	List<Integer> projects;              // 负责项目
}
