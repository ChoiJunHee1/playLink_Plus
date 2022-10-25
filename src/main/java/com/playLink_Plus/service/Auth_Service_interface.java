package com.playLink_Plus.service;

import com.playLink_Plus.entity.AuthMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public interface Auth_Service_interface {

    @Autowired
    AuthMaster issued_Token(String mall_id, String code); // 토큰 발급

    @Autowired
    AuthMaster refreshTokenIssued(AuthMaster refreshToken);
}
