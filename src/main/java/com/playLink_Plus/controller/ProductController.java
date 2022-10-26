package com.playLink_Plus.controller;

import com.playLink_Plus.entity.AuthMaster;
import com.playLink_Plus.repository.Auth_Repository;
import com.playLink_Plus.repository.Options_Repository;
import com.playLink_Plus.repository.Product_Repository;
import com.playLink_Plus.service.Auth_Service_interface;
import com.playLink_Plus.service.Product_Service_Interface;
import com.playLink_Plus.service.auth.Cafe24_Auth_Service;
import com.playLink_Plus.service.product.Cafe24_Product_Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    AuthMaster authMaster;
    final Auth_Repository auth_repository;
    final Cafe24_Auth_Service cafe24_auth_service;
    final Options_Repository options_repository;
    final Product_Repository product_repository;
    Product_Service_Interface product_service = null ;
    Auth_Service_interface auth_service = null;
    @GetMapping("IssuedItem")
    public void issued_Product_Item(@RequestParam("mall_id") String mallId){
        auth_service = new Cafe24_Auth_Service(auth_repository);
        product_service = new Cafe24_Product_Service(cafe24_auth_service, options_repository, product_repository);
        try {
            authMaster  = auth_service.refreshTokenIssued(mallId);
            product_service.issued_Product_Item(mallId,authMaster);
        }catch (Exception e){
            log.error("최초 토큰 발급시 상품 테이블 업데이트 중 오류 발생");
        }
    }
}
