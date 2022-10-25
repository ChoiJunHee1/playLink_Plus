package com.playlinkplus.api.service;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public interface Product_Service_Interface {

    @Autowired
    String issued_Product_Item(String mall_id) throws ParseException;
}
