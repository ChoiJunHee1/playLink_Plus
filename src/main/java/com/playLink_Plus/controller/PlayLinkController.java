package com.playLink_Plus.controller;
<<<<<<< HEAD

=======
import com.playLink_Plus.common.Consts;
import com.playLink_Plus.common.ResponseDto;
>>>>>>> release
import com.playLink_Plus.dto.TenantRegistrationDto;
import com.playLink_Plus.entity.PlTenant;
import com.playLink_Plus.service.PlTenantServiceInterface;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @GetMapping(value = "/tenant/list/all")
    public Iterable<PlTenant> listTenant() {
        return plTenantService.findAll();
    }


    @PostMapping(value = "/tenant/delete")
    public void deleteTenant(@RequestParam(name = "id") Long id) {
        Optional<PlTenant> result= plTenantService.findTenantById(id);

        if(result.isPresent()) {
            plTenantService.delete(result.get().getTenantIdx());
        }
        else {
            throw new RuntimeException("삭제할 회원사 id가 없습니다.");
        }
    }


    @PostMapping(value = "/tenant/registration", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseDto registerTenant(@RequestBody TenantRegistrationDto tenantRegistrationDto) throws Exception {

        String service = tenantRegistrationDto.getService();
        String tenant = tenantRegistrationDto.getTenant();
        Optional<PlTenant> result = plTenantService.findTenantByServiceAndTenant(service, tenant);

<<<<<<< HEAD
        if (result.isPresent()) {
            log.info("========================>>>>>존재하는 회원사입니다. >>>" + result.get().getTenant());
        } else {
            UUID uuid = UUID.randomUUID();
=======
        if(result.isPresent()){
            log.info("========================>>>>>존재하는 회원사입니다.>>>"+result.get().getTenant());
        }
        else{
            UUID uuid =UUID.randomUUID();
>>>>>>> release
            PlTenant plTenant = PlTenant.builder()
                    .service(tenantRegistrationDto.getService())
                    .tenant(tenantRegistrationDto.getTenant())
                    .companyName(tenantRegistrationDto.getCompanyName())
                    .businessNumber(tenantRegistrationDto.getBusinessNumber())
                    .playLinkCompanyKey(uuid.toString())
                    .build();

            plTenant.setModTime(Instant.now());
            plTenantService.save(plTenant);

            result = Optional.of(plTenant);
        }

        ResponseDto.Time.TimeBuilder timeBuilder = ResponseDto.Time.builder().start(Instant.now());
        ResponseDto.ResponseDtoBuilder responseDtoBuilder = ResponseDto.builder().in(tenantRegistrationDto);

        String plGroupId = Consts.Xmd.RequestGroupPrefix.REGISTER_TENANT + LocalDateTime.now().format(DateTimeFormatter.ofPattern(Consts.PLAYLINK_GROUP_FORMAT));
        responseDtoBuilder.plGroupId(plGroupId);
        timeBuilder.end(Instant.now());

        responseDtoBuilder
                .out(result.get())
                .time(timeBuilder.build());
        return responseDtoBuilder.build();
    }
}
