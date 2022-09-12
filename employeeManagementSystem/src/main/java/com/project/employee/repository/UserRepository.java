package com.project.employee.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.project.employee.entity.User;

public interface UserRepository extends Repository<User, Integer> {

	Collection<User> findAll();//shows all users including inactive
	
	Collection<User> findAllByStatus(Byte status);//shows active users only
	
	Optional<User>findByUserIdAndPasswordAndStatus(Integer userId, String password,Byte status);
	
	User findByUserId(Integer userId);
		
	Optional<User> findByUserIdAndStatus(Integer userId,Byte status);

	Optional<User> findByUserNameAndStatus(String userName,Byte status);//active users login

	User save(User user);//to save users
}
