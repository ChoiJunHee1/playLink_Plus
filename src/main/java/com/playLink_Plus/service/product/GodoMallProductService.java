package com.playLink_Plus.service.product;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.playLink_Plus.dto.InsertProductOptionXml;
import com.playLink_Plus.dto.InsertProductXml;
import com.playLink_Plus.dto.InsertProductXmlList;
import com.playLink_Plus.entity.AuthMaster;
import com.playLink_Plus.entity.ProductMaster;
import com.playLink_Plus.entity.ProductDetail;
import com.playLink_Plus.repository.AuthRepository;
import com.playLink_Plus.repository.ProductDetailRepository;
import com.playLink_Plus.repository.ProductRepository;
import com.playLink_Plus.service.ProductServiceInterface;
import com.playLink_Plus.xmlType.StockGoodsData;
import com.playLink_Plus.xmlType.StockGoodsDataList;
import com.playLink_Plus.xmlType.StockOptionData;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.json.XML;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.StringWriter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GodoMallProductService implements ProductServiceInterface {

    AuthMaster authMaster;
    final AuthRepository authRepository;
    final ProductRepository productRepository;
    final ProductDetailRepository productDetailRepository;
    ProductMaster productMaster;
    Gson gson = new Gson();
    List<ProductMaster> products = new ArrayList<>();
    List<ProductMaster> productsOptionNull = new ArrayList<>();
    List<ProductDetail> variantOptions = new ArrayList<>();
    ArrayList<String> urlVariable = new ArrayList<>();

    private HttpResponse<String> godoMallApiUrl(HashMap<String, Object> reqData, ArrayList<String> urlVariable) {

        authMaster = authRepository.findByMallId((String) reqData.get("mallId"));
        String ApiUrl = null;

        HttpResponse<String> response = null;
        System.out.println(urlVariable);
        switch (urlVariable.get(0)) {
            case "size":
                ApiUrl = "&size=100";
                break;
            case "productInfo":
                ApiUrl = "&size=100&page=" + urlVariable.get(1);
                break;
            case "regDateSize":
                ApiUrl = "&size=100&searchDateType=regDt&startDate=" + urlVariable.get(1) + "&endDate=" + urlVariable.get(2);
                break;
            case "regDateSearchProductInfo":
                ApiUrl = "&size=100&page=" + urlVariable.get(1) + "&searchDateType=regDt&startDate=" + urlVariable.get(2) + "&endDate=" + urlVariable.get(3);
                break;

        }
        try {
            System.out.println("https://openhub.godo.co.kr/godomall5/goods/Goods_Search.php?partner_key="
                    + authMaster.getAuthorizationCode() + "&key=" + authMaster.getAccessToken() + ApiUrl);

            response = Unirest.post
                            ("https://openhub.godo.co.kr/godomall5/goods/Goods_Search.php?partner_key="
                                    + authMaster.getAuthorizationCode() + "&key=" + authMaster.getAccessToken() + ApiUrl)
                    .asString();

        } catch (Exception e) {
            log.error(urlVariable.get(0) + "요청중 error");
            log.error("https://openhub.godo.co.kr/godomall5/goods/Goods_Search.php?partner_key="
                    + authMaster.getAuthorizationCode() + "&key=" + authMaster.getAccessToken() + ApiUrl);
            log.error(response.getBody());
        }

        urlVariable.clear();
        log.info(response.getBody());
        log.info("success");
        return response;
    }


    @Transactional
    @Override
    public void issuedProductItem(HashMap<String, Object> reqData) {


        urlVariable.add("size");
        HttpResponse<String> responseMaxPage = godoMallApiUrl(reqData, urlVariable);

        JSONObject jsonObjectMaxPage = XML.toJSONObject(responseMaxPage.getBody());
        String strMaxPage = jsonObjectMaxPage.toString(4);

        Map<String, Object> productPage = gson.fromJson(strMaxPage, new TypeToken<Map<String, Object>>() {
        }.getType());
        Map<String, Object> reqPageData = (Map<String, Object>) productPage.get("data");
        Map<String, Object> maxPage = (Map<String, Object>) reqPageData.get("header");
        String maxPageStr = (String) maxPage.get("max_page");
        int max_page = Integer.parseInt(maxPageStr);
        for (int i = 1; i <= max_page; i++) {
            urlVariable.add("productInfo");
            urlVariable.add(String.valueOf(i));
            HttpResponse<String> responseProductInfo = godoMallApiUrl(reqData, urlVariable);

            JSONObject jsonObjectProductInfo = XML.toJSONObject(responseProductInfo.getBody());
            String StrObjectProductInfo = jsonObjectProductInfo.toString(4);
            Map<String, Object> productData = gson.fromJson(StrObjectProductInfo, new TypeToken<Map<String, Object>>() {
            }.getType());
            Map<String, Object> requestData = (Map<String, Object>) productData.get("data");
            Map<String, Object> returnData = (Map<String, Object>) requestData.get("return");
            if (returnData.get("goods_data") instanceof Map) {
                Map<String, Object> goodsData = (Map<String, Object>) returnData.get("goods_data");
                System.out.println(goodsData);
                double goodsNo = (double) goodsData.get("goodsNo");
                int goodsNoInt = (int) goodsNo;
                if (goodsData.get("optionFl").equals("y")) {
                    List<Map<String, Object>> optionData = (List<Map<String, Object>>) goodsData.get("optionData");

                    for (int l = 0; l < optionData.size(); l++) {

                        double sno = (double) optionData.get(l).get("sno");
                        int snoInt = (int) sno;

                        ProductMaster product_List = ProductMaster.builder()
                                .mallId((String) reqData.get("mallId"))
                                .productCode(String.valueOf(goodsNoInt))
                                .productName((String) goodsData.get("goodsNm"))
                                .systemId("godoMall")
                                .option_yn((String) goodsData.get("optionFl"))
                                .optionQty(optionData.size()).build();

                        String str = goodsData.get("optionName").toString();
                        String[] option = str.split("\\^\\|\\^");

                        products.add(product_List);
                        for (int t = 0; t < option.length; t++) {
                            ProductDetail variantOption = ProductDetail.builder()
                                    .mallId((String) reqData.get("mallId"))
                                    .systemId("godoMall")
                                    .variantCode(String.valueOf(snoInt))
                                    .optionName(option[t])
                                    .productCode(String.valueOf(goodsNoInt))
                                    .optionNum(t)
                                    .optionValue((String) optionData.get(l).get("optionValue" + (t + 1)))
                                    .createdDate((String) optionData.get(l).get("regDt"))
//                                    .optionPrice((Integer) optionData.get(l).get("optionPrice"))
//                                    .optionViewFl((String) optionData.get(l).get("optionViewFl"))
//                                    .optionSellFl((String) optionData.get(l).get("optionSellFl"))
                                    .build();
                            variantOptions.add(variantOption);
                        }
                    }
                } else {

                    ProductMaster product_List = ProductMaster.builder()
                            .mallId((String) reqData.get("mallId"))
                            .productCode(String.valueOf(goodsNoInt))
                            .productName((String) goodsData.get("goodsNm"))
                            .systemId("godoMall")
                            .option_yn((String) goodsData.get("optionFl"))
                            .productCode(String.valueOf(goodsNoInt))
                            .optionQty(0).build();
                    productsOptionNull.add(product_List);

                }
            } else {

                List<Map<String, Object>> goodsData = (List<Map<String, Object>>) returnData.get("goods_data");

                for (int j = 0; j < goodsData.size(); j++) {
//                    System.out.println(goodsData);
                    double goodsNo = (double) goodsData.get(j).get("goodsNo");
                    int goodsNoInt = (int) goodsNo;
                    if (goodsData.get(j).get("optionFl").equals("y")) {
                        List<Map<String, Object>> optionData = (List<Map<String, Object>>) goodsData.get(j).get("optionData");

                        for (int l = 0; l < optionData.size(); l++) {

                            double sno = (double) optionData.get(l).get("sno");
                            int snoInt = (int) sno;

                            ProductMaster product_List = ProductMaster.builder()
                                    .mallId((String) reqData.get("mallId"))
                                    .productCode(String.valueOf(goodsNoInt))
                                    .productName((String) goodsData.get(j).get("goodsNm"))
                                    .systemId("godoMall")
                                    .option_yn((String) goodsData.get(j).get("optionFl"))
                                    .optionQty(optionData.size()).build();

                            String str = goodsData.get(j).get("optionName").toString();
                            String[] option = str.split("\\^\\|\\^");

                            products.add(product_List);
                            for (int t = 0; t < option.length; t++) {
                                System.out.println(optionData.get(l).get("optionPrice"));
                                ProductDetail variantOption = ProductDetail.builder()
                                        .mallId((String) reqData.get("mallId"))
                                        .systemId("godoMall")
                                        .variantCode(String.valueOf(snoInt))
                                        .optionName(option[t])
                                        .productCode(String.valueOf(goodsNoInt))
                                        .optionNum(t)
                                        .optionValue((String) optionData.get(l).get("optionValue" + (t + 1)))
                                        .createdDate((String) optionData.get(l).get("regDt"))
//                                        .optionPrice((Integer) optionData.get(l).get("optionPrice"))
//                                        .optionViewFl((String) optionData.get(l).get("optionViewFl"))
//                                        .optionSellFl((String) optionData.get(l).get("optionSellFl"))
                                        .build();
                                variantOptions.add(variantOption);
                            }
                        }
                    } else {

                        ProductMaster product_List = ProductMaster.builder()
                                .mallId((String) reqData.get("mallId"))
                                .productCode(String.valueOf(goodsNoInt))
                                .productName((String) goodsData.get(j).get("goodsNm"))
                                .systemId("godoMall")
                                .option_yn((String) goodsData.get(j).get("optionFl"))
                                .productCode(String.valueOf(goodsNoInt))
                                .optionQty(0).build();
                        productsOptionNull.add(product_List);

                    }
                }
            }
        }
        productRepository.saveAll(productsOptionNull);
        productRepository.saveAll(products);
        productDetailRepository.saveAll(variantOptions);
    }

    @Transactional
    @Override
    public void regDateSearchProductInfo(HashMap<String, Object> reqData) {

        authMaster = authRepository.findByMallId((String) reqData.get("mallId"));

        urlVariable.add("regDateSize");
        urlVariable.add((String) reqData.get("startDate"));
        urlVariable.add((String) reqData.get("endDate"));
        HttpResponse<String> responseRegDateSize = godoMallApiUrl(reqData, urlVariable);

        JSONObject jsonObjectRegDateSize = XML.toJSONObject(responseRegDateSize.getBody());
        String strRegDateSize = jsonObjectRegDateSize.toString(4);

        Map<String, Object> productPage = gson.fromJson(strRegDateSize, new TypeToken<Map<String, Object>>() {
        }.getType());
        Map<String, Object> reqPageData = (Map<String, Object>) productPage.get("data");
        Map<String, Object> maxPage = (Map<String, Object>) reqPageData.get("header");
            String maxPageStr = (String) maxPage.get("max_page");
            if(!maxPageStr.equals("0")) {
            int max_page = Integer.parseInt(maxPageStr);
            for (int i = 0; i < max_page; i++) {

                urlVariable.add("regDateSearchProductInfo");
                urlVariable.add(String.valueOf(i));
                urlVariable.add((String) reqData.get("startDate"));
                urlVariable.add((String) reqData.get("endDate"));
                HttpResponse<String> responseRegDateProductInfo = godoMallApiUrl(reqData, urlVariable);

                JSONObject jsonObjectRegDateProductInfo = XML.toJSONObject(responseRegDateProductInfo.getBody());
                String strRegDateProductInfo = jsonObjectRegDateProductInfo.toString(4);
                Map<String, Object> regProductData = gson.fromJson(strRegDateProductInfo, new TypeToken<Map<String, Object>>() {
                }.getType());
                Map<String, Object> requestData = (Map<String, Object>) regProductData.get("data");
                Map<String, Object> returnData = (Map<String, Object>) requestData.get("return");

                if (returnData.get("goods_data") instanceof Map) {
                    Map<String, Object> goodsData = (Map<String, Object>) returnData.get("goods_data");
                    double goodsNo = (double) goodsData.get("goodsNo");
                    int goodsNoInt = (int) goodsNo;

                    if (goodsData.get("optionFl").equals("y")) {
                        List<Map<String, Object>> optionData = (List<Map<String, Object>>) goodsData.get("optionData");
                        for (int l = 0; l < optionData.size(); l++) {
                            double sno = (double) optionData.get(l).get("sno");
                            int snoInt = (int) sno;

                            ProductMaster product_List = ProductMaster.builder()
                                    .mallId((String) reqData.get("mallId"))
                                    .productCode(String.valueOf(goodsNoInt))
                                    .productName((String) goodsData.get("goodsNm"))
                                    .systemId("godoMall")
                                    .option_yn((String) goodsData.get("optionFl"))
                                    .optionQty(optionData.size()).build();

                            String str = goodsData.get("optionName").toString();
                            String[] option = str.split("\\^\\|\\^");

                            products.add(product_List);
                            for (int t = 0; t < option.length; t++) {
                                ProductDetail variantOption = ProductDetail.builder()
                                        .mallId((String) reqData.get("mallId"))
                                        .systemId("godoMall")
                                        .variantCode(String.valueOf(snoInt))
                                        .optionName(option[t])
                                        .productCode(String.valueOf(goodsNoInt))
                                        .optionNum(t)
                                        .optionValue((String) optionData.get(l).get("optionValue" + (t + 1)))
                                        .createdDate((String) optionData.get(l).get("regDt"))
//                                        .optionPrice((Integer) optionData.get(l).get("optionPrice"))
//                                        .optionViewFl((String) optionData.get(l).get("optionViewFl"))
//                                        .optionSellFl((String) optionData.get(l).get("optionSellFl"))
                                        .build();
                                variantOptions.add(variantOption);
                            }
                        }
                    } else {

                        ProductMaster product_List = ProductMaster.builder()
                                .mallId((String) reqData.get("mallId"))
                                .productCode(String.valueOf(goodsNoInt))
                                .productName((String) goodsData.get("goodsNm"))
                                .systemId("godoMall")
                                .option_yn((String) goodsData.get("optionFl"))
                                .optionQty(0).build();
                        productsOptionNull.add(product_List);

                    }
                } else {
                    List<Map<String, Object>> goodsData = (List<Map<String, Object>>) returnData.get("goods_data");

                    for (int j = 0; j < goodsData.size(); j++) {

                        double goodsNo = (double) goodsData.get(j).get("goodsNo");
                        int goodsNoInt = (int) goodsNo;

                        if (goodsData.get(j).get("optionFl").equals("y")) {
                            List<Map<String, Object>> optionData = (List<Map<String, Object>>) goodsData.get(j).get("optionData");
                            for (int l = 0; l < optionData.size(); l++) {
                                double sno = (double) optionData.get(l).get("sno");
                                int snoInt = (int) sno;

                                ProductMaster product_List = ProductMaster.builder()
                                        .mallId((String) reqData.get("mallId"))
                                        .productCode(String.valueOf(goodsNoInt))
                                        .productName((String) goodsData.get(j).get("goodsNm"))
                                        .systemId("godoMall")
                                        .option_yn((String) goodsData.get(j).get("optionFl"))
                                        .optionQty(optionData.size()).build();

                                String str = goodsData.get(j).get("optionName").toString();
                                String[] option = str.split("\\^\\|\\^");

                                products.add(product_List);
                                for (int t = 0; t < option.length; t++) {
                                    ProductDetail variantOption = ProductDetail.builder()
                                            .mallId((String) reqData.get("mallId"))
                                            .systemId("godoMall")
                                            .variantCode(String.valueOf(snoInt))
                                            .optionName(option[t])
                                            .productCode(String.valueOf(goodsNoInt))
                                            .optionNum(t)
                                            .optionValue((String) optionData.get(l).get("optionValue" + (t + 1)))
                                            .createdDate((String) optionData.get(l).get("regDt"))
//                                            .optionPrice((Integer) optionData.get(l).get("optionPrice"))
//                                            .optionViewFl((String) optionData.get(l).get("optionViewFl"))
//                                            .optionSellFl((String) optionData.get(l).get("optionSellFl"))
                                            .build();
                                    variantOptions.add(variantOption);
                                }
                            }
                        } else {

                            ProductMaster product_List = ProductMaster.builder()
                                    .mallId((String) reqData.get("mallId"))
                                    .productCode(String.valueOf(goodsNoInt))
                                    .productName((String) goodsData.get(j).get("goodsNm"))
                                    .systemId("godoMall")
                                    .option_yn((String) goodsData.get(j).get("optionFl"))
                                    .optionQty(0).build();
                            productsOptionNull.add(product_List);

                        }
                    }
                }
            }
            productRepository.saveAll(productsOptionNull);
            productRepository.saveAll(products);
            productDetailRepository.saveAll(variantOptions);
        }else{
                log.info("추가된 상품 정보가 없습니다.");
        }

    }

    @Transactional
    @Override
    public String upDateQtyXmlData(HashMap<String, Object> upDate_QtyData) throws ParseException {

        List<StockGoodsData> stockGoodsData = new ArrayList<StockGoodsData>();
        System.out.println(upDate_QtyData.get("upDate_Item_List"));
        List<Map<String,Object>> upDate_Item_List = (List<Map<String, Object>>) upDate_QtyData.get("upDate_Item_List");
        StringWriter sw = new StringWriter();
        StockGoodsDataList res = new StockGoodsDataList();
     for(int i=0; i < upDate_Item_List.size(); i++) {
         int idx = i+1;
         System.out.println(upDate_Item_List.get(i).get("product_no"));
         Map<String,Object> request_Item = (Map<String, Object>) upDate_Item_List.get(i).get("request_Item");
         List<Map<String,Object>> requests = (List<Map<String, Object>>) request_Item.get("requests");
         List<StockOptionData> stockOptionData = new ArrayList<StockOptionData>();
         for (int j = 0; j < requests.size(); j++) {
             int id = j+1;
             System.out.println(requests.get(j).get("variant_code"));
             System.out.println(requests.get(j).get("quantity"));
             stockOptionData.add(new StockOptionData(id, (String) requests.get(j).get("variant_code"), (Integer) requests.get(j).get("quantity")));
         }
         stockGoodsData.add(new StockGoodsData(idx, (Integer) upDate_Item_List.get(i).get("product_no"), (String) upDate_Item_List.get(i).get("optionFl"), stockOptionData));
     }

        res.setGoods_data(stockGoodsData);
        try {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
            xmlMapper.writeValue(sw, res);
            System.out.println("뀨");
        }catch (Exception e){

        }
        return sw.toString();
    }

    @Transactional
    @Override
    public void upDateStockQty(HashMap<String, Object> upDateQtyData) {

        Unirest.config().socketTimeout(300000);
        HttpResponse<String> response =
                Unirest.post("https://openhub.godo.co.kr/godomall5/goods/Goods_Stock.php?" +
                                "partner_key=JTAxbiVCNyUxNk4lODclODYlREI=" +
                                "&key=TC1zayUwMCVGRXolOUUlNUUlQTVJJUExJUNFJUVEJUU1JTNDVWslRTUlRjclRUY4JTNDJTVDJUQ1JTEyJTVDTiVERCUwNSVGNVglM0IlRjQlMTUlMURHJUNEbSVCQg==" +
                                "&data_url=https://playlink-plus.xmd.co.kr/product/upDateQtyXmlData")
                        .asString();

//        HttpResponse<String> response =
//                Unirest.get("http://localhost:8080/product/upDateQtyXmlData")
//                        .asString();

        System.out.println(response.getBody());

    }

    @Transactional
    @Override
    public String makeProductDataXml() {
        StringWriter sw = new StringWriter();
        String res2 = null;
        try {
            InsertProductXmlList res = new InsertProductXmlList();
            List<InsertProductXml> ProductList = new ArrayList<InsertProductXml>();

            List<InsertProductOptionXml> optionList = new ArrayList<InsertProductOptionXml>();
            for (int j = 1; j < 3; j++) {

                optionList.add(new InsertProductOptionXml(j, j, "블랙" + j, "s" + j, "", "", "", 0, "y", "0"));
            }
            for (int i = 1; i < 301; i++) {
                ProductList.add(new InsertProductXml
                        (i, "001", "001", "준희" + i, "d", "aa", "bb",
                                "cc", "y", "y", "y", "y", "1",
                                "1", "111", "dd", "2018-04-11 00:00:00", "n",
                                "y", "d", "칼라^|^사이즈", optionList));
            }
            res.setGoods_data(ProductList);
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
            xmlMapper.writeValue(sw, res);
            System.out.println("뀨");
            System.out.println("뀨");

        } catch (Exception e) {

        }

        return sw.toString();
    }

}