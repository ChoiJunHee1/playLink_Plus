package com.playLink_Plus.controller;
import com.playLink_Plus.dto.TenantRegistrationDto;
import com.playLink_Plus.entity.PlTenant;
import com.playLink_Plus.service.PlTenantServiceInterface;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PlayLinkController {
    final PlTenantServiceInterface plTenantService;

    @GetMapping(value = "/tenant/list")
    public Iterable<PlTenant> listTenant(@RequestParam(name = "svc") String service,
                                         @RequestParam(name = "keyword") String keyword) {

        return plTenantService.findPlTenantsByServiceAndKeyword(service, StringUtils.replace(keyword, "_", "\\_"));
    }

    @PostMapping(value = "/tenant/registration", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public String registerTenant(@RequestBody TenantRegistrationDto tenantRegistrationDto) throws Exception {

        String service = tenantRegistrationDto.getService();
        String tenant = tenantRegistrationDto.getTenant();
        Optional<PlTenant> result =  plTenantService.findTenantByServiceAndTenant(service, tenant);

        if(result.isPresent()){
            log.info("========================>>>>>존재하는 회원사입니다. >>>"+result.get().getTenant());
        }
        else{
            UUID uuid =UUID.randomUUID();
            PlTenant plTenant = PlTenant.builder()
                    .service(tenantRegistrationDto.getService())
                    .tenant(tenantRegistrationDto.getTenant())
                    .companyName(tenantRegistrationDto.getCompanyName())
                    .businessNumber(tenantRegistrationDto.getBusinessNumber())
                    .playLinkCompanyKey(uuid.toString())
                    .build();

            plTenantService.save(plTenant);
        }

//        log.info(tenantRegistrationDto.getService());
//
//
//        log.info(tenantRegistrationDto.toString());
        return tenantRegistrationDto.toString();
    }
}
