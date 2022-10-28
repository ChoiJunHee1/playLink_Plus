package com.playLink_Plus.service.order;

import com.playLink_Plus.dto.OrderDto;
import com.playLink_Plus.service.OrderServiceInterface;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class Cafe24OrderService implements OrderServiceInterface {
    @Override
    @Transactional
    public void issuedOrder(OrderDto orderDto){
        System.out.println(orderDto.getStartDate());
        System.out.println(orderDto.getStartDate());
        System.out.println(orderDto.getStartDate());
        HttpResponse<String> response = Unirest.get("https://"+orderDto.getMallId()+".cafe24api.com/api/v2/admin/orders?start_date="+orderDto.getStartDate()+"&end_date="+orderDto.getEndDate())
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer vkoCge6eCkrJqAoX3LYuyJ")
                .header("X-Cafe24-Api-Version", "2022-06-01")
                .header("Cookie", "ECSESSID=f462bbbbee1ccc07ba6741ff12de8493; atl_epcheck=1; atl_option=0%2C0%2CD; basketcount_1=0; s3_connection_bucket=pg255b83940685007")
                .asString();

        System.out.println(response.getBody());

    }


}
