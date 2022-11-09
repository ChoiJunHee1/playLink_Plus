package com.playLink_Plus.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "pl_tenants")
public class PlTenant {

    @Id
    @GeneratedValue
    @Column
    private Long tenantIdx;
    @Column
    private String service;
    @Column
    private String tenant;
    @Column
    private String companyName;
    @Column
    private String businessNumber;
    @Column
    private String playLinkCompanyKey;
    @Column
    @Builder.Default
    private Instant playLinkRegTime = null;
    @Column
    @Builder.Default
    private Instant playLinkModTime = null;

    @Column
    @Builder.Default
    private Instant regTime = Instant.now();
    @Column
    @Builder.Default
    private Instant modTime = Instant.now();


}
