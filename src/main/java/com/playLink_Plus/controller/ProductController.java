package com.playLink_Plus.controller;

import com.playLink_Plus.dto.ProductDto;
import com.playLink_Plus.entity.AuthMaster;
import com.playLink_Plus.repository.AuthRepository;
import com.playLink_Plus.repository.ProductDetailRepository;
import com.playLink_Plus.repository.ProductRepository;
import com.playLink_Plus.service.AuthServiceInterface;
import com.playLink_Plus.service.ProductServiceInterface;
import com.playLink_Plus.service.auth.Cafe24AuthService;
import com.playLink_Plus.service.product.Cafe24ProductService;
import com.playLink_Plus.service.product.GodoMallProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    ProductDto productDto;
    AuthMaster authMaster;

    final AuthRepository authRepository;
    final Cafe24AuthService cafe24AuthService;
    final ProductDetailRepository productDetailRepository;
    final ProductRepository productRepository;
    ProductServiceInterface productService = null;
    AuthServiceInterface auth_service = null;

    HashMap<String, Object> reqData22 = new HashMap<>();
    @GetMapping("/issuedItem")
    public void issuedProductItem(@RequestBody HashMap<String, Object> reqData) {

        System.out.println(reqData.get("systemId"));

        if (reqData.get("systemId").equals("cafe24")) {
            productService = new Cafe24ProductService(cafe24AuthService, productDetailRepository, productRepository);
        } else if (reqData.get("systemId").equals("godomall")) {
            productService = new GodoMallProductService(authRepository, productRepository, productDetailRepository);
        }
        productService.issuedProductItem(reqData);
    }

    @GetMapping("/regDateSearchProductInfo")
    public void responseProductInfo(@RequestBody HashMap<String, Object> reqData) {

        System.out.println(reqData);
        if (reqData.get("systemId").equals("cafe24")) {
            productService = new Cafe24ProductService(cafe24AuthService, productDetailRepository, productRepository);
        } else if (reqData.get("systemId").equals("godomall")) {
            productService = new GodoMallProductService(authRepository, productRepository, productDetailRepository);
        }
        productService.regDateSearchProductInfo(reqData);
    }


    @GetMapping("/upDateQtyXmlData")
    public String upDateProduct_Qty() throws ParseException {
        System.out.println(reqData22);
            productService = new GodoMallProductService(authRepository, productRepository, productDetailRepository);
       String returnXml = productService.upDateQtyXmlData(reqData22);
       reqData22 = null;
        return returnXml;

    }

    @GetMapping("/upDateStockQty")
    public void insertProductTest(@RequestBody HashMap<String, Object> reqData) {
        reqData22 = reqData;
        System.out.println("뀨?");
        System.out.println(reqData.get("systemId"));
        if (reqData.get("systemId").equals("cafe24")) {
            productService = new Cafe24ProductService(cafe24AuthService, productDetailRepository, productRepository);
        } else if (reqData.get("systemId").equals("godomall")) {

           productService = new GodoMallProductService(authRepository, productRepository, productDetailRepository);
        }
        productService.upDateStockQty(reqData);

    }


    @GetMapping(path = "/makeProductDataXml", produces = MediaType.APPLICATION_XML_VALUE)
    public String makeProductDataXml() {
        productService = new GodoMallProductService(authRepository, productRepository, productDetailRepository);

        return productService.makeProductDataXml();
    }


}
