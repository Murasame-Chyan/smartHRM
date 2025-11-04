package com.murasame.smarthrm.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "Project") // 对应MongoDB中的集合名
public class Project {
	@Id               // ← 告诉 Spring Data 这是主键
	@Field("_id")     // ← 强制映射文档字段 "_id"
	private Integer _id;
	private String projName;
	private List<Integer> members;
	private List<Map<String, Integer>> reqSkill; // 项目需求技能: [{技能id, 熟练度}...]
	private Integer projStatus;                     // 0-已归档 1-正在进行
	private LocalDateTime startDate;
}
