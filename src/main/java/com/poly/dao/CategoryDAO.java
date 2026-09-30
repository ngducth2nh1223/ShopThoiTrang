package com.poly.dao;

import com.poly.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryDAO extends JpaRepository<Category, Integer> {
    // Spring Data JPA đã tích hợp sẵn các phương thức:
    // - findAll() -> Trả về List<Category>
    // - findById(Integer id) -> Trả về Optional<Category>
    // - save(Category entity) -> Thêm mới hoặc cập nhật
    // - deleteById(Integer id) -> Xóa theo ID
}