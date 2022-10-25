package com.playLink_Plus.repository;

import com.playLink_Plus.entity.ProductMaster;
import com.playLink_Plus.identifier.Product_Identifier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Product_Repository extends JpaRepository<ProductMaster, Product_Identifier> {

}
