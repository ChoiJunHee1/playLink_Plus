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
import com.playLink_Plus.service.product.GodomallProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.List;

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
    ProductServiceInterface productService = null;
    AuthServiceInterface auth_service = null;

    @GetMapping("/issuedItem")
    public void issuedProductItem(@RequestBody HashMap<String, Object> reqData) {



        System.out.println(reqData.get("systemId"));

            if (reqData.get("systemId").equals("cafe24")) {
                productService = new Cafe24ProductService(cafe24AuthService, optionsRepository, productRepository);
            } else if (reqData.get("systemId").equals("godomall")) {
                productService = new GodomallProductService(authRepository,productRepository,optionsRepository);
            }
            productService.issuedProductItem(reqData);
    }

    @PostMapping("/responseProductInfo")
    public void responseProductInfo(@RequestBody HashMap<String, Object> reqData){

        if (reqData.get("systemId").equals("cafe24")) {
            productService = new Cafe24ProductService(cafe24AuthService, optionsRepository, productRepository);
        } else if (reqData.get("systemId").equals("godomall")) {
            productService = new GodomallProductService(authRepository,productRepository,optionsRepository);
        }
        productService.checkProductInfo(reqData);
    }


    @PostMapping("/UpDateProductQty")
    public void upDateProduct_Qty(@RequestBody HashMap<String, Object> upDateQtyData) throws ParseException {

        productService = new Cafe24ProductService(cafe24AuthService, optionsRepository, productRepository);
        productService.upDateProductQty(upDateQtyData);

    }

    @GetMapping("/insertProductTest")
    public void insertProductTest() {

        System.out.println("뀨?");
        productService = new GodomallProductService(authRepository,productRepository,optionsRepository);
        productService.insertProductTest();

    }


    @GetMapping(path = "/makeProductDataXml", produces = MediaType.APPLICATION_XML_VALUE)
    public String makeProductDataXml() {
        productService = new GodomallProductService(authRepository,productRepository,optionsRepository);

        return productService.makeProductDataXml();
    }


}
