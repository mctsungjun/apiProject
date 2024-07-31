package com.myjob.useapi.vo;

import lombok.Data;

@Data
public class ApproveResponse {
    
    private String aid; // 요청 고유 번호 - 승인/취소가 구분된 결제번호
    private String tid; // 결제 고유 번호  - 승인/취소가 동일한 결제번호
    private String cid; // 가맹점 코드
    private String sid; // 정기 결제용 ID, 정기 결제 CID로 단건 결제 요청 시 발급
    private String partner_order_id; //가맹점 주문 번호
    private String partner_user_id; // 가맹점 회원 id
    private String payment_method_type; // 결제 수단,카드 또는 머니 중 하나
    private Amount amount; // 결제 금액 정보
    private CardInfo cardInfo; // 결제 상세 정보, 결제 수단이 카드일 경우만 포함
    private String item_name; // 상품이름
    private String item_code; // 상품코드
    private int quantiy; // 상품수량
    private String created_at; // 결제 준비 요청 시각
    private String approved_at; // 결제 승인 시각
    private String payload; // 결제승인 요청에 대해 저장한 값 ,용청시 전달된 내용
}
