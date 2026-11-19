package com.poly.repository;

import com.poly.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findTop8ByStatusOrderByCreatedAtDesc(String status);
    List<Product> findByCategoryIdAndStatus(Integer categoryId, String status);

    @Query("select p from Product p where upper(p.status) = 'ACTIVE' " +
           "and (:keyword is null or :keyword = '' or lower(p.productName) like lower(concat('%', :keyword, '%')) " +
           "or lower(p.brand) like lower(concat('%', :keyword, '%'))) " +
           "and (:minPrice is null or p.basePrice >= :minPrice) " +
           "and (:maxPrice is null or p.basePrice <= :maxPrice) " +
           "order by p.createdAt desc")
    List<Product> searchActiveProducts(@Param("keyword") String keyword,
                                       @Param("minPrice") BigDecimal minPrice,
                                       @Param("maxPrice") BigDecimal maxPrice);
}
