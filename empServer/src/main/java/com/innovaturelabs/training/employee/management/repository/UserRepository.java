
package com.innovaturelabs.training.employee.management.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.innovaturelabs.training.employee.management.entity.User;
import com.innovaturelabs.training.employee.management.view.StatusView;

public interface UserRepository extends Repository<User, Integer> {

    Optional<User> findByUserIdAndStatus(Integer userId, Byte status);

    Optional<User> findByUserIdAndStatusAndPasswordResetRequest(Integer userId, Byte status,
            Boolean passwordResetRequest);

    @Query(value = "SELECT * FROM user_tbl  WHERE status IN ?1 AND (name LIKE %?2% OR email LIKE %?2% OR address LIKE %?2% )", nativeQuery = true)
    Page<User> findAllByStatus(ArrayList<Byte> status, String search, Pageable page);

    @Query(value = "SELECT `COLUMN_NAME`  FROM `INFORMATION_SCHEMA`.`COLUMNS`  WHERE `TABLE_NAME`='user_tbl'", nativeQuery = true)
    ArrayList<String> findColumns();

    // Page<UserView> findAllByStatus(Byte status,Pageable page);

    Optional<User> findByUserIdAndPassword(Integer userId, String password);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndStatus(String email, Byte status);

    Optional<User> findByUserName(String userName);

    Optional<User> findByUserId(Integer userId);

    User save(User user);

    Collection<User> findAll();

    void delete(User user);

    void deleteById(Integer userId);

    @Query(value = "SELECT role as status, COUNT(*) as count FROM user_tbl GROUP BY role;", nativeQuery = true)
    Collection<StatusView> countUserRoles();

}
