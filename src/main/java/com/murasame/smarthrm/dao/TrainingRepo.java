package com.murasame.smarthrm.dao;

import com.murasame.smarthrm.entity.Training;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TrainingRepo extends MongoRepository<Training, Integer> {
    // 根据技能ID精确查询
    List<Training> findBySkillId(Integer skillId);

    // 校验用
    boolean existsByTrainName(String trainName);

    // 根据课程名模糊查询
    List<Training> findByTrainNameContaining(String name);
    // 使用注解显式指定按 _id 倒序排列，取第一条
    @Query(value = "{}", sort = "{ '_id' : -1 }")
    List<Training> findTopByOrderBy_idDesc();
}