package com.poly.dao;

import com.poly.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductDAO extends JpaRepository<Product, Integer> {

    // Lấy danh sách sản phẩm còn bán (available = true)
    List<Product> findByAvailableTrue();

    // Lấy danh sách sản phẩm còn bán theo mã danh mục (category.id)
    List<Product> findByCategoryIdAndAvailableTrue(Integer categoryId);
}