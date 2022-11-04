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
import com.playLink_Plus.repository.OptionsRepository;
import com.playLink_Plus.repository.ProductRepository;
import com.playLink_Plus.service.ProductServiceInterface;
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
public class GodomallProductService implements ProductServiceInterface {

    AuthMaster authMaster;
    final AuthRepository authRepository;
    final ProductRepository productRepository;
    final OptionsRepository optionsRepository;
    ProductMaster productMaster;
    Gson gson = new Gson();

    @Transactional
    @Override
    public void issuedProductItem(HashMap<String, Object> reqData) {

        List<ProductMaster> products = new ArrayList<>();
        List<ProductDetail> variantOptions = new ArrayList<>();
        authMaster = authRepository.findByMallId((String) reqData.get("mallId"));

        HttpResponse<String> response = Unirest.post
                        ("https://openhub.godo.co.kr/godomall5/goods/Goods_Search.php?partner_key=" + authMaster.getAuthorizationCode() + "&key=" + authMaster.getAccessToken() + "&size=100")
                .asString();

        JSONObject header = XML.toJSONObject(response.getBody());
        String jsonheader = header.toString(4);

        Map<String, Object> productPage = gson.fromJson(jsonheader, new TypeToken<Map<String, Object>>() {
        }.getType());
        Map<String, Object> reqPageData = (Map<String, Object>) productPage.get("data");
        Map<String, Object> maxPage = (Map<String, Object>) reqPageData.get("header");
        String maxPageStr = (String) maxPage.get("max_page");
        int max_page = Integer.parseInt(maxPageStr);
        for (int i = 1; i <= max_page; i++) {
            HttpResponse<String> responseProductInfo = Unirest.post
                            ("https://openhub.godo.co.kr/godomall5/goods/Goods_Search.php?partner_key=" + authMaster.getAuthorizationCode()
                                    + "&key=" + authMaster.getAccessToken() + "&size=100&page=" + i)
                    .asString();

            JSONObject jsonObject = XML.toJSONObject(responseProductInfo.getBody());
            System.out.println(jsonObject);
            String jsonStr = jsonObject.toString(4);
            Map<String, Object> productData = gson.fromJson(jsonStr, new TypeToken<Map<String, Object>>() {
            }.getType());
            Map<String, Object> requestData = (Map<String, Object>) productData.get("data");
            Map<String, Object> returnData = (Map<String, Object>) requestData.get("return");
            List<Map<String, Object>> goodsData = (List<Map<String, Object>>) returnData.get("goods_data");

                for (int j = 0; j < goodsData.size(); j++) {
                    List<Map<String, Object>> optionData = (List<Map<String, Object>>) goodsData.get(j).get("optionData");
                    if (optionData.size() != 0) {
                        for (int l = 0; l < optionData.size(); l++) {
                            double goodsNo = (double) optionData.get(l).get("goodsNo");
                            int goodsNoInt = (int) goodsNo;
                            double sno = (double) optionData.get(l).get("sno");
                            int snoInt = (int) sno;
                            ProductMaster product_List = ProductMaster.builder()
                                    .mallId((String) reqData.get("mallId"))
                                    .productCode(String.valueOf(goodsNoInt))
                                    .productName((String) goodsData.get(j).get("goodsNm"))
                                    .variantCode(String.valueOf(snoInt))
                                    .systemId("godoMall")
                                    .optionQty(optionData.size()).build();

                            String str = goodsData.get(j).get("optionName").toString();
                            String [] option = str.split("\\^\\|\\^");

                            products.add(product_List);
                            for(int t = 0; t < option.length; t ++){
                                ProductDetail variantOption = ProductDetail.builder()
                                        .mallId((String) reqData.get("mallId"))
                                        .variantCode(String.valueOf(snoInt))
                                        .optionName(option[t])
                                        .optionValue((String) optionData.get(l).get("optionValue"+(t+1)))
                                        .systemId("godoMall")
                                        .createdDate((String) optionData.get(i).get("regDt"))
                                        .build();
                                variantOptions.add(variantOption);
                            }
                        }
                    } else{
                        //  ** 옵션 없는 상품 로직 자리  **
                    }
                }

        }
          productRepository.saveAll(products);
          optionsRepository.saveAll(variantOptions);
    }
    @Transactional
    @Override
    public void checkProductInfo(HashMap<String, Object> reqData){

    }

    @Override
    public void upDateProductQty(HashMap<String, Object> upDate_QtyData) throws ParseException {

    }

    @Transactional
    @Override
    public void insertProductTest() {

        Unirest.config().socketTimeout(300000);
        HttpResponse<String> response =
                Unirest.post("https://openhub.godo.co.kr/godomall5/goods/Goods_Insert.php?" +
                                "partner_key=JTAxbiVCNyUxNk4lODclODYlREI=" +
                                "&key=TC1zayUwMCVGRXolOUUlNUUlQTVJJUExJUNFJUVEJUU1JTNDVWslRTUlRjclRUY4JTNDJTVDJUQ1JTEyJTVDTiVERCUwNSVGNVglM0IlRjQlMTUlMURHJUNEbSVCQg==" +
                                "&data_url=https://playlink-plus.xmd.co.kr/product/xml")
                        .body("")
                        .asString();

        System.out.println(response.getBody());

    }

    @Transactional
    @Override
    public String makeProductDataXml() {
        StringWriter sw = new StringWriter();
        String res2 = null;
        try {
            InsertProductXmlList res = new InsertProductXmlList();
            List<InsertProductXml> Productlist = new ArrayList<InsertProductXml>();

            List<InsertProductOptionXml> optionList = new ArrayList<InsertProductOptionXml>();
            for (int j = 1; j < 3; j++) {

                optionList.add(new InsertProductOptionXml(j, j, "블랙" + j, "s" + j, "", "", "", 0, "y", "0"));
            }
            for (int i = 1; i < 301; i++) {
                Productlist.add(new InsertProductXml
                        (i, "001", "001", "준희" + i, "d", "aa", "bb",
                                "cc", "y", "y", "y", "y", "1",
                                "1", "111", "dd", "2018-04-11 00:00:00", "n",
                                "y", "d", "칼라^|^사이즈", optionList));
            }
            res.setGoods_data(Productlist);
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
            xmlMapper.writeValue(sw, res);
            System.out.println(sw.toString());
            res2 = sw.toString();

        } catch (Exception e) {

        }

        return res2;
    }

}