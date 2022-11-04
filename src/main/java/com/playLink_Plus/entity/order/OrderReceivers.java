package com.playLink_Plus.entity.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor //기본 생성자를 생성해준다준다.
@AllArgsConstructor //전체 변수를 생성하는 생성자를 만들어
@Entity //테이블과의 매핑
@EntityListeners(AuditingEntityListener.class)
public class OrderReceivers {

    @Id
    private String orderId;

    private String receiversName;

    private String receiversCellphone;

    private String zipCode;

    private String addressFull;


    @CreatedDate
    @Column(insertable = true, updatable = false)
    private LocalDateTime createdAt;
}
