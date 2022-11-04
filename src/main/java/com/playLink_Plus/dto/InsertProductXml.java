package com.playLink_Plus.dto;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "goods_data")
public class InsertProductXml {


    @JacksonXmlProperty(isAttribute = true)
    private Integer id;

    @JacksonXmlProperty
    private String cateCd;

    @JacksonXmlCData
    private String allCateCd;

    @JacksonXmlProperty
    private String goodsNm;

    @JacksonXmlProperty
    private String goodsNmFl;

    @JacksonXmlCData
    private String goodsNmMain;

    @JacksonXmlCData
    private String goodsNmList;

    @JacksonXmlCData
    private String goodsNmDetail;

    @JacksonXmlProperty
    private String goodsDisplayFl;

    @JacksonXmlProperty
    private String goodsDisplayMobileFl;

    @JacksonXmlProperty
    private String goodsSellFl;

    @JacksonXmlProperty
    private String goodsSellMobileFl;

    @JacksonXmlProperty
    private String scmNo;

    @JacksonXmlCData
    private String deliverySno;

    @JacksonXmlCData
    private String goodsCd;

    @JacksonXmlCData
    private String goodsSearchWord;

    @JacksonXmlCData
    private String goodsOpenDt;

    @JacksonXmlProperty
    private String goodsState;

    @JacksonXmlProperty
    private String optionFl;

    @JacksonXmlProperty
    private String optionDisplayFl;

    @JacksonXmlCData
    private String optionName;

    @JacksonXmlProperty(localName = "optionData")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<InsertProductOptionXml> optionData;


}
