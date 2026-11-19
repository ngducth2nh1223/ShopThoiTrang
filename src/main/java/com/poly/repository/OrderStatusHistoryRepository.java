package com.poly.repository;

import com.poly.entity.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Integer> {
    List<OrderStatusHistory> findByOrderIdOrderByHistoryIdAsc(Integer orderId);
}