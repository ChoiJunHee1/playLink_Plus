package com.playLink_Plus.identifier;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Variant_Identifier implements Serializable{
    private String mallId;
    private String variantCode;
    private String optionName;
    private String optionValue;
}
