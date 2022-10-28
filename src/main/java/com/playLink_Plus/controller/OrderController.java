package com.playLink_Plus.controller;

import com.playLink_Plus.dto.OrderDto;
import com.playLink_Plus.entity.order.OrderMaster;
import com.playLink_Plus.service.OrderServiceInterface;
import com.playLink_Plus.service.order.Cafe24OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    OrderDto orderDto;

    OrderServiceInterface orderService = null;

    @PostMapping("issuedOrder")
    public void issuedOrder (@RequestBody OrderDto orderDto){

        orderService = new Cafe24OrderService();
        orderService.issuedOrder(orderDto);




    }
}
