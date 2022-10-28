package com.playLink_Plus.identifier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderIdentifier {
    private String orderId; // 주문 아이디
    private String systemId;
}
