package com.playLink_Plus.controller;

import com.playLink_Plus.entity.AuthMaster;
import com.playLink_Plus.repository.Auth_Repository;
import com.playLink_Plus.repository.Options_Repository;
import com.playLink_Plus.repository.Product_Repository;
import com.playLink_Plus.service.Auth_Service_interface;
import com.playLink_Plus.service.Product_Service_Interface;
import com.playLink_Plus.service.auth.Cafe24_Auth_Service;
import com.playLink_Plus.service.product.Cafe24_Product_Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    AuthMaster authMaster;
    final Auth_Repository auth_repository;
    final Product_Repository product_repository;
    final Options_Repository options_repository;
    final Cafe24_Auth_Service cafe24_auth_service;
    private String mallid;
     Auth_Service_interface auth_service = null;

     Product_Service_Interface product_service = null ;

    @GetMapping("/authCode")
    public RedirectView issued_Auth_Code (@RequestParam("mall_id") String mall_id) {
        //나중에 환경 변수로 뺴야됨
       String path = "https://"+mall_id+".cafe24.com/api/v2/oauth/authorize?response_type=code&client_id=0blvLX4UOBuled5CnyTwfI&" +
                "state=app_install&redirect_uri=https://playlink-plus.xmd.co.kr/auth/IssuedToken" +
                "&scope=mall.read_Category,mall.write_Category,mall.read_Product,mall.write_Product,mall.read_Collection,mall.write_Collection,mall.read_Supply," +
                "mall.write_Supply,mall.read_Order,mall.write_Order,mall.read_Customer,mall.write_Customer,mall.read_Promotion,mall.write_Promotion";
        log.info(path);
        RedirectView redirectView = new RedirectView();
        mallid = mall_id;
        redirectView.setUrl(path);

    return  redirectView;
    }

    @RequestMapping("/IssuedToken")
    public String issued_access_token(@RequestParam("code") String code ){


        auth_service = new Cafe24_Auth_Service(auth_repository);

        product_service = new Cafe24_Product_Service(cafe24_auth_service, options_repository, product_repository);
        try {
            authMaster = auth_service.issued_Token(mallid, code);
        }catch (Exception e){
            log.error("인증 토큰 발급 진행중 오류가 발생 하였습니다."+e.getMessage());
        }

        return "onlineMallGuide";
    }

//    @RequestMapping("/test")
//    public String testController(@RequestParam("mall_id") String mallId) {
//
//            //추후 고도몰 service 들어올 자리
//        auth_service = new Cafe24_Auth_Service(auth_repository);
//        Object Options_Repository = null;
//        product_service = new Cafe24_Product_Service(cafe24_auth_service, options_repository, product_repository);
//            try {
//                authMaster  = auth_service.refreshTokenIssued(mallId);
//                product_service.issued_Product_Item(mallId,authMaster);
//            } catch (Exception e) {
//                log.error(e.getMessage());
//            }
//
//        return "onlineMallGuide";
//    }

}
