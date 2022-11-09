package com.playLink_Plus.dto;

import com.playLink_Plus.util.CommonUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantRegistrationDto {
    private String service;
    private String tenant;
    private String companyName;
    private String businessNumber;


    private Details details;
//    private Spl spl;
//    private Csn csn;

    /* Paan 업체 키 발급을 위한 정보 */
    @Data
    public static class Details {
        private String companyName;
        private String businessNumber;

        public boolean isRegistrable() {
            return StringUtils.isNotEmpty(this.companyName) && (StringUtils.isNotEmpty(this.businessNumber) && CommonUtils.isValidBusinessNumber(this.businessNumber));
        }
    }
}
