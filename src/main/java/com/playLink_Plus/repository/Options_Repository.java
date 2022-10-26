package com.playLink_Plus.repository;

import com.playLink_Plus.entity.VariantOption;
import com.playLink_Plus.identifier.Variant_Identifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Options_Repository extends JpaRepository<VariantOption, Variant_Identifier> {

    List<VariantOption> findByVariantCode(String variantCode);
}
