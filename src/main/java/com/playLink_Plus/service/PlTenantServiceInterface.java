package com.playLink_Plus.service;

import com.playLink_Plus.entity.PlTenant;


import java.util.Optional;

public interface PlTenantServiceInterface {
    void save(PlTenant tenant);
    void delete(Long id);
    Optional<PlTenant> findTenantByServiceAndTenant(String service, String tenant);
    Iterable<PlTenant> findPlTenantsByServiceAndKeyword(String service, String keyword);
    Iterable<PlTenant> findAll();
    Optional<PlTenant> findTenantById(Long id);
}
