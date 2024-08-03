package com.myjob.useapi.dto;

import com.myjob.useapi.vo.Amount;

import lombok.Data;
@Data
public class PayCancelDto {
    String aid;
    String tid;
    String cid;
    String status;
    String partner_order_id;
    String partner_user_id;
    String payment_method_type;
    Amount amount;
    String item_name;
    String item_code;
    int quantity;
    String created_at;
    String approved_at;
    String canceled_at;
    String payload;

}
