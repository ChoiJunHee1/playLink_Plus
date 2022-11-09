package com.playLink_Plus.xmlType;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JacksonXmlRootElement(localName = "data")
public class StockGoodsDataList {
    @JacksonXmlProperty(localName = "goods_data")
    @JacksonXmlElementWrapper(useWrapping = false)
    List<StockGoodsData> goods_data = new ArrayList<>();
}
