package com.playLink_Plus.service;

import com.playLink_Plus.dto.ProductDto;
import com.playLink_Plus.entity.AuthMaster;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public interface Product_Service_Interface {

    @Autowired
    void issued_Product_Item(String mall_id);

    @Autowired
    void upDate_Product_Qty(HashMap<String,Object> upDate_QtyData ) throws ParseException;

}
