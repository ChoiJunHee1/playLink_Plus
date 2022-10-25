package com.playLink_Plus.service.product;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.playLink_Plus.dto.ApiCallDto;
import com.playLink_Plus.entity.AuthMaster;
import com.playLink_Plus.entity.ProductMaster;
import com.playLink_Plus.entity.VariantOption;
import com.playLink_Plus.repository.Options_Repository;
import com.playLink_Plus.repository.Product_Repository;
import com.playLink_Plus.service.Product_Service_Interface;
import com.playLink_Plus.service.auth.Cafe24_Auth_Service;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public Cafe24_Product_Service(Cafe24_Auth_Service cafe24_auth_service, Options_Repository options_repository, Product_Repository product_repository) {
        this.cafe24_auth_service = cafe24_auth_service;
        this.options_repository = options_repository;
        this.product_repository = product_repository;
    }

    ProductMaster productMaster;



    ApiCallDto apiCallDto = new ApiCallDto();

    @Transactional
    @Override
    public void issued_Product_Item(String mall_id, AuthMaster authMaster) {
        HttpResponse<String> response = Unirest.get("https://" + mall_id + ".cafe24api.com/api/v2/products/count")
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
            AuthMaster refreshTokenIssued = cafe24_auth_service.refreshTokenIssued(mall_id);
            HttpResponse<String> response2 = Unirest.get("https://" + mall_id + ".cafe24api.com/api/v2/products?offset=" + q + "00&limit=100")
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

                    Thread.sleep(500);
                    HttpResponse<String> response3 = Unirest.get("https://" + mall_id + ".cafe24api.com/api/v2/products/" + product_No_int + "/variants")
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
                                    .mallId(mall_id).productName(product_name)
                                    .productCode(product_code)
                                    .productName(product_name)
                                    .variantCode((String) variantList.get(j).get("variant_code"))
                                    .optionQty(0).build();
                            productOptionNull.add(product_List);

                        } else {

                            List<Map<String, Object>> variantOptionsList = (List) variantList.get(j).get("options");

                            ProductMaster product_List = ProductMaster.builder()
                                    .mallId(mall_id)
                                    .productName(product_name)
                                    .productCode(product_code)
                                    .productName(product_name)
                                    .variantCode((String) variantList.get(j).get("variant_code"))
                                    .optionQty(variantOptionsList.size())
                                    .build();
                            products.add(product_List);

                            for (int t = 0; t < variantOptionsList.size(); t++) {
                                VariantOption variantOption = VariantOption.builder()
                                        .mallId(mall_id)
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
}