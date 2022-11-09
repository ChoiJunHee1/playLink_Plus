package com.playLink_Plus.controller;

import com.playLink_Plus.dto.ProductDto;
import com.playLink_Plus.entity.AuthMaster;
import com.playLink_Plus.repository.AuthRepository;
import com.playLink_Plus.repository.ProductDetailRepository;
import com.playLink_Plus.repository.ProductRepository;
import com.playLink_Plus.repository.UpDateStockProductQtyRepository;
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

    final AuthRepository authRepository;
    final Cafe24AuthService cafe24AuthService;
    final ProductDetailRepository productDetailRepository;
    final ProductRepository productRepository;
    final UpDateStockProductQtyRepository upDateStockProductQtyRepository;
    ProductServiceInterface productService = null;
    AuthServiceInterface auth_service = null;

    HashMap<String, Object> reqData22 = new HashMap<String,Object>();


    @GetMapping("/issuedItem")
    public void issuedProductItem(@RequestBody HashMap<String, Object> reqData) {

        System.out.println(reqData.get("systemId"));

        if (reqData.get("systemId").equals("cafe24")) {
            productService = new Cafe24ProductService(cafe24AuthService, productDetailRepository, productRepository);
        } else if (reqData.get("systemId").equals("godomall")) {
            productService = new GodoMallProductService(authRepository, productRepository, productDetailRepository,upDateStockProductQtyRepository);
        }
        productService.issuedProductItem(reqData);
    }

    @GetMapping("/regDateSearchProductInfo")
    public void responseProductInfo(@RequestBody HashMap<String, Object> reqData) {

        System.out.println(reqData);
        if (reqData.get("systemId").equals("cafe24")) {
            productService = new Cafe24ProductService(cafe24AuthService, productDetailRepository, productRepository);
        } else if (reqData.get("systemId").equals("godomall")) {
            productService = new GodoMallProductService(authRepository, productRepository, productDetailRepository,upDateStockProductQtyRepository);
        }

        productService.regDateSearchProductInfo(reqData);
    }


    @GetMapping("/upDateQtyXmlData/{systemId}/{mallId}")
    public String upDateProduct_Qty(@PathVariable String systemId,@PathVariable String mallId) throws ParseException {
        System.out.println(systemId);
        System.out.println(mallId);
        productService = new GodoMallProductService(authRepository, productRepository, productDetailRepository,upDateStockProductQtyRepository);
        return productService.upDateQtyXmlData(reqData22);

    }

    @GetMapping("/upDateStockQty")
    public void insertProductTest(@RequestBody HashMap<String, Object> reqData) throws InterruptedException {
        if (reqData.get("systemId").equals("cafe24")) {
            productService = new Cafe24ProductService(cafe24AuthService, productDetailRepository, productRepository);
        } else if (reqData.get("systemId").equals("godomall")) {
            System.out.println(reqData);
            productService = new GodoMallProductService(authRepository, productRepository, productDetailRepository,upDateStockProductQtyRepository);
        }
        productService.upDateStockQty(reqData);

    }

//    @GetMapping(path = "/makeProductDataXml", produces = MediaType.APPLICATION_XML_VALUE)
//    public String makeProductDataXml() {
//        productService = new GodoMallProductService(authRepository, productRepository, productDetailRepository);
//
//        return productService.makeProductDataXml();
//    }


}
