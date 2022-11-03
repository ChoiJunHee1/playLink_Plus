package com.playLink_Plus.repository;

import com.playLink_Plus.entity.AuthMaster;
import com.playLink_Plus.entity.order.OrderMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository  extends JpaRepository<OrderMaster,String> {
}
