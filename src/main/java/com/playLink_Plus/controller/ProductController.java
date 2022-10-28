package com.playLink_Plus.controller;

import com.playLink_Plus.dto.ProductDto;
import com.playLink_Plus.entity.AuthMaster;
import com.playLink_Plus.repository.AuthRepository;
import com.playLink_Plus.repository.OptionsRepository;
import com.playLink_Plus.repository.ProductRepository;
import com.playLink_Plus.service.AuthServiceInterface;
import com.playLink_Plus.service.ProductServiceInterface;
import com.playLink_Plus.service.auth.Cafe24AuthService;
import com.playLink_Plus.service.product.Cafe24ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    ProductDto productDto;
    AuthMaster authMaster;
    final AuthRepository authRepository;
    final Cafe24AuthService cafe24AuthService;
    final OptionsRepository optionsRepository;
    final ProductRepository productRepository;
    ProductServiceInterface productService = null ;
    AuthServiceInterface auth_service = null;
    @GetMapping("/issuedItem")
    public void issuedProductItem(@RequestParam("mallId") String mallId){
        productService = new Cafe24ProductService(cafe24AuthService, optionsRepository, productRepository);
        try {
            productService.issuedProductItem(mallId);
        }catch (Exception e){
            log.error("온라인몰 상품수집중 오류 발생");
        }
    }
    @PostMapping("/UpDateProductQty")
    public void upDateProduct_Qty(@RequestBody HashMap<String,Object> upDateQtyData) throws ParseException {

        productService = new Cafe24ProductService(cafe24AuthService, optionsRepository, productRepository);
        productService.upDateProductQty(upDateQtyData);

    }



}
