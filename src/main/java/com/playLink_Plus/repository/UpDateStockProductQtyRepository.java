package com.playLink_Plus.repository;

import com.playLink_Plus.entity.product.UpDateStockQty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UpDateStockProductQtyRepository extends JpaRepository<UpDateStockQty, Long> {
    UpDateStockQty findBySystemIdAndMallId(String systemId,String mallId);

    @Transactional
    void deleteBySystemIdAndMallId(String systemId, String mallId);
}
