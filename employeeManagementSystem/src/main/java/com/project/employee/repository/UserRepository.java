package com.project.employee.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.project.employee.entity.User;

public interface UserRepository extends Repository<User, Integer> {

	Collection<User> findAll();// shows all users including inactive

	Collection<User> findAllByStatus(Byte status);// shows active users only

	Optional<User> findByUserIdAndPasswordAndStatus(Integer userId, String password, Byte status);

	User findByUserId(Integer userId);
	
	Optional<User> findById(Integer jobId);

	
//	Optional<User>findByUserIdAndStatus(Integer userId,byte status);

	Optional<User> findByUserIdAndStatus(Integer userId, Byte status);

	Optional<User> findByUserNameAndStatus(String userName, Byte status);// active users login

	User save(User user);// to save users

	@Query(value = "SELECT `COLUMN_NAME`  FROM `INFORMATION_SCHEMA`.`COLUMNS`  WHERE `TABLE_NAME`='user'", nativeQuery = true)
	ArrayList<String> findColumns();

	@Query(value = "SELECT * FROM user  WHERE status = ?1 AND (user_name LIKE %?2% OR email LIKE %?2% OR name LIKE %?2% )", nativeQuery = true)
	Page<User> findAllByStatus(Byte status, String search, Pageable page);

	@Query(value = "SELECT COUNT(*) FROM user  WHERE status = ?1 AND (user_name LIKE %?2% OR email LIKE %?2% OR name LIKE %?2%)", nativeQuery = true)
	Long countJobList(Byte status, String search);

	@Query(value = "SELECT COUNT(*) FROM user  WHERE status = ?1 ", nativeQuery = true)
	Long countUsers(Byte status);
}
