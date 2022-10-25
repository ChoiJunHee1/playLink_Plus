package com.playLink_Plus.service.auth;

import com.playLink_Plus.dto.apiDto;
import com.playLink_Plus.entity.AuthMaster;
import com.playLink_Plus.service.Auth_Service_interface;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
public class Cafe24_Auth_Service implements Auth_Service_interface {

    apiDto api = new apiDto();

    JSONParser parser = new JSONParser();
    @Override
    @Transactional
    public AuthMaster issued_Token(String mall_id, String code) {
    log.info("토큰발급시작");
        HttpResponse<String> response = Unirest.post("https://" + mall_id + ".cafe24api.com/api/v2/oauth/token")
                .header("Authorization", api.getAuthorization())
                .header("Content-Type", api.getContent_Type())
                .field("grant_type", api.getGrant_type())
                .field("code", code)
                .field("redirect_uri", api.getRedirect_uri())
                .asString();
    log.info(response.getBody());
        JSONParser parser = new JSONParser();
        JSONObject tokenData = new JSONObject();
        try {
            tokenData = (JSONObject) parser.parse(response.getBody());
        } catch (Exception e) {
            log.error("토큰 데이터 jsonparse중 오류 발생");
        }
        AuthMaster authMaster = AuthMaster.builder()
                .systemId("cafe24")
                .mallId(tokenData.get("mall_id").toString())
                .accessToken(tokenData.get("access_token").toString())
                .refreshToken(tokenData.get("refresh_token").toString())
                .refreshTokenexpiresat(tokenData.get("refresh_token_expires_at").toString())
                .AuthorizationCode("Basic MGJsdkxYNFVPQnVsZWQ1Q255VHdmSTpGM0pOT0I3UER4Vm4wNEVzZlpZdVlE") // 추후 환경변수 처리해야됨
                .build();

        return authMaster ;
    }

    @Override
    @Transactional
    public AuthMaster refreshTokenIssued(AuthMaster refreshToken) {
        log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println("@@@@@@@@@@@@@@@@@@");
        HttpResponse<String> response = Unirest.post("https://" + refreshToken.getMallId() + ".cafe24api.com/api/v2/oauth/token")
                .header("Authorization", api.getAuthorization())
                .header("Content-Type", api.getContent_Type())
                .field("grant_type", "refresh_token")
                .field("refresh_token", refreshToken.getRefreshToken())
                .asString();

        JSONParser parser = new JSONParser();
        JSONObject refreshTokenData = new JSONObject();;
        try {

            refreshTokenData = (JSONObject) parser.parse(response.getBody());
        } catch (Exception e) {
            log.error("refresh token 발급중 오류 발생");
        }

        AuthMaster refresh_List = AuthMaster.builder()
                .mallId(refreshTokenData.get("mall_id").toString())
                .accessToken(refreshTokenData.get("access_token").toString())
                .refreshToken(refreshTokenData.get("refresh_token").toString())
                .refreshTokenexpiresat(refreshTokenData.get("refresh_token_expires_at").toString())
                                .build();

        return refresh_List;
    }
}
