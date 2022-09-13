package com.project.EmployeeManagement.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.project.EmployeeManagement.entity.User;

public interface UserRepository extends Repository<User,Integer>{
    User save(User user);

    Collection<User> findAll();
    Optional<User> findById(Integer userId);
    Optional<User> findByUserIdAndPassword(Integer userId, String password);
    Optional<User> findByUserName(String userName);
    User findByUserId(Integer UserId);

    
    
}
