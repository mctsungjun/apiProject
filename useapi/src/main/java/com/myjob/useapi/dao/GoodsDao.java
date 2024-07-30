package com.myjob.useapi.dao;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import com.myjob.useapi.controller.ShopController;
import com.myjob.useapi.mybatis.MyFactory;
import com.myjob.useapi.vo.GoodsVo;
import com.myjob.useapi.vo.ProductPhoto;
import com.myjob.useapi.vo.UserOrder;

import jakarta.servlet.http.HttpSession;

@Component
public class GoodsDao {
    
    SqlSession session;

    public String goodsRegister(GoodsVo vo, List<ProductPhoto> photoFiles){
        String msg ="";
        session = new MyFactory().getSession();
        int cnt = session.insert("member.goodsRegister", vo);
        if(cnt>0){
            if(photoFiles.size()>0){
                Map<String,Object> map = new HashMap<>();
                map.put("name",vo.getGoodsName());
                map.put("photos",photoFiles);
                cnt = session.insert("member.goodsRegisterPhoto",map);
                if(cnt>0){
                    System.out.println("파일저장성공");
                }
            }
            session.commit();
            msg="등록성공";

        }else{
            session.rollback();
            msg="등록실패";
        }
        return msg;

    }
    //상품불러오기
    public List<GoodsVo> goodsBri(){
        session = new MyFactory().getSession();
        List<ProductPhoto> pp = new ArrayList<>();
        List<GoodsVo> goods = session.selectList("member.goodsBri");
        pp = session.selectList("member.goodsPhotoBri");
        if(pp.size()>0){
            for(GoodsVo g:goods){
                for(ProductPhoto p:pp){
                    if(g.getGoodsName().equals(p.getGoodsName())){
                        g.setSysfile(p.getSysfile());
                    }
                }
            }
        }
        return goods;
    }
    // 주문코드생성
    public String orderCode(String id, HttpSession se){
        String orderCode="";
        session = new MyFactory().getSession();
          String orderTime = String.valueOf(System.currentTimeMillis());
        if(!LocalTime.now().equals(LocalTime.MIDNIGHT)){
            orderCode = "od"+orderTime+(ShopController.cnt+1);
            System.out.println(orderCode);
          
        }else{
            ShopController.cnt=0;
            orderCode = "od"+orderTime+(ShopController.cnt+1); 
           
        }
        se.setAttribute("orderCode", orderCode);

        Map<String,String> code = new HashMap<>();
      
        code.put("order",orderCode);
        code.put("id",id);
        
        int cnt=session.insert("member.addCodeUserOrder", code);
        if(cnt>0){
            session.commit();
            System.out.println("userorder 저장");
        }else{
            session.rollback();
        }
        session.close();
        return orderCode;
    }
   // 카트추가
   public String goodsCartAdd(String code, GoodsVo vo){
        String msg="";
        session = new MyFactory().getSession();
        String goodsName =vo.getGoodsName();
        int goodsPrice =session.selectOne("member.getprice", goodsName);

        System.out.println(goodsPrice);
        Map<String,Object> cart = new HashMap<>();
        cart.put("code", code);
        cart.put("name",goodsName);
        cart.put("price",goodsPrice);
        int cnt = session.insert("member.cartAdd", cart);
        if(cnt>0){
            msg="장바구니추가";
            session.commit();

        }else{
            msg="오류발생";
            session.rollback();
        }
        session.close();
        return msg;
   }
   // 주문코드 가지고 오기
   public String getOrderCode(String id){
    session = new MyFactory().getSession();
    String orderCode = "";
    UserOrder uo = session.selectOne("member.getOrderCode", id);
    if(uo.getOrdered()=='f'){
         orderCode = uo.getOrderCode();
    }else{
        Map<String,String> map = new HashMap<>();
        map.put("orderCode", uo.getOrderCode());
        map.put("change","t");
        session.update("member.changeOrdered", map);
        orderCode="empty";
    }
    session.commit();
    session.close();
    return orderCode;
   }
   //카트 목록 가져오기
   public List<GoodsVo> getGoods(String orderCode){
    session = new MyFactory().getSession();
    List<GoodsVo> list = session.selectList("member.getGoodsVoList", orderCode);
    session.commit();
    session.close();
    return list;
   }

}
