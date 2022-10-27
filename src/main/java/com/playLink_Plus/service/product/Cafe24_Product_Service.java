package com.playLink_Plus.service.product;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.playLink_Plus.dto.ApiCallDto;
import com.playLink_Plus.dto.ProductDto;
import com.playLink_Plus.entity.AuthMaster;
import com.playLink_Plus.entity.ProductMaster;
import com.playLink_Plus.entity.VariantOption;
import com.playLink_Plus.repository.Options_Repository;
import com.playLink_Plus.repository.Product_Repository;
import com.playLink_Plus.service.Product_Service_Interface;
import com.playLink_Plus.service.auth.Cafe24_Auth_Service;
import kong.unirest.HttpResponse;
import kong.unirest.ObjectMapper;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Reader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class Cafe24_Product_Service implements Product_Service_Interface {
    @Autowired
   final Cafe24_Auth_Service cafe24_auth_service;

    @Autowired
   final Options_Repository options_repository;

    @Autowired
    final Product_Repository product_repository;

    AuthMaster authMaster;

    public Cafe24_Product_Service(Cafe24_Auth_Service cafe24_auth_service, Options_Repository options_repository, Product_Repository product_repository) {
        this.cafe24_auth_service = cafe24_auth_service;
        this.options_repository = options_repository;
        this.product_repository = product_repository;
    }

    ProductMaster productMaster;
    ApiCallDto apiCallDto = new ApiCallDto();

    @Transactional
    @Override
    public void issued_Product_Item(String mallid) {
        authMaster  = cafe24_auth_service.refreshTokenIssued(mallid);
        HttpResponse<String> response = Unirest.get("https://" + mallid + ".cafe24api.com/api/v2/products/count")
                .header("Content-Type", apiCallDto.getContent_Type())
                .header("X-Cafe24-Api-Version", apiCallDto.getX_Cafe24_Api_Version())
                .header("'Authorization", "Bearer " + authMaster.getAccessToken())
                .header("X-Cafe24-Client-Id", apiCallDto.getX_Cafe24_Client_Id())
                .asString();

        System.out.println(response.getBody());
        JSONParser parser = new JSONParser();
        JSONObject tokenData = new JSONObject();
        try {
            tokenData = (JSONObject) parser.parse(response.getBody());

        } catch (Exception e) {
            log.error("전체 상품 갯수 요청 중 오류");
        }

        int offsetInt = 0;
        String num = tokenData.get("count").toString();

        System.out.println(num);

        double offsetDouble = Double.parseDouble(num);

        if (offsetDouble < 100) {
            offsetInt = 0;
        } else {
            offsetDouble = Math.ceil(offsetDouble / 100);
            offsetInt = (int) offsetDouble;
        }
        List<ProductMaster> productOptionNull = new ArrayList<>();
        List<ProductMaster> products = new ArrayList<>();
        List<VariantOption> VariantOptions = new ArrayList<>();
        for (int q = 0; q <= offsetInt;q++) {
            AuthMaster refreshTokenIssued = cafe24_auth_service.refreshTokenIssued(mallid);
            HttpResponse<String> response2 = Unirest.get("https://" + mallid + ".cafe24api.com/api/v2/products?offset=" + q + "00&limit=100")
                    .header("Content-Type", apiCallDto.getContent_Type())
                    .header("X-Cafe24-Api-Version", apiCallDto.getX_Cafe24_Api_Version())
                    .header("'Authorization", "Bearer " + authMaster.getAccessToken())
                    .header("X-Cafe24-Client-Id", apiCallDto.getX_Cafe24_Client_Id())
                    .asString();

            try {

                Gson gson = new Gson();
                Map<String, Object> productData = gson.fromJson(response2.getBody(), new TypeToken<Map<String, Object>>() {
                }.getType());
                List<Map<String, Object>> productList = (List) productData.get("products");


                for (int i = 0; i < productList.size(); i++) {
                    String product_name = (String) productList.get(i).get("product_name");
                    String product_code = (String) productList.get(i).get("product_code");

                    double product_No = (double) productList.get(i).get("product_no");
                    int product_No_int = (int) product_No;

                    Thread.sleep(250);

                    HttpResponse<String> response3 = Unirest.get("https://" + mallid + ".cafe24api.com/api/v2/products/" + product_No_int + "/variants")
                            .header("Content-Type", apiCallDto.getContent_Type())
                            .header("X-Cafe24-Api-Version", apiCallDto.getX_Cafe24_Api_Version())
                            .header("'Authorization", "Bearer " + refreshTokenIssued.getAccessToken())
                            .header("X-Cafe24-Client-Id", apiCallDto.getX_Cafe24_Client_Id())
                            .asString();

                    Map<String, Object> variantData = gson.fromJson(response3.getBody(), new TypeToken<Map<String, Object>>() {
                    }.getType());
                    List<Map<String, Object>> variantList = (List) variantData.get("variants");


                    for (int j = 0; j < variantList.size(); j++) {

                        if (variantList.get(j).get("options") == null) {
                            ProductMaster product_List = ProductMaster.builder()
                                    .mallId(mallid)
                                    .productCode(product_code)
                                    .productName(product_name)
                                    .variantCode((String) variantList.get(j).get("variant_code"))
                                    .productNo(product_No_int)
                                    .systemId("cafe24")
                                    .optionQty(0).build();
                            productOptionNull.add(product_List);

                        } else {

                            List<Map<String, Object>> variantOptionsList = (List) variantList.get(j).get("options");

                            ProductMaster product_List = ProductMaster.builder()
                                    .mallId(mallid)
                                    .productName(product_name)
                                    .productCode(product_code)
                                    .variantCode((String) variantList.get(j).get("variant_code"))
                                    .productNo(product_No_int)
                                    .systemId("cafe24")
                                    .optionQty(variantOptionsList.size())
                                    .build();
                            products.add(product_List);

                            for (int t = 0; t < variantOptionsList.size(); t++) {
                                LocalDateTime now = LocalDateTime.now();
                                VariantOption variantOption = VariantOption.builder()
                                        .mallId(mallid)
                                        .systemId("cafe24")
                                        .optionKey(String.valueOf(t))
                                        .variantCode((String) variantList.get(j).get("variant_code"))
                                        .optionName((String) variantOptionsList.get(t).get("name"))
                                        .optionValue((String) variantOptionsList.get(t).get("value"))
                                        .build();
                                VariantOptions.add(variantOption);

                            }
                        }
                    }
                }

            product_repository.saveAll(productOptionNull);
            product_repository.saveAll(products);
            options_repository.saveAll(VariantOptions);
        }catch(Exception e){
            log.error("에러에러에러");
        }
        }
    }
    @Transactional
    @Override
    public void upDate_Product_Qty(HashMap<String,Object> upDate_QtyData) throws ParseException {
        authMaster  = cafe24_auth_service.refreshTokenIssued((String) upDate_QtyData.get("mall_id"));
                System.out.println(upDate_QtyData.get("upDate_Item_List"));
        List<HashMap<String, Object>> request_Item = (List<HashMap<String, Object>>) upDate_QtyData.get("upDate_Item_List");
//                System.out.println(request_Item.get(0).get("request_Item"));
        for (int i = 0; i < request_Item.size(); i++) {

            HttpResponse<String> response = Unirest.put("https://"+upDate_QtyData.get("mall_id")+".cafe24api.com/api/v2/admin/products/"+request_Item.get(i).get("product_no")+"/variants")
                    .header("Content-Type", apiCallDto.getInsert_Content_Type())
                    .header("Authorization", "Bearer "+ authMaster.getAccessToken())
                    .header("X-Cafe24-Api-Version", apiCallDto.getX_Cafe24_Api_Version())
                    .header("X-Cafe24-Client-Id", apiCallDto.getX_Cafe24_Client_Id())
                    .body(request_Item.get(i).get("request_Item"))
                    .asString();
            log.info(response.getBody());
        }
     }
    }

