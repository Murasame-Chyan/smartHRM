package com.murasame.smarthrm.dao;

import com.murasame.smarthrm.entity.Skill;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SkillRepo extends MongoRepository<Skill, Integer> {
    boolean existsBySkillName(String skillName);
    // 根据名称模糊查询
    List<Skill> findBySkillNameContaining(String name);
    // 使用注解显式指定按 _id 倒序排列，取第一条
    @Query(value = "{}", sort = "{ '_id' : -1 }")
    List<Skill> findTopByOrderBy_idDesc();
}