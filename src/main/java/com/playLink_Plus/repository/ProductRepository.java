package com.playLink_Plus.repository;

import com.playLink_Plus.entity.ProductMaster;
import com.playLink_Plus.identifier.ProductIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductMaster, ProductIdentifier> {



}
