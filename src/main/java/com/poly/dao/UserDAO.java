package com.poly.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.entity.User;

@Repository
public interface UserDAO extends JpaRepository<User, Integer> {
    Optional<User> findByUsernameAndPasswordAndActivatedTrue(String username, String password);
    User findByUsernameAndPassword(String username, String password);
}