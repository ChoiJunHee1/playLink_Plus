package com.playLink_Plus.repository;

import com.playLink_Plus.entity.VariantOption;
import com.playLink_Plus.identifier.VariantIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionsRepository extends JpaRepository<VariantOption, VariantIdentifier> {

    List<VariantOption> findByVariantCode(String variantCode);
}
