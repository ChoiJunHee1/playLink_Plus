package com.playLink_Plus.repository;

import com.playLink_Plus.entity.VariantOption;
import com.playLink_Plus.identifier.Variant_Identifier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Options_Repository extends JpaRepository<VariantOption, Variant_Identifier> {
}
