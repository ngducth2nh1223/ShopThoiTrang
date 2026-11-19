package com.poly.repository;

import com.poly.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query("select r from Review r where (:status = '' or upper(r.status) = upper(:status)) and (:rating = 0 or r.rating = :rating) order by r.createdAt desc")
    List<Review> search(@Param("status") String status, @Param("rating") Integer rating);
}
