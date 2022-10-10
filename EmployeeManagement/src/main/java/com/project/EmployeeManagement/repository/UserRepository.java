package com.project.EmployeeManagement.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.project.EmployeeManagement.entity.User;

public interface UserRepository extends Repository<User, Integer> {
    User save(User user);

    Collection<User> findAll();

    Optional<User> findById(Integer userId);

    Optional<User> findByUserIdAndPassword(Integer userId, String password);

    Optional<User> findByUserName(String userName);

    Optional<User> findByUserId(Integer UserId);

    Collection<User> findByStatus(byte status);

    Collection<User> findByRoleAndStatus(byte role, byte status);

    // ...............................................
    // @Query(value = "SELECT COUNT(*) FROM user as u WHERE u.role = ?1 AND u.status in ?2 AND (u.user_name like %?3% OR u.name like %?3% ) ", nativeQuery = true)
    // Long countUserList(byte role, byte status, String search);

    @Query(value = "SELECT * FROM user  WHERE status = ?1  AND (name like %?2% OR address like %?2% OR email like %?2%) ", nativeQuery = true)
    Page<User> find(Byte status, String search, Pageable pageable);

    Optional<User> findByUserId(Long userId);
    // .....................................................


    @Query(nativeQuery = true, value = "UPDATE user set status=0 WHERE userId IN(ids)")
    Optional<User> softDeleteAllIds(List<Integer> ids);

    Optional<User> findByUserIdAndStatus(Integer userId, byte value);

    Optional<User> findByEmail(String email);

    // @Query("update  UserContentManagep u set u.active = false where u.id in(:integers)")
    // void softDeleteAllIds(List<Integer> integers);
}
