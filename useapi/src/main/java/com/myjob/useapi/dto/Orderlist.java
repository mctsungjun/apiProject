package com.myjob.useapi.dto;



import lombok.Data;

@Data
public class Orderlist {
     
 
    private String partner_order_id; //가맹점 주문 번호
  
    private String payment_method_type; // 결제 수단,카드 또는 머니 중 하나
   
  
    private String item_name; // 상품이름
    
    private int total; 
   
    private String approved_at; // 결제 승인 시각
   
}

