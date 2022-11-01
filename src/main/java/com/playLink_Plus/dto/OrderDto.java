package com.playLink_Plus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private String systemId;
    private String mallId;
    private String startDate;
    private String endDate;

}
