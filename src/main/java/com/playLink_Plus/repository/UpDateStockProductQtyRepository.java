package com.playLink_Plus.repository;

import com.playLink_Plus.entity.product.UpDateStockQty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpDateStockProductQtyRepository extends JpaRepository<UpDateStockQty, Long> {
    UpDateStockQty findByMallKey(String mallKey);
}
