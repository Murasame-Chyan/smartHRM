package com.murasame.smarthrm.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.List;

@Data
@Document(collection = "Department") // 对应MongoDB中的集合名
public class Department {
	@Id               // ← 告诉 Spring Data 这是主键
	@Field("_id")     // ← 强制映射文档字段 "_id"
	private Integer _id;
	private String depName;
	private Integer managerId;
	private List<Integer> empList;
}
