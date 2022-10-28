
package com.innovaturelabs.training.employee.management.repository;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.innovaturelabs.training.employee.management.entity.User;
import com.innovaturelabs.training.employee.management.view.ChartView;
import com.innovaturelabs.training.employee.management.view.StatusView;

public interface UserRepository extends Repository<User, Integer> {

    Optional<User> findByUserIdAndStatus(Integer userId, Byte status);

    Optional<User> findByUserIdAndStatusAndUserType(Integer userId, Byte status, Byte userType);

    Optional<User> findByUserIdAndUserType(Integer userId, Byte userType);

    Optional<User> findByUserIdAndStatusAndPasswordResetRequest(Integer userId, Byte status,
            Boolean passwordResetRequest);

    @Query(value = "SELECT user FROM com.innovaturelabs.training.employee.management.entity.User user WHERE user.status IN ?1 AND user.role IN ?2 AND (user.name LIKE %?3% OR user.email LIKE %?3% OR user.address LIKE %?3% )")
    Page<User> findAllByStatus(Collection<Byte> status, Collection<Byte> roles, String search, Pageable page);

    // Page<User> findByNameContaining(String name, Pageable page);

    // @Query(value = "SELECT `COLUMN_NAME` FROM `INFORMATION_SCHEMA`.`COLUMNS`
    // WHERE `TABLE_NAME`='user_tbl'", nativeQuery = true)
    // ArrayList<String> findColumns();

    Optional<User> findByUserIdAndPassword(Integer userId, String password);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndUserType(String email, Byte userType);

    Optional<User> findByEmailAndStatus(String email, Byte status);

    Optional<User> findByUserName(String userName);

    Optional<User> findByUserNameLike(String userName);

    Optional<User> findByUserId(Integer userId);

    User save(User user);

    Collection<User> findAll();

    void delete(User user);

    void deleteById(Integer userId);

    @Query(value = "select date(create_date) as date ,count(*) as count  from user_tbl group by date(create_date)", nativeQuery = true)
    Collection<ChartView> getJoinDates();

    @Query(value = "SELECT role as status, COUNT(*) as count FROM com.innovaturelabs.training.employee.management.entity.User GROUP BY role")
    Collection<StatusView> countUserRoles();

    @Query(value = "SELECT user FROM com.innovaturelabs.training.employee.management.entity.User user WHERE user.status IN ?1 AND user.role IN ?2 AND user.createDate >= ?3 AND user.createDate <=  ?4")
    Collection<User> findQueryCsv(Collection<Byte> status, Collection<Byte> roles, Date startDate, Date endDate);

}
