
package com.innovaturelabs.training.employee.management.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import com.innovaturelabs.training.employee.management.entity.User;
import com.innovaturelabs.training.employee.management.view.UserView;


public interface UserRepository extends Repository<User, Integer> {

    Optional<User> findById(Integer userId);

    Page<UserView> findAllByStatus(Byte status,Pageable page);

    Optional<User> findByUserIdAndPassword(Integer userId, String password);

    Optional<User> findByEmail(String email);
    Optional<User> findByUserName(String userName);

    User save(User user);
    
    Collection<User> findAll();

    void delete(User user);

    void deleteById(Integer userId);

}
