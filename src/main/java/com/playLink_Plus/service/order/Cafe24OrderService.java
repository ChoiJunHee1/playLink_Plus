package com.playLink_Plus.service.order;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.playLink_Plus.dto.OrderDto;
import com.playLink_Plus.entity.AuthMaster;
import com.playLink_Plus.entity.order.OrderItem;
import com.playLink_Plus.entity.order.OrderMaster;
import com.playLink_Plus.entity.order.OrderReceivers;
import com.playLink_Plus.repository.OrderItemRepository;
import com.playLink_Plus.repository.OrderReceiverRepository;
import com.playLink_Plus.repository.OrderRepository;
import com.playLink_Plus.service.OrderServiceInterface;
import com.playLink_Plus.service.auth.Cafe24AuthService;
import com.playLink_Plus.vo.ApiVo;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class Cafe24OrderService implements OrderServiceInterface {

    final Cafe24AuthService cafe24_auth_service;
    final OrderItemRepository orderItemRepository;
    final OrderRepository orderRepository;

    final OrderReceiverRepository orderReceiverRepository;

    AuthMaster authMaster;

    ApiVo apiVo = new ApiVo();

    @Override
    @Transactional
    public void issuedOrder(OrderDto orderDto) {


        authMaster = cafe24_auth_service.refreshTokenIssued(orderDto.getMallId());

        List<String> order_item_code = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();

        HttpResponse<String> response = Unirest.get("https://" + orderDto.getMallId() + ".cafe24api.com/api/v2/admin/orders/count?start_date=" + orderDto.getStartDate() + "&end_date=" + orderDto.getEndDate() + "&order_status=N10&date_type=pay_date")
                .header("Content-Type", apiVo.getInsertContentType())
                .header("Authorization", "Bearer " + authMaster.getAccessToken())
                .header("X-Cafe24-Api-Version", apiVo.getCafe24ApiVersion())
                .asString();

        JSONParser parser = new JSONParser();
        JSONObject orderData = new JSONObject();
        try {
            orderData = (JSONObject) parser.parse(response.getBody());

        } catch (Exception e) {
            log.error("전체 주문 갯수 요청 중 오류");
        }
        int offsetInt = 0;
        System.out.println(orderData.get("count"));
        String num = orderData.get("count").toString();
        double orderQty = Double.parseDouble(num);

        if (orderQty < 1000) {
            offsetInt = 0;
        } else {
            orderQty = Math.ceil(orderQty / 1000);
            offsetInt = (int) orderQty;
        }

        List<OrderMaster> orderMasters = new ArrayList<>();
        List<OrderItem> orderItems = new ArrayList<>();
        List<OrderReceivers> orderReceivers = new ArrayList<>();

        for (int i = 0; i <= offsetInt; i++) {
            HttpResponse<String> responseOrderList = Unirest.get("https://" + orderDto.getMallId() + ".cafe24api.com/api/v2/admin/orders?start_date=" + orderDto.getStartDate() + "&end_date=" + orderDto.getEndDate() + "&offset=" + i + "000&limit=1000&order_status=N10")
                    .header("Content-Type", apiVo.getInsertContentType())
                    .header("Authorization", "Bearer " + authMaster.getAccessToken())
                    .header("X-Cafe24-Api-Version", apiVo.getCafe24ApiVersion())
                    .asString();

            Gson gson = new Gson();
            Map<String, Object> productData = gson.fromJson(responseOrderList.getBody(), new TypeToken<Map<String, Object>>() {
            }.getType());

            List<Map<String, Object>> OrderList = (List) productData.get("orders");

            System.out.println(OrderList.size());
            for (int j = 0; j < OrderList.size(); j++) {

                HttpResponse<String> ResponsebuyerInfo = Unirest.get("https://" + orderDto.getMallId() + ".cafe24api.com/api/v2/admin/orders/" + OrderList.get(j).get("order_id") + "/buyer")
                        .header("Content-Type", apiVo.getInsertContentType())
                        .header("Authorization", "Bearer " + authMaster.getAccessToken())
                        .header("X-Cafe24-Api-Version", apiVo.getCafe24ApiVersion())
                        .asString();

                Map<String, Object> buyerInfoMap = gson.fromJson(ResponsebuyerInfo.getBody(), new TypeToken<Map<String, Object>>() {
                }.getType());
                Map<String, Object> buyerInfo = (Map) buyerInfoMap.get("buyer");
                System.out.println(buyerInfo.get("name"));
                System.out.println(buyerInfo.get("cellphone"));
                System.out.println("=====================================");
                OrderMaster orderMaster = OrderMaster.builder()
                        .orderId((String) OrderList.get(j).get("order_id"))
                        .systemId("cafe24")
                        .mallId(orderDto.getMallId())
                        .orderDate((String) OrderList.get(i).get("order_date"))
                        .buyerId((String) OrderList.get(i).get("member_id"))
                        .billingName((String) OrderList.get(i).get("billing_name"))
                        .buyerName((String) buyerInfo.get("name"))
                        .buyerCellphone((String) buyerInfo.get("cellphone"))
                        .build();
                orderMasters.add(orderMaster);

            }


            for (int t = 0; t < OrderList.size(); t++) {

                HttpResponse<String> responseOrderItems = Unirest.get("https://" + orderDto.getMallId() + ".cafe24api.com/api/v2/admin/orders/" + OrderList.get(t).get("order_id") + "/items")
                        .header("Content-Type", apiVo.getInsertContentType())
                        .header("Authorization", "Bearer " + authMaster.getAccessToken())
                        .header("X-Cafe24-Api-Version", apiVo.getCafe24ApiVersion())
                        .asString();

                Map<String, Object> orderItemMap = gson.fromJson(responseOrderItems.getBody(), new TypeToken<Map<String, Object>>() {
                }.getType());

                List<Map<String, Object>> OrderItemList = (List) orderItemMap.get("items");

                JSONObject jsonObject = new JSONObject();

                JSONArray jsonArray = new JSONArray();
                JSONObject data = new JSONObject();

                jsonArray.add(data);
                JSONArray array = new JSONArray();
                data.put("order_item_code", array);
                for (int y = 0; y < OrderItemList.size(); y++) {

                    String price_String = OrderItemList.get(y).get("product_price").toString();
                    double price_Doeble = Double.parseDouble(price_String);
                    int price_int = (int) price_Doeble;

                    String quantity_String = OrderItemList.get(y).get("quantity").toString();
                    double quantity_Doeble = Double.parseDouble(quantity_String);
                    int quantity_int = (int) quantity_Doeble;

                    int totalPrice = price_int * quantity_int;

                    OrderItem orderItem = OrderItem.builder()
                            .orderId((String) OrderList.get(t).get("order_id"))
                            .orderItemCode((String) OrderItemList.get(y).get("order_item_code"))
                            .productName((String) OrderItemList.get(y).get("product_name"))
                            .variantCode((String) OrderItemList.get(y).get("variant_code"))
                            .optionValue((String) OrderItemList.get(y).get("option_value"))
                            .productPrice(price_int)
                            .quantity(quantity_int)
                            .totalPrice(totalPrice)
                            .status(0)
                            .statusCode("N20")
                            .build();
                    orderItems.add(orderItem);
                    array.add(OrderItemList.get(y).get("order_item_code"));


                }
                data.put("process_status", "prepare");
                jsonObject.put("requests", data);
                jsonObject.put("shop_no", 1);
                System.out.println(jsonObject);
                HttpResponse<String> statusMultipleUpdate = Unirest.put("https://" + orderDto.getMallId() + ".cafe24api.com/api/v2/admin/orders/" + OrderList.get(t).get("order_id"))
                        .header("Content-Type", apiVo.getInsertContentType())
                        .header("Authorization", "Bearer " + authMaster.getAccessToken())
                        .header("X-Cafe24-Api-Version", apiVo.getCafe24ApiVersion())
                        .body(jsonObject)
                        .asString();
                System.out.println(statusMultipleUpdate.getBody());


            }


            for (int t = 0; t < OrderList.size(); t++) {
                HttpResponse<String> responseReceivers = Unirest.get("https://" + orderDto.getMallId() + ".cafe24api.com/api/v2/admin/orders/" + OrderList.get(t).get("order_id") + "/receivers")
                        .header("Content-Type", apiVo.getInsertContentType())
                        .header("Authorization", "Bearer " + authMaster.getAccessToken())
                        .header("X-Cafe24-Api-Version", apiVo.getCafe24ApiVersion())
                        .asString();

                Map<String, Object> receiversMap = gson.fromJson(responseReceivers.getBody(), new TypeToken<Map<String, Object>>() {
                }.getType());

                List<Map<String, Object>> OrderReceiver = (List) receiversMap.get("receivers");
                System.out.println(OrderReceiver.get(0).get("address_full"));

                OrderReceivers orderReceiver = OrderReceivers.builder()
                        .orderId(String.valueOf(OrderList.get(t).get("order_id")))
                        .receiversName((String) OrderReceiver.get(0).get("name"))
                        .zipCode((String) OrderReceiver.get(0).get("zipcode"))
                        .addressFull((String) OrderReceiver.get(0).get("address_full"))
                        .receiversCellphone((String) OrderReceiver.get(0).get("cellphone"))
                        .build();
                orderReceivers.add(orderReceiver);

            }
        }

        orderRepository.saveAll(orderMasters);
        orderItemRepository.saveAll(orderItems);
        orderReceiverRepository.saveAll(orderReceivers);

    }


}
