package com.springboot.evaluation_task.repository;

import com.springboot.evaluation_task.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails,Long> {
    Long deleteByProductId(Long productId);

    Long deleteByOrderId(Long orderId);

    List<OrderDetails> findByOrderId(Long orderId);
}
