package com.playLink_Plus.service;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public interface ProductServiceInterface {

    @Autowired
    void issuedProductItem(HashMap<String, Object> mall_id);

    void regDateSearchProductInfo(HashMap<String, Object> reqData);

    @Autowired
    String upDateQtyXmlData(String systemId,String mallId) throws ParseException;


    void upDateStockQty(String upDateQtyData, HashMap<String, Object> reqDataHashMap);

//    String makeProductDataXml();
}
