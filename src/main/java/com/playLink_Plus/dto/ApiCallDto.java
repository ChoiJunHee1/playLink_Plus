package com.playLink_Plus.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiCallDto {

    private String Authorization = "Basic MGJsdkxYNFVPQnVsZWQ1Q255VHdmSTpGM0pOT0I3UER4Vm4wNEVzZlpZdVlE";
    private String Content_Type = "application/x-www-form-urlencoded";
    private String grant_type = "authorization_code";
    private String redirect_uri = "https://playlink-plus.xmd.co.kr/auth/IssuedToken";
    private String X_Cafe24_Api_Version = "2022-09-01";
    private String X_Cafe24_Client_Id = "0blvLX4UOBuled5CnyTwfI";
    private String Insert_Content_Type = "application/json";


}
