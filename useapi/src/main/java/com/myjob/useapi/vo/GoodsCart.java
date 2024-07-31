package com.myjob.useapi.vo;

import java.util.List;

import lombok.Data;

@Data
public class GoodsCart {
    
    int sno; 
    String orderCode;
    String goodsName;
    String goodsPrice;
    int ea;
    List<GoodsVo> orders;

    public int goodsCal(){
        int sum = 0;
        if(orders.size()>0){
            for(GoodsVo order: orders){
                sum = sum + (order.goodsPrice*order.ea);
            }
        }
        return sum;
    }
    public int totalEa(){
        int total = 0;
        if(orders.size()>0){
            for(GoodsVo vo: orders){
                total = total + vo.ea;
            }
        }
        return total;
    }
}
