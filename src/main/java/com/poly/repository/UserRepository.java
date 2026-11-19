package com.poly.repository;

import com.poly.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("select u from User u where (:keyword is null or :keyword = '' " +
           "or lower(u.fullName) like lower(concat('%', :keyword, '%')) " +
           "or lower(u.email) like lower(concat('%', :keyword, '%')) " +
           "or lower(coalesce(u.phone, '')) like lower(concat('%', :keyword, '%'))) " +
           "order by u.userId desc")
    List<User> searchUsers(@Param("keyword") String keyword);
}
