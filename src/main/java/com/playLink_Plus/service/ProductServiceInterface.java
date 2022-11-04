package com.playLink_Plus.service;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public interface ProductServiceInterface {

    @Autowired
    void issuedProductItem(HashMap<String, Object> mall_id);

    void checkProductInfo(HashMap<String, Object> reqData);

    @Autowired
    void upDateProductQty(HashMap<String, Object> upDate_QtyData) throws ParseException;


    void insertProductTest();

    String makeProductDataXml();
}
