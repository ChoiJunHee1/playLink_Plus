package com.playLink_Plus.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiCallDto {

    private String authorization = "Basic MGJsdkxYNFVPQnVsZWQ1Q255VHdmSTpGM0pOT0I3UER4Vm4wNEVzZlpZdVlE";
    private String ContentType = "application/x-www-form-urlencoded";
    private String grantType = "authorization_code";
    private String redirectUri = "https://playlink-plus.xmd.co.kr/auth/issuedToken";
    private String cafe24ApiVersion = "2022-09-01";
    private String cafe24ClientId = "0blvLX4UOBuled5CnyTwfI";
    private String insertContentType = "application/json";

}
