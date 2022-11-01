package com.playLink_Plus.repository;

import com.playLink_Plus.entity.order.OrderItem;
import com.playLink_Plus.entity.order.OrderReceivers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderReceiverRepository extends JpaRepository<OrderReceivers, String> {
}
