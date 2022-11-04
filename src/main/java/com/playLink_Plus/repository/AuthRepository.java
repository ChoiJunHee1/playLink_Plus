package com.playLink_Plus.repository;

import com.playLink_Plus.entity.AuthMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<AuthMaster, String> {
    AuthMaster findByMallId(String mallId);

//    Optional<AuthMaster> findByMallId(String mallid); //카멜문법 아주 중요함!!

}
