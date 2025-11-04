package com.murasame.smarthrm.dao;

import com.murasame.smarthrm.dto.SkillMatchDTO;
import com.murasame.smarthrm.entity.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EmployeeDao {

	private final MongoTemplate mongoTemplate;

	/*
	  匹配：skillList 里同时存在
	  key = skillId, value ≥ minLevel
	 */
	public List<Employee> findBySkillsRequired(List<SkillMatchDTO> reqs) {
		if (CollectionUtils.isEmpty(reqs)) return Collections.emptyList();

		// 每个 req 生成一个 elemMatch
		Criteria[] array = reqs.stream()
				.map(r -> Criteria.where("skillList." + r.getSkillId()).gte(r.getMinLevel()))
				.toArray(Criteria[]::new);

		Query query = new Query(new Criteria().andOperator(array));
		return new ArrayList<>(mongoTemplate.find(query, Employee.class));
/*
		try {
			return mongoTemplate.find(query, Employee.class);
		} catch (Exception e) {
			System.err.println(">>> 反序列化失败，条件=" + Arrays.toString(array));
			System.err.println(">>> 命中文档=" + mongoTemplate.find(query, Document.class));
			throw e;   // 继续抛，让前端 500
		}
*/
	}
}
