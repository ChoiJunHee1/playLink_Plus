package com.playLink_Plus.identifier;

import lombok.NoArgsConstructor;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductIdentifier implements Serializable {
    private String mallId;
    private String systemId;
    private String productCode;
}
