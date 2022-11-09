package com.playLink_Plus.repository;

import com.playLink_Plus.identifier.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductDetailRepository extends JpaRepository<com.playLink_Plus.entity.ProductDetail, ProductDetail> {

    @Transactional
    void deleteByProductCode(String goodsNoInt);
}
