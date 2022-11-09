package com.playLink_Plus.repository;

import com.playLink_Plus.entity.PlTenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface PlTenantRepository extends JpaRepository<PlTenant, Long> {
    Optional<PlTenant> findPlTenantByServiceAndTenant(String service, String tenant);



    @Query(
            "SELECT pt FROM PlTenant pt "
                    + "WHERE (:service = '' OR :service IS NULL OR pt.service = :service) "
                    + "AND (pt.tenant LIKE CONCAT('%', :keyword, '%') OR pt.businessNumber LIKE CONCAT('%', :keyword, '%') OR pt.companyName LIKE CONCAT('%', :keyword, '%')) "
                    + "ORDER BY pt.tenantIdx DESC"
    )
    Iterable<PlTenant> findPlTenantsByServiceAndKeyword(@Param("service") String service,
                                                        @Param("keyword") String keyword);


}
