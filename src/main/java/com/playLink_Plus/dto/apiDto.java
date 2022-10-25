package com.playLink_Plus.dto;

import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class apiDto {

    private String Authorization = "Basic MGJsdkxYNFVPQnVsZWQ1Q255VHdmSTpGM0pOT0I3UER4Vm4wNEVzZlpZdVlE";
    private String Content_Type = "application/x-www-form-urlencoded";
    private String grant_type = "authorization_code";
    private String redirect_uri = "https://playlink-plus.xmd.co.kr/auth/IssuedToken";

}
