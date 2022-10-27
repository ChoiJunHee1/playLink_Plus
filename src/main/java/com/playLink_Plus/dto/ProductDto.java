package com.playLink_Plus.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private String mall_id;
    private int product_no;
    private int shop_no;
    private String variant_code;
    private String use_inventory;
    private int quantity;
    private String safety_inventory;

}
