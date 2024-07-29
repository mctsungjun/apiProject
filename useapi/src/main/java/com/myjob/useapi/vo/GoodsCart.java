package com.myjob.useapi.vo;

import java.util.List;

import lombok.Data;

@Data
public class GoodsCart {
    
    int sno; 
    String orderCode;
    String goodsName;
    List<GoodsVo> order;
}
