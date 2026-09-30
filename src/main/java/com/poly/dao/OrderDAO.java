package com.poly.dao;

import com.poly.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDAO extends JpaRepository<Order, Integer> {
    // Spring Data JPA hỗ trợ sẵn phương thức save() để tạo/cập nhật Order
}