package com.playLink_Plus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantRegistrationDto {
    private String service;
    private String tenant;
    private String companyName;
    private String businessNumber;
}
