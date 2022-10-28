package com.playLink_Plus.service;

import com.playLink_Plus.entity.AuthMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public interface AuthServiceInterface {

    @Autowired
    AuthMaster issuedToken(String mall_id, String code); // 토큰 발급

    @Autowired
    AuthMaster refreshTokenIssued(String refreshToken);
}
