package com.playLink_Plus.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private String mallId;
    private int productNo;
    private int shopMo;
    private String variantCode;
    private String useInventory;
    private int quantity;
    private String safetyInventory;


}
