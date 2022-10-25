package com.playLink_Plus.service.product;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.playlinkplus.api.service.Product_Service_Interface;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class Product_Service implements Product_Service_Interface {

    @Transactional
    @Override
    public String issued_Product_Item(String mall_id)  {

        HttpResponse<String> response = Unirest.get("https://"+mall_id+".cafe24api.com/api/v2/products/count")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("X-Cafe24-Api-Version", "2022-09-01")
                .header("'Authorization", "Bearer TJtkw3NKIlmkSCfuRBl0OD")
                .header("X-Cafe24-Client-Id", "0blvLX4UOBuled5CnyTwfI")
                .header("Cookie", "ECSESSID=3d5873d0332a32cc66b61aaa5b898567")
                .asString();

        JSONParser parser = new JSONParser();
        JSONObject tokenData = new JSONObject();
        try {

        tokenData = (JSONObject) parser.parse(response.getBody());
        }catch (Exception e){
            log.error("전체 상품 갯수 요청 중 오류");
        }

        int offsetInt = 0;
        String num = tokenData.get("count").toString();

        double offsetDouble = Double.parseDouble(num);

        if (offsetDouble < 100){
            offsetInt = 0;
        }else {
            offsetDouble =  Math.ceil(offsetDouble/10);
            offsetInt = (int) offsetDouble;
        }

        HttpResponse<String> response2 = Unirest.get("https://"+mall_id+".cafe24api.com/api/v2/products?offset=000&limit=100")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("X-Cafe24-Api-Version", "2022-09-01")
                .header("'Authorization", "Bearer 6f9g3VHZZa4XDeu6ILvDHO")
                .header("X-Cafe24-Client-Id", "0blvLX4UOBuled5CnyTwfI")
                .asString();

        try {

            Gson gson = new Gson();
            Map<String, Object> productData = gson.fromJson(response2.getBody(), new TypeToken<Map<String, Object>>(){}.getType());
            List<Map<String, Object>> productList = (List) productData.get("products");

//            System.out.println(productList);
            for(int i = 54; i< productList.size(); i++) {
                String product_name = (String) productList.get(i).get("product_name");
                String product_code = (String) productList.get(i).get("product_code");

                double product_No = (double) productList.get(i).get("product_no");
                int product_No_int = (int) product_No;
//                System.out.println("product_name : "+product_name+" "+ "product_code : "+product_code+"  product_No : "+product_No_int);

                HttpResponse<String> response3 = Unirest.get("https://"+mall_id+".cafe24api.com/api/v2/products/"+product_No_int+"/variants")
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer ecIteuXKKkBDq17eSRRMII")
                        .header("X-Cafe24-Api-Version", "2022-06-01")
                        .header("X-Cafe24-Client-Id", "0blvLX4UOBuled5CnyTwfI")
                        .header("Cookie", "ECSESSID=5b6a7833cba8818ac4ff3ace9c31381e")
                        .asString();

                Map<String, Object> variantData = gson.fromJson(response3.getBody(), new TypeToken<Map<String, Object>>(){}.getType());
                List<Map<String, Object>> variantList = (List) variantData.get("variants");
                for(int j = 0; j < variantList.size(); j++) {
//                    System.out.println(variantList.get(j));
//                    System.out.println(variantList.get(j).get("variant_code"));

                    List<Map<String, Object>> variantOptionsList = (List) variantList.get(j).get("options");
                        System.out.println("=========================================");
                        System.out.println(product_name);
                        System.out.println(product_code);
                        System.out.println(product_No_int);
                        System.out.println(variantList.get(j).get("variant_code"));
                        System.out.print(variantOptionsList);
                        System.out.println("");
                        System.out.println("=========================================");
                        System.out.println("");
                }
            }

        }catch (Exception e){
            log.error("에러에러에러");
            log.error(e.getMessage());
        }
        return null;
    }
}
