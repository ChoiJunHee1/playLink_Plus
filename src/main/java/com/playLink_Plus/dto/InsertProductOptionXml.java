package com.playLink_Plus.dto;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "optionData")
public class InsertProductOptionXml {

    @JacksonXmlProperty(isAttribute = true)
    private Integer idx;

    @JacksonXmlProperty
    private Integer optionNo;

    @JacksonXmlCData
    private String optionValue1;

    @JacksonXmlCData
    private String optionValue2;

    @JacksonXmlCData
    private String optionValue3;

    @JacksonXmlCData
    private String optionValue4;

    @JacksonXmlCData
    private String optionValue5;

    @JacksonXmlCData
    private Integer optionPrice;

    @JacksonXmlProperty
    private String optionViewFl;

    @JacksonXmlProperty
    private String stockCnt;


}
