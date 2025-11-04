package com.murasame.smarthrm.dao;

import com.murasame.smarthrm.entity.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface ProjectRepo extends MongoRepository<Project, Integer> {
}
