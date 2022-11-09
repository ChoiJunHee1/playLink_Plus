package com.playLink_Plus.xmlType;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "stockOptionData")
public class StockOptionData {

    @JacksonXmlProperty(isAttribute = true)
    private Integer idx;

    private String sno;

    private Integer stockCnt;

}
