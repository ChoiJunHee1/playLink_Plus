package com.playLink_Plus.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JacksonXmlRootElement(localName = "data")
public class InsertProductXmlList {


    @JacksonXmlProperty(localName = "goods_data")
    @JacksonXmlElementWrapper(useWrapping = false)
    List<InsertProductXml> goods_data = new ArrayList<>();
}
