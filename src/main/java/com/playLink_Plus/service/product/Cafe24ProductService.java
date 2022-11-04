package com.playLink_Plus.service.product;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.playLink_Plus.entity.AuthMaster;
import com.playLink_Plus.entity.ProductMaster;
import com.playLink_Plus.entity.ProductDetail;
import com.playLink_Plus.repository.OptionsRepository;
import com.playLink_Plus.repository.ProductRepository;
import com.playLink_Plus.service.ProductServiceInterface;
import com.playLink_Plus.service.auth.Cafe24AuthService;
import com.playLink_Plus.vo.ApiVo;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class Cafe24ProductService implements ProductServiceInterface {
    final Cafe24AuthService cafe24_auth_service;

    final OptionsRepository options_repository;

    final ProductRepository product_repository;

    AuthMaster authMaster;

    ProductMaster productMaster;

    ApiVo apiVo = new ApiVo();

    @Transactional
    @Override   // 쇼핑몰 상품 수집 Service
    public void issuedProductItem(String mallid) {

        System.out.println("Cafe24" + mallid);
        authMaster  = cafe24_auth_service.refreshTokenIssued(mallid);
        HttpResponse<String> response = Unirest.get("https://" + mallid + ".cafe24api.com/api/v2/products/count")
                .header("Content-Type", apiVo.getContentType())
                .header("X-Cafe24-Api-Version", apiVo.getCafe24ApiVersion())
                .header("'Authorization", "Bearer " + authMaster.getAccessToken())
                .header("X-Cafe24-Client-Id", apiVo.getCafe24ClientId())
                .asString();

        System.out.println(response.getBody());
        JSONParser parser = new JSONParser();
        JSONObject ProductCountData = new JSONObject();
        try {
            ProductCountData = (JSONObject) parser.parse(response.getBody());

        } catch (Exception e) {
            log.error("전체 상품 갯수 요청 중 오류");
        }

        int offsetInt = 0;
        String num = ProductCountData.get("count").toString();

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
        List<ProductDetail> VariantOptions = new ArrayList<>();
        for (int q = 0; q <= offsetInt;q++) {
            AuthMaster refreshTokenIssued = cafe24_auth_service.refreshTokenIssued(mallid);
            HttpResponse<String> response2 = Unirest.get("https://" + mallid + ".cafe24api.com/api/v2/products?offset=" + q + "00&limit=100")
                    .header("Content-Type", apiVo.getContentType())
                    .header("X-Cafe24-Api-Version", apiVo.getCafe24ApiVersion())
                    .header("'Authorization", "Bearer " + authMaster.getAccessToken())
                    .header("X-Cafe24-Client-Id", apiVo.getCafe24ClientId())
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
                            .header("Content-Type", apiVo.getContentType())
                            .header("X-Cafe24-Api-Version", apiVo.getCafe24ApiVersion())
                            .header("'Authorization", "Bearer " + refreshTokenIssued.getAccessToken())
                            .header("X-Cafe24-Client-Id", apiVo.getCafe24ClientId())
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
                                    .systemId("cafe24")
                                    .optionQty(variantOptionsList.size())
                                    .build();
                            products.add(product_List);

                            for (int t = 0; t < variantOptionsList.size(); t++) {
                                LocalDateTime now = LocalDateTime.now();
                                ProductDetail variantOption = ProductDetail.builder()
                                        .mallId(mallid)
                                        .systemId("cafe24")
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
    @Override //재고수량 안전재고 입력 Service
    public void upDateProductQty(HashMap<String, Object> upDateQtyData) throws ParseException {
        authMaster = cafe24_auth_service.refreshTokenIssued((String) upDateQtyData.get("mall_id"));
        System.out.println(upDateQtyData.get("upDate_Item_List"));
        List<HashMap<String, Object>> request_Item = (List<HashMap<String, Object>>) upDateQtyData.get("upDate_Item_List");
        for (int i = 0; i < request_Item.size(); i++) {

            HttpResponse<String> response = Unirest.put("https://" + upDateQtyData.get("mall_id") + ".cafe24api.com/api/v2/admin/products/" + request_Item.get(i).get("product_no") + "/variants")
                    .header("Content-Type", apiVo.getInsertContentType())
                    .header("Authorization", "Bearer " + authMaster.getAccessToken())
                    .header("X-Cafe24-Api-Version", apiVo.getCafe24ApiVersion())
                    .header("X-Cafe24-Client-Id", apiVo.getCafe24ClientId())
                    .body(request_Item.get(i).get("request_Item"))
                    .asString();
            log.info(response.getBody());
        }
    }

    @Override
    public String makeProductDataXml() {

        return null;
    }

    @Override
    public void insertProductTest() {

    }


}

