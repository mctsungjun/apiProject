package com.myjob.useapi.vo;

import java.util.List;

import lombok.Data;

@Data
public class GoodsCart {
    
    int sno; 
    String orderCode;
    String goodsName;
    String goodsPrice;
    List<GoodsVo> orders;

    public int goodsCal(){
        int sum = 0;
        if(orders.size()>0){
            for(GoodsVo order: orders){
                sum = sum + order.goodsPrice;
            }
        }
        return sum;
    }
}
