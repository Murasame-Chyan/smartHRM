package com.murasame.smarthrm.controller;

import com.murasame.smarthrm.dto.AddEmployeeDTO;
import com.murasame.smarthrm.dto.ModEmployeeDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
	// 1. 管理页
	@GetMapping("/")
	public String index() {
		return "employees";
	}

	@PostMapping("/add")
	public String addEmployee(@Valid AddEmployeeDTO dto,
	                          BindingResult br,
	                          RedirectAttributes ra) {
		// 表单提交数据，自动封装同名字段为dto 差错检测
		if (br.hasErrors()) {
			ra.addFlashAttribute("errors", br.getFieldErrors());
			return "redirect:/employees";   // 回到列表页并显示错误
		}
		// 调用服务 ===>
		return "redirect:/employees";
	}

	@PostMapping("/del")
	public String deleteEmployee(@RequestParam int id) {
		// 调用服务 ===>
		return "redirect:/employees";   // 重定向并刷新列表
	}

	// 2. 编辑页
	@GetMapping("/mod")
	public String modEmployeePage(@RequestParam int id) {
		return "modEmployees";
	}

	@PostMapping("/mod")
	public String modEmployee(@Valid ModEmployeeDTO dto,
	                          BindingResult br,
	                          RedirectAttributes ra) {
		if (br.hasErrors()) {
			ra.addFlashAttribute("errors", br.getFieldErrors());
			return "redirect:/employees";   // 回到列表页并显示错误
		}
		// 调用服务 ===>
		return "redirect:/employees";
	}

}
