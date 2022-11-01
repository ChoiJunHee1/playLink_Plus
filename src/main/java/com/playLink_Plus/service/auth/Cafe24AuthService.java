package com.playLink_Plus.service.auth;

import com.playLink_Plus.entity.AuthMaster;
import com.playLink_Plus.repository.AuthRepository;
import com.playLink_Plus.service.AuthServiceInterface;
import com.playLink_Plus.vo.ApiVo;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
public class Cafe24AuthService implements AuthServiceInterface {

    @Autowired
    final AuthRepository auth_repository;
    ApiVo apiVo = new ApiVo();

    JSONParser parser = new JSONParser();

    public Cafe24AuthService(AuthRepository auth_repository) {
        this.auth_repository = auth_repository;
    }

    @Override
    @Transactional
    public AuthMaster issuedToken(String mall_id, String code) {
    log.info("토큰발급시작");
        HttpResponse<String> response = Unirest.post("https://" + mall_id + ".cafe24api.com/api/v2/oauth/token")
                .header("Authorization", apiVo.getAuthorization())
                .header("Content-Type", apiVo.getContentType())
                .field("grant_type", apiVo.getGrantType())
                .field("code", code)
                .field("redirect_uri", apiVo.getRedirectUri())
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
                .refreshTokenExpiresAt(tokenData.get("refresh_token_expires_at").toString())
                .AuthorizationCode("Basic MGJsdkxYNFVPQnVsZWQ1Q255VHdmSTpGM0pOT0I3UER4Vm4wNEVzZlpZdVlE") // 추후 환경변수 처리해야됨
                .build();
        System.out.println(authMaster);
        auth_repository.save(authMaster);
        return authMaster;
    }

    @Override
    @Transactional
    public AuthMaster refreshTokenIssued(String mallId) {
        AuthMaster refreshToken = auth_repository.findByMallId(mallId);
        HttpResponse<String> response = Unirest.post("https://" + refreshToken.getMallId() + ".cafe24api.com/api/v2/oauth/token")
                .header("Authorization", apiVo.getAuthorization())
                .header("Content-Type", apiVo.getContentType())
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
                .refreshTokenExpiresAt(refreshTokenData.get("refresh_token_expires_at").toString())
                                .build();

        auth_repository.save(refresh_List);

        return refresh_List;
    }
}
