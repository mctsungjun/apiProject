package com.myjob.useapi.vo;

import lombok.Data;

@Data
public class PaymentVo {
    
    private int payCode; //일련번호 관리
    private String ordCode; //주문번호
    private String payMethod; //결제방식선택
    private String payDate; //결제일
    private int payTotPrice; //결제금액
    private int payRestPrice; //미지급금
    private String payNobankUser; //입금자명(무통장)
    private String payNobank; //입금은행
}
