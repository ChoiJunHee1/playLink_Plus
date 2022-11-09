package com.playLink_Plus.xmlType;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.playLink_Plus.dto.InsertProductOptionXml;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "goods_data")
public class StockGoodsData {

    @JacksonXmlProperty(isAttribute = true)
    private Integer idx;

    @JacksonXmlProperty
    private Integer goodsNo;

    @JacksonXmlProperty
    private String optionFl;

    @JacksonXmlProperty(localName = "stockOptionData")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<StockOptionData> stockOptionData;


}
