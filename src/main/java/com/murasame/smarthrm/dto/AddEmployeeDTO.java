package com.murasame.smarthrm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// 新增员工dto
@Data
public class AddEmployeeDTO {
	@NotNull(message = "名字不可为空")
	private String name;

	@NotBlank(message = "部门不可为空")
	private String department;
}
