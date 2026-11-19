package com.poly.repository;

import com.poly.entity.Orders;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {

    Optional<Orders> findByOrderCode(String orderCode);
    boolean existsByOrderCode(String orderCode);
    List<Orders> findByUserIdOrderByOrderIdDesc(Integer userId);

    /** Khóa dòng đơn hàng để hai thao tác cùng lúc không đè lên nhau */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from Orders o where o.orderCode = :code")
    Optional<Orders> lockByOrderCode(@Param("code") String code);

    @Query(value = "select o from Orders o where (:status = '' or o.orderStatus = :status) "
                 + "and (:keyword = '' or o.orderCode like concat('%', :keyword, '%')) order by o.orderId desc",
           countQuery = "select count(o) from Orders o where (:status = '' or o.orderStatus = :status) "
                      + "and (:keyword = '' or o.orderCode like concat('%', :keyword, '%'))")
    Page<Orders> search(@Param("status") String status, @Param("keyword") String keyword, Pageable pageable);

    @Query("select coalesce(sum(o.finalAmount), 0) from Orders o where o.orderStatus = 'COMPLETED'")
    java.math.BigDecimal totalCompletedRevenue();

    long countByOrderStatus(String orderStatus);


    long countByOrderStatusIn(List<String> statuses);

    @Query("select coalesce(sum(o.finalAmount), 0) from Orders o where o.orderStatus = 'COMPLETED' and o.createAt >= :from and o.createAt < :to")
    java.math.BigDecimal revenueBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("select function('format', o.createAt, 'yyyy-MM-dd'), coalesce(sum(o.finalAmount), 0) from Orders o where o.orderStatus = 'COMPLETED' and o.createAt >= :from and o.createAt < :to group by function('format', o.createAt, 'yyyy-MM-dd') order by function('format', o.createAt, 'yyyy-MM-dd')")
    List<Object[]> dailyRevenue(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("select od.variant.product.productName, sum(od.quantity) from OrderDetail od join Orders o on o.orderId = od.orderId where o.orderStatus = 'COMPLETED' and o.createAt >= :from and o.createAt < :to group by od.variant.product.productName order by sum(od.quantity) desc")
    List<Object[]> bestSellingProducts(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to, org.springframework.data.domain.Pageable pageable);

    /** Đơn online chờ xử lý, quá hạn mà chưa thanh toán thành công */
    @Query("select o from Orders o where o.orderStatus = 'PENDING' and o.createAt < :cutoff "
         + "and exists (select 1 from Payment p where p.orderId = o.orderId "
         + "and p.paymentMethod = 'E_WALLET' and p.paymentStatus <> 'SUCCESS')")
    List<Orders> findExpiredUnpaidOnline(@Param("cutoff") LocalDateTime cutoff);
}