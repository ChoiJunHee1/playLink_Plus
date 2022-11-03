package com.playLink_Plus.controller;

import com.playLink_Plus.dto.OrderDto;
import com.playLink_Plus.repository.OrderItemRepository;
import com.playLink_Plus.repository.OrderReceiverRepository;
import com.playLink_Plus.repository.OrderRepository;
import com.playLink_Plus.service.OrderServiceInterface;
import com.playLink_Plus.service.auth.Cafe24AuthService;
import com.playLink_Plus.service.order.Cafe24OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {


    final Cafe24AuthService cafe24_auth_service;
    OrderDto orderDto;
    final OrderRepository orderRepository;
    final OrderItemRepository orderItemRepository;

    final OrderReceiverRepository orderReceiverRepository;
    OrderServiceInterface orderService = null;

    @PostMapping("issuedOrder")
    public void issuedOrder (@RequestBody OrderDto orderDto){

        if (orderDto.getSystemId().equals("cafe24")) {

            orderService = new Cafe24OrderService(cafe24_auth_service, orderItemRepository, orderRepository,orderReceiverRepository);
            orderService.issuedOrder(orderDto);

        }else {
            System.out.println("없는 systemId");
        }


    }
}
