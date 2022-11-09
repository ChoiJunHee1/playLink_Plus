package com.playLink_Plus.service.Tenant;

import com.playLink_Plus.entity.PlTenant;
import com.playLink_Plus.repository.PlTenantRepository;

import com.playLink_Plus.service.PlTenantServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class PlTenantService implements PlTenantServiceInterface {
    private PlTenantRepository tenantRepository;

    public PlTenantService(PlTenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    public void save(PlTenant tenant) {
        tenantRepository.save(tenant);
    }

    @Override
    public Optional<PlTenant> findTenantByServiceAndTenant(String service, String tenant) {
        return tenantRepository.findPlTenantByServiceAndTenant(service, tenant);
    }


    @Override
    public Iterable<PlTenant> findPlTenantsByServiceAndKeyword(String service, String keyword) {
        return tenantRepository.findPlTenantsByServiceAndKeyword(service, keyword);
    }
}
