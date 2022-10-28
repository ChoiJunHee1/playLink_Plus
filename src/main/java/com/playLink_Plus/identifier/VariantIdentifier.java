package com.playLink_Plus.identifier;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariantIdentifier implements Serializable{
    private String mallId;
    private String systemId;
    private String variantCode;
    private String optionKey;

}
