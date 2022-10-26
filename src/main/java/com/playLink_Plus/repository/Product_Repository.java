package com.playLink_Plus.repository;

import com.playLink_Plus.entity.ProductMaster;
import com.playLink_Plus.entity.VariantOption;
import com.playLink_Plus.identifier.Product_Identifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Product_Repository extends JpaRepository<ProductMaster, Product_Identifier> {



}
