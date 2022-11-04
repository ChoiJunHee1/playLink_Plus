package com.playLink_Plus.repository;

import com.playLink_Plus.entity.ProductDetail;
import com.playLink_Plus.identifier.VariantIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionsRepository extends JpaRepository<ProductDetail, VariantIdentifier> {

    List<ProductDetail> findByVariantCode(String variantCode);
}
