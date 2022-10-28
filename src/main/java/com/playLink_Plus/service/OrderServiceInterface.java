package com.playLink_Plus.service;

import com.playLink_Plus.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public interface OrderServiceInterface {
    @Autowired
    void issuedOrder(OrderDto orderDto);
}
