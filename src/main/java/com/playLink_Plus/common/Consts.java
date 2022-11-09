package com.playLink_Plus.common;

public class Consts {
    public static final String DEFAULT_HOST = "xmd.co.kr";

    public static final String HEADER_X_REQUESTED_WITH = "X-Requested-With";
    public static final String VALUE_X_REQUESTED_WITH = "AJAX";

    public static final String YES = "Y";
    public static final String NO = "N";

    public static final String URL_PREFIX_API = "/apis";
    public static final String URL_PREFIX_PAGE = "/pages";

    public static final String URL_PREFIX_PLAYLINK = URL_PREFIX_API + "/pl";
    public static final String URL_PREFIX_SERVICE = URL_PREFIX_API + "/svc";
    public static final String URL_PREFIX_GOODSFLOW = URL_PREFIX_API + "/gf";

    public static final String PLAYLINK_GROUP_FORMAT = "yyyyMMddHHmmssSSS";

    public static final String[] INNER_SERVICE = {"PMP", "PMT", "PS"};

    public interface PlayLink {
        interface EnvironmentKey {
            String ALLOWED_HOSTS = "ALLOWED_HOSTS";
            String TARGET_SYSTEMS = "TARGET_SYSTEMS";
        }
    }

    /* XMD Constants START */
    public interface Xmd {
        interface HeaderKey {
            String SERVICE = "X-XMD-ST"; // XMD - Service Type
            String TENANT = "X-XMD-TID"; // XMD - Tenant ID
            String MEMBER = "X-XMD-MID"; // XMD - Member ID
        }

        interface SessionKey {
            String TENANT = "TENANT_INFO"; // XMD - Tenant ID
        }

        interface RequestGroupPrefix {
            String REGISTER_TENANT = "RT_";
            String ISSUE_MALL_KEY = "IK_";
            String GATHER_ORDER = "GO_";
            String GATHER_PRODUCT = "GP_";
            String MODIFY_ORDER_STATUS = "MOS_";
            String REGISTER_INVOICE = "RI_";
            String GATHER_CLAIM = "GC_";
            String GATHER_QNA = "GQ_";
            String SOLD_OUT = "SO_";
            String GOODSFLOW_GET_CONTRACTS = "FC_";
            String GOODSFLOW_GET_OTP = "FO_";
            String GOODSFLOW_SEND_ORDERS = "FS_";
            String GOODSFLOW_CANCEL_ORDERS = "FD_";
            String GOODSFLOW_PRINT_RESULT = "FR_";
        }

        interface KeepRawKey {
            String REQUEST = "keep-req-xml";
            String RESPONSE = "keep-res-xml";
        }
    }
    /* XMD Constants END */

    /* 샵플링 주문수집 자동화 관련 정보 START */
    public interface Selenium {
        String JSOUP_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36";

        interface HEADER {
            String HOST = "a.shopling.co.kr";
            String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8";
            String ACCEPT_ENCODING = "gzip, deflate";
            String ACCEPT_LANGUAGE = "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7";
        }
    }
    /* 샵플링 관리자 서비스 정보 END */

    /* 샵플링 서비스 정보 START */
    public interface Shopling {
        String[] GATHER_FIELDS = {
                "ord_no",                   // 관리번호(샵플링주문번호)
                "mall_key",                 // 쇼핑몰코드
                "mall_login_id",            // 쇼핑몰아이디
                "mall_ord_id",              // 쇼핑몰주문번호
                "mall_ord_seq",             // 주문순번
                "mall_dlvy_id",             // 배송번호,배송지시번호,합포장번호, 출고번호
                "mall_dlvy_seq",            // 배송순번
                "mall_ord_dt",              // 몰 주문일자
                "mall_pay_dt",              // 몰 결제일자
                "ord_tp",                   // 주문유형
                "auto_fg",                  // 자동수집구분
                "ord_status",               // 주문상태
                "prod_id",                  // Shopling 매핑 상품번호
                "opt_id",                   // Shopling 매핑 옵션코드
                "t_prod_nm",                // Shopling 매핑 상품명
                "t_opt_valu",               // Shopling 매핑 옵션명
                "buying_cd",                // Shopling 매핑 매입처 코드
                "tax_tp",                   // Shopling 매핑 과세구분
                "logis_cd",                 // Shopling 매핑 물류처코드
                "ptn_goods_cd",             // Shopling 매핑 자사상품코드
                "mall_user_id",             // 주문자정보 : 주문자ID
                "mall_user_nm",             // 주문자정보 : 주문자명
                "mall_user_tel",            // 주문자정보 : 주문자전화번호
                "mall_user_phone",          // 주문자정보 : 주문자휴대폰
                "mall_user_email",          // 주문자정보 : 주문자이메일
                "mall_rcv_tel",             // 수취인정보 : 수취인전화번호
                "mall_rcv_phone",           // 수취인정보 : 수취인휴대폰
                "mall_rcv_email",           // 수취인정보 : 수취인이메일
                "mall_rcv_nm",              // 수취인정보 : 수취인명
                "mall_rcv_en_nm",           // 수취인정보 : 수취인 영문명
                "mall_rcv_zipcd",           // 수취인정보 : 수취인우편번호
                "mall_rcv_addr1",           // 수취인정보 : 수취인 주소1
                "mall_rcv_addr2",           // 수취인정보 : 수취인 주소2
                "mall_rcv_add1",            // 수취인정보 : 수취인 도로명 주소1
                "mall_rcv_add2",            // 수취인정보 : 수취인 도로명 주소2
                "mall_rcv_msg",             // 수취인정보 : 배송메세지
                "mall_ord_tp",              // 수집주문정보 : 주문구분
                "mall_ord_amt",             // 수집주문정보 : 주문총금액
                "mall_pay_amt",             // 수집주문정보 : 결제총금액
                "mall_pay_tp",              // 수집주문정보 : 결제방법
                "mall_unit_price",          // 수집주문정보 : 판매단가
                "mall_supply_price",        // 수집주문정보 : 쇼핑몰공급가
                "mall_dlvy_amt_tp2",        // 수집주문정보 : 배송비지불방법
                "mall_dlvy_amt",            // 수집주문정보 : 배송비
                "mall_ord_cnt",             // 수집주문정보 : 주문수량
                "djustment_amt",            // 정산금액
                "org_price",                // 원가
                "dlvy_id",                  // 운송장정보 : 택배사코드
                "dlvy_no",                  // 운송장정보 : 운송장번호
                "dlvy_send_dt",             // 운송장정보 : 쇼핑몰운송장전송일
                "dlvy_tp",                  // 운송장정보 : 쇼핑몰운송장전송결과
                "mall_prod_key",            // 상품수집정보 : 쇼핑몰상품코드
                "mall_ptn_goods_cd",        // 상품수집정보 : 자사상품코드
                "mall_prod_nm",             // 상품수집정보 : 상품명
                "mall_opt_valu",            // 상품수집정보 : 옵션명
                "mall_opt_price",           // 상품수집정보 : 옵션가격
                "mall_opt_cd",              // 상품수집정보 : 옵션코드
                "mall_add_valu",            // 상품수집정보 : 추가상품명
                "mall_add_price",           // 상품수집정보 : 추가금액
                "mall_gift",                // 상품수집정보 : 사은품
                "mall_int_tp",              // 기타수집정보 : 설치방법
                "mall_int_amt",             // 기타수집정보 : 설치비
                "mall_sale_root",           // 기타수집정보 : 매출매체정보
                "mall_expect_dt",           // 배송희망일
                "org_ord_no",               // 원주문번호
                "i_dt",                     // 주문생성일
        };

        String[] PRODUCT_FIELDS = {
                "goods_key",            // 상품코드
                "cate_all_nm",          // 표준카테고리 전체분류명
                "ptn_goods_cd",         // 자사상품코드
                "prod_tp",              // 상품구분
                "prod_nm",              // 상품명
                "prod_exp_nm",          // 노출상품명
                "prod_abbr",            // 상품약어
                "prod_en_nm",           // 영문상품명
                "prod_en_cn",           // 중문상품명
                "prod_en_jp",           // 일어상품명
                "buying_cd",            // 매입처코드
                "site_srch",            // 사이트검색어 구분자","
                "org_price",            // 원가(매입가)
                "sale_price",           // 판매가
                "list_price",           // 리스트가(태그가)
                "margin",               // 마진율
                "tax_tp",               // 과세구분
                "dlvy_tp",              // 배송비구분
                "dlvy_cost",            // 배송비
                "isbn",                 // ISBN
                "brand_nm",             // 브랜드명
                "adult_yn",             // 성인용품여부
                "season_tp",            // 계절정보
                "sex_tp",               // 남여구분
                "age_tp",               // 연령
                "sale_area",            // 판매지역정보
                "expire_dt",            // 상품유효일
                "prod_weight",          // 무게(g으로 표시)
                "maker_nm",             // 제조사
                "make_yy",              // 제조년도
                "make_dt",              // 제조일(발행일)
                "model_no",             // 모델NO
                "model_nm",             // 모델명
                "sale_status",          // 판매상태
                "origin_nm",            // 원산지-대분류
                "origin_dtl_nm",        // 원산지-중분류
                "origin_dtl2_nm",       // 원산지-소분류
                "origin_food",          // 식품재료원산지
                "food_tp",              // 농수산물구분
                "import_no",            // 수입시 수입신고번호
                "import_img",           // 수입시 수입면장이미지
                "safe_cert_tp",         // 인증 대상 구분
                "safe_cert_item",       // 인증대상품목
                "safe_cmpy_nm",         // 인증받은 상호명(업체명)
                "safe_cert_no",         // 인증번호
                "safe_cert_int",        // 인증기관
                "safe_cert_iss_dt",     // 발급일자
                "safe_cert_dt",         // 인증일자
                "safe_cert_s_dt",       // 인증시작일자
                "safe_cert_e_dt",       // 인증만료일
                "safe_cert_img",        // 인증서이미지
                "notify_cert_tp",       // 신고대상 구분
                "notify_cert_int",      // 신고기관명
                "notify_cert_no",       // 신고번호
                "notify_cert_item_no",  // 품목허가번호
                "notify_cert_adv_yn",   // 사전광고 심의필 여부
                "notify_cert_adv_no",   // 사전광고심의번호
                "dtl_desc",             // 상품상세설명
                "dtl_simple_desc",      // 상품요약설명
                "dtl_add1_desc",        // 상품추가상세설명1
                "dtl_add2_desc",        // 상품추가상세설명2
                "dtl_add3_desc",        // 상품추가상세설명3
                "img_0",                // 기본이미지
                "img_19",               // 고정이미지
                "ptn_seller_cd",        // 제휴사 판매자 코드
                "prod_group",           // 상품그룹명
        };
    }
    /* 샵플링 서비스 정보 END */

    public interface GoodsFlow {
        interface HeaderKey {
            String API_KEY = "goodsFLOW-Api-Key";
        }

    }
}
