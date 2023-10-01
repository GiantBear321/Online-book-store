package com.example.onlinebookstore.repository;

import com.example.onlinebookstore.model.Order;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("UPDATE Order o SET o.shippingAddress=:shippingAddress "
            + "WHERE o.id=:id AND o.user.id=:userId")
    @Modifying
    @Transactional
    void updateShippingAddress(@Param("id") Long id,
                               @Param("shippingAddress") String shippingAddress,
                               @Param("userId") Long userId);

    @Query("UPDATE Order o SET o.status=:status WHERE o.id=:id ")
    @Modifying
    @Transactional
    void updateStatusByOrderId(@Param("id") Long id, @Param("status") Order.Status status);

    @EntityGraph(attributePaths = "orderItems")
    List<Order> findAllByUserId(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = "orderItems")
    Optional<Order> findById(Long id);
}
