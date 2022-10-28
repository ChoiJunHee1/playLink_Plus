package com.playLink_Plus.entity.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import java.io.Serializable;

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

    private String OrderDate; //주문 일시

    private int status; // 주문 상태 값 0 : 신규 주문    1 : 기존 주문

    private String orderName; // 주문자 이름  * 수취인 이름이랑 다를 수 있슴

    private String ordererCellphone; // 주문자 핸드폰 번호

}
