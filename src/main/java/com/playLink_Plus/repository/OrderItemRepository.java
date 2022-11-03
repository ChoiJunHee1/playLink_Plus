package com.playLink_Plus.repository;

import com.playLink_Plus.entity.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository  extends JpaRepository<OrderItem, String> {
}
