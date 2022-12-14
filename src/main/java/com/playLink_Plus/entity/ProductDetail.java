package com.playLink_Plus.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor //기본 생성자를 생성해준다준다.
@AllArgsConstructor //전체 변수를 생성하는 생성자를 만들어
@Entity //테이블과의 매핑
@EntityListeners(AuditingEntityListener.class)
@IdClass(com.playLink_Plus.identifier.ProductDetail.class)
public class ProductDetail implements Serializable {

    @Id
    private String mallId;

    @Id
    private String productCode;

    @Id
    private String variantCode;

    private String optionName;

    private String optionValue;

    private String createdDate;

    private String updated_date;

    @Id
    private int optionNum;
//
//    private Integer optionPrice;
//
//    private String optionViewFl;
//
//    private String optionSellFl;

    @CreatedDate
    @Column(insertable = true, updatable = false)
    private LocalDateTime createdAt;

    @Id
    private String systemId;

    @LastModifiedDate
    @Column(insertable = false, updatable = true)
    private LocalDateTime updateAt;


}
