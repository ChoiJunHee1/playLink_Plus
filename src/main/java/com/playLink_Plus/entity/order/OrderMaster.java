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
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor //기본 생성자를 생성해준다준다.
@AllArgsConstructor //전체 변수를 생성하는 생성자를 만들어
@Entity //테이블과의 매핑
@EntityListeners(AuditingEntityListener.class)
public class OrderMaster implements Serializable {

    @Id
    private String orderId; // 주문 아이디

    private String systemId; // 쇼핑몰 종류

    private String mallId; // 쇼핑몰 아이디

    private String orderDate; //주문 일시

    private String buyerId; // 주문자 이름  * 수취인 이름이랑 다를 수 있슴

    private String buyerCellphone; // 주문자 핸드폰 번호

    private String billingName; //결제자 이름

    private String buyerName;

    @CreatedDate
    @Column(insertable = true, updatable = false)
    private LocalDateTime createdAt;

}
