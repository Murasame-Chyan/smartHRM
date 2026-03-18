package com.cang.entity.dao;


import com.cang.entity.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Employee数据访问层
 * Spring Data MongoDB Repository接口
 */
@Repository
public interface EmployeeRepo extends MongoRepository<Employee, Integer> {
}