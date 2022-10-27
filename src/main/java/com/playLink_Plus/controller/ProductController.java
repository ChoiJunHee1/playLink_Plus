package com.playLink_Plus.controller;

import com.playLink_Plus.dto.ProductDto;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    ProductDto productDto;
    AuthMaster authMaster;
    final Auth_Repository auth_repository;
    final Cafe24_Auth_Service cafe24_auth_service;
    final Options_Repository options_repository;
    final Product_Repository product_repository;
    Product_Service_Interface product_service = null ;
    Auth_Service_interface auth_service = null;
    @GetMapping("/IssuedItem")
    public void issued_Product_Item(@RequestParam("mall_id") String mallId){
        product_service = new Cafe24_Product_Service(cafe24_auth_service, options_repository, product_repository);
        try {
            product_service.issued_Product_Item(mallId);
        }catch (Exception e){
            log.error("온라인몰 상품수집중 오류 발생");
        }
    }
    @PostMapping("/UpDateProduct_Qty")
//    public void upDateProduct_Qty(@RequestBody HashMap<String,Object> upDate_QtyData) throws ParseException {
    public void upDateProduct_Qty(@RequestBody HashMap<String,Object> upDate_QtyData) throws ParseException {

//        System.out.println(upDate_QtyData.get("upDate_Item_List"));
//       List<HashMap<String,Object>> request_Item = (List<HashMap<String, Object>>) upDate_QtyData.get("upDate_Item_List");
//        System.out.println(request_Item.get(1).get("request_Item"));

        product_service = new Cafe24_Product_Service(cafe24_auth_service, options_repository, product_repository);
        product_service.upDate_Product_Qty(upDate_QtyData);

    }



}
