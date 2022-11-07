package com.playLink_Plus.service.product;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.playLink_Plus.ApiUrl;
import com.playLink_Plus.entity.AuthMaster;
import com.playLink_Plus.entity.ProductMaster;
import com.playLink_Plus.entity.ProductDetail;
import com.playLink_Plus.repository.ProductDetailRepository;
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

    final ProductDetailRepository options_repository;

    final ProductRepository product_repository;

    AuthMaster authMaster;

    ProductMaster productMaster;

    ApiVo apiVo = new ApiVo();

    ArrayList<String> urlVariable = new ArrayList<>();
    private HttpResponse<String> cafe24apiUrl(HashMap<String, Object> reqData, ArrayList<String> urlVariable){

     String ApiUrl = null;

        System.out.println(urlVariable);
        switch (urlVariable.get(0)){
            case "count":
                ApiUrl = "/count";
                break;
            case "productInfo":
                ApiUrl = "?offset="+urlVariable.get(1)+"00&limit=100";
                break;
            case "variantInfo":
                ApiUrl = "/"+urlVariable.get(1) + "/variants";
                break;
            case "createProductCount":
                ApiUrl = "/count?" + urlVariable.get(1) + "="+urlVariable.get(2)+"&" + urlVariable.get(3) + "="+urlVariable.get(4);
                break;
            case "createProductInfo":
                ApiUrl = "?offset=" + urlVariable.get(1) + "00&limit=100&" + urlVariable.get(2) + "="+urlVariable.get(3)+"&" + urlVariable.get(4) + "="+urlVariable.get(5);
                break;

        }

        System.out.println("https://" + reqData.get("mallId") + ".cafe24api.com/api/v2/admin/products/"+ApiUrl);
        HttpResponse<String> response = Unirest.get("https://" + reqData.get("mallId") + ".cafe24api.com/api/v2/admin/products"+ApiUrl)
                .header("Content-Type", apiVo.getContentType())
                .header("X-Cafe24-Api-Version", apiVo.getCafe24ApiVersion())
                .header("Authorization", "Bearer " + this.authMaster.getAccessToken())
                .asString();

        urlVariable.clear();

        return response;
    }


    @Transactional
    @Override   // 쇼핑몰 상품 수집 Service
    public void issuedProductItem(HashMap<String, Object> reqData) {


        authMaster = cafe24_auth_service.refreshTokenIssued((String) reqData.get("mallId"));

        urlVariable.add("count");
        HttpResponse<String> responseProductCount = cafe24apiUrl(reqData,urlVariable);

        JSONParser parser = new JSONParser();
        JSONObject ProductCountData = new JSONObject();
        try {
            ProductCountData = (JSONObject) parser.parse(responseProductCount.getBody());

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

        for (int q = 0; q <= offsetInt; q++) {

            urlVariable.add("productInfo");
            urlVariable.add(String.valueOf(q));
            HttpResponse<String> responseProductInfo = cafe24apiUrl(reqData,urlVariable);

            try {

                Gson gson = new Gson();
                Map<String, Object> productData = gson.fromJson(responseProductInfo.getBody(), new TypeToken<Map<String, Object>>() {
                }.getType());
                List<Map<String, Object>> productList = (List) productData.get("products");


                for (int i = 0; i < productList.size(); i++) {

                    String product_name = (String) productList.get(i).get("product_name");
                    String product_code = (String) productList.get(i).get("product_code");

                    double product_No = (double) productList.get(i).get("product_no");
                    int product_No_int = (int) product_No;

                    Thread.sleep(250);


                    urlVariable.add("variantInfo");
                    urlVariable.add(String.valueOf(product_No_int));
                    HttpResponse<String> responseVariantInfo = cafe24apiUrl(reqData,urlVariable);
                    System.out.println(responseVariantInfo.getBody());
                    Map<String, Object> variantData = gson.fromJson(responseVariantInfo.getBody(), new TypeToken<Map<String, Object>>() {
                    }.getType());
                    List<Map<String, Object>> variantList = (List) variantData.get("variants");


                    for (int j = 0; j < variantList.size(); j++) {

                        if (variantList.get(j).get("options") == null) {
                            ProductMaster product_List = ProductMaster.builder()
                                    .mallId((String) reqData.get("mallId"))
                                    .productCode(product_code)
                                    .productName(product_name)
                                    .systemId("cafe24")
                                    .productNo(product_No_int)
                                    .option_yn("N")
                                    .optionQty(0).build();
                            productOptionNull.add(product_List);

                        } else {

                            List<Map<String, Object>> variantOptionsList = (List) variantList.get(j).get("options");

                            ProductMaster product_List = ProductMaster.builder()
                                    .mallId((String) reqData.get("mallId"))
                                    .productName(product_name)
                                    .productCode(product_code)
                                    .systemId("cafe24")
                                    .productNo(product_No_int)
                                    .option_yn("Y")
                                    .optionQty(variantOptionsList.size())
                                    .build();
                            products.add(product_List);

                            for (int t = 0; t < variantOptionsList.size(); t++) {
                                LocalDateTime now = LocalDateTime.now();
                                ProductDetail variantOption = ProductDetail.builder()
                                        .mallId((String) reqData.get("mallId"))
                                        .systemId("cafe24")
                                        .variantCode((String) variantList.get(j).get("variant_code"))
                                        .optionName((String) variantOptionsList.get(t).get("name"))
                                        .productCode(product_code)
                                        .optionNum(t)
                                        .optionValue((String) variantOptionsList.get(t).get("value"))
                                        .createdDate((String) productList.get(i).get("created_date"))
                                        .build();
                                VariantOptions.add(variantOption);

                            }
                        }
                    }
                }

                product_repository.saveAll(productOptionNull);
                product_repository.saveAll(products);
                options_repository.saveAll(VariantOptions);
            } catch (Exception e) {
                log.error("에러에러에러");
            }
        }
    }


    @Transactional
    @Override
    public void checkProductInfo(HashMap<String, Object> reqData) {

               String startDate = "created_start_date";
               String endDate = "created_end_date";

            System.out.println(startDate);

            authMaster = cafe24_auth_service.refreshTokenIssued((String) reqData.get("mallId"));
            System.out.println(authMaster);


        urlVariable.add("createProductCount");
        urlVariable.add(startDate);
        urlVariable.add((String) reqData.get("startDate"));
        urlVariable.add(endDate);
        urlVariable.add((String) reqData.get("endDate"));

        HttpResponse<String> responseCreateProductCount = cafe24apiUrl(reqData,urlVariable);

            System.out.println(responseCreateProductCount.getBody());
            JSONParser parser = new JSONParser();
            JSONObject ProductCountData = new JSONObject();

            try {
                ProductCountData = (JSONObject) parser.parse(responseCreateProductCount.getBody());

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

            for (int q = 0; q <= offsetInt; q++) {


                try {


                    urlVariable.add("createProductInfo");
                    urlVariable.add(String.valueOf(q));
                    urlVariable.add(startDate);
                    urlVariable.add((String) reqData.get("startDate"));
                    urlVariable.add(endDate);
                    urlVariable.add((String) reqData.get("endDate"));

                    HttpResponse<String> responseCreateProductInfo = cafe24apiUrl(reqData,urlVariable);
                    Gson gson = new Gson();
                    Map<String, Object> productData = gson.fromJson(responseCreateProductInfo.getBody(), new TypeToken<Map<String, Object>>() {
                    }.getType());
                    List<Map<String, Object>> productList = (List) productData.get("products");


                    for (int i = 0; i < productList.size(); i++) {

                        String product_name = (String) productList.get(i).get("product_name");
                        String product_code = (String) productList.get(i).get("product_code");

                        double product_No = (double) productList.get(i).get("product_no");
                        int product_No_int = (int) product_No;

                        Thread.sleep(250);

                       urlVariable.add("variantInfo");
                    urlVariable.add(String.valueOf(product_No_int));
                    HttpResponse<String> responseVariantInfo = cafe24apiUrl(reqData,urlVariable);
                    System.out.println(responseVariantInfo.getBody());

                        Map<String, Object> variantData = gson.fromJson(responseVariantInfo.getBody(), new TypeToken<Map<String, Object>>() {
                        }.getType());
                        List<Map<String, Object>> variantList = (List) variantData.get("variants");


                        for (int j = 0; j < variantList.size(); j++) {

                            if (variantList.get(j).get("options") == null) {
                                ProductMaster product_List = ProductMaster.builder()
                                        .mallId((String) reqData.get("mallId"))
                                        .productCode(product_code)
                                        .productName(product_name)
                                        .systemId("cafe24")
                                        .productNo(product_No_int)
                                        .option_yn("N")
                                        .optionQty(0).build();
                                productOptionNull.add(product_List);

                            } else {

                                List<Map<String, Object>> variantOptionsList = (List) variantList.get(j).get("options");

                                ProductMaster product_List = ProductMaster.builder()
                                        .mallId((String) reqData.get("mallId"))
                                        .productName(product_name)
                                        .productCode(product_code)
                                        .systemId("cafe24")
                                        .productNo(product_No_int)
                                        .option_yn("Y")
                                        .optionQty(variantOptionsList.size())
                                        .build();
                                products.add(product_List);

                                for (int t = 0; t < variantOptionsList.size(); t++) {
                                    LocalDateTime now = LocalDateTime.now();
                                    ProductDetail variantOption = ProductDetail.builder()
                                            .mallId((String) reqData.get("mallId"))
                                            .systemId("cafe24")
                                            .productCode(product_code)
                                            .variantCode((String) variantList.get(j).get("variant_code"))
                                            .optionName((String) variantOptionsList.get(t).get("name"))
                                            .optionValue((String) variantOptionsList.get(t).get("value"))
                                            .createdDate((String) productList.get(i).get("created_date"))
                                            .updated_date((String) productList.get(i).get("updated_date"))
                                            .optionNum(t)
                                            .build();
                                    VariantOptions.add(variantOption);

                                }
                            }
                        }
                    }

                    product_repository.saveAll(productOptionNull);
                    product_repository.saveAll(products);
                    options_repository.saveAll(VariantOptions);
                } catch (Exception e) {
                    log.error("에러에러에러");
                }
            }

    }

    @Transactional
    @Override //재고수량 안전재고 입력 Service
    public void upDateProductQty(HashMap<String, Object> upDateQtyData) throws ParseException {
//        authMaster = cafe24_auth_service.refreshTokenIssued((String) upDateQtyData.get("mall_id"));

        System.out.println(upDateQtyData);


//            HttpResponse<String> response = Unirest.put("https://" + upDateQtyData.get("mall_id") + ".cafe24api.com/api/v2/admin/products/" + request_Item.get(i).get("product_no") + "/variants")
//                    .header("Content-Type", apiVo.getInsertContentType())
//                    .header("Authorization", "Bearer " + authMaster.getAccessToken())
//                    .header("X-Cafe24-Api-Version", apiVo.getCafe24ApiVersion())
//                    .header("X-Cafe24-Client-Id", apiVo.getCafe24ClientId())
//                    .body(request_Item.get(i).get("request_Item"))
//                    .asString();
//            log.info(response.getBody());

    }

    @Override
    public String makeProductDataXml() {

        return null;
    }

    @Override
    public void insertProductTest() {

    }


}

