package com.myjob.useapi.controller;

import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.myjob.useapi.dao.GoodsDao;
import com.myjob.useapi.dao.MemberDao;
import com.myjob.useapi.vo.GoodsCart;
import com.myjob.useapi.vo.GoodsVo;
import com.myjob.useapi.vo.MemberVo;
import com.myjob.useapi.vo.ProductPhoto;


import jakarta.servlet.http.HttpSession;

@RestController
public class ShopController {

    public static int cnt =0;
    public static String uploadfolder = "H:\\job4project\\useapi\\src\\main\\resources\\static\\upload\\";
    @Autowired
    GoodsDao goodsDao;
    @Autowired
    MemberDao memberDao;
    
    // // 쇼핑결제폼 보기
    // @RequestMapping(path="checkout")
    // public ModelAndView checkout(){
    //     ModelAndView mv = new ModelAndView();
    //     mv.setViewName("shopping/payment");
    //     return mv;
    // }

    // 상품등록 폼보기
    @RequestMapping(path="/goodsRegister")
    public ModelAndView goddsRegister(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("shopping/goodsRegister");
        return mv;
    }
    // 상품등록 하기
    @RequestMapping(path="/productRegister")
    public ModelAndView productRegister(GoodsVo vo, @RequestParam("unitsPhoto") List<MultipartFile> photos){
        System.out.println("vo:"  + vo);
        System.out.println(photos.toString());
        ModelAndView mv = new ModelAndView();
        String msg="";
        String sysfile="";
        UUID uuid = null;
        List<ProductPhoto> photoFiles=new ArrayList<>();
        if(photos.size()>0){
            for(MultipartFile f: photos){
                if(f.getOriginalFilename().equals("")) continue;
                uuid = UUID.randomUUID();
                sysfile = String.format("%s-%s",uuid,f.getOriginalFilename());
               
                File file = new File(uploadfolder+sysfile);
                try{
                    f.transferTo(file);
                }catch(Exception e){
                    e.printStackTrace();
                }

                ProductPhoto pp = new ProductPhoto();
                pp.setOrifile(f.getOriginalFilename());
                pp.setSysfile(sysfile);
                pp.setGoodsName(vo.getGoodsName());
                photoFiles.add(pp);

            }
        }
        msg = goodsDao.goodsRegister(vo,photoFiles);
        Map<String,Object> map = new HashMap<>();
        List<GoodsVo> gv = new ArrayList<>();
        
        gv = goodsDao.goodsBri();
        System.out.println(gv);
        map.put("msg",msg);
        map.put("gv",gv);
        mv.addObject("map", map);
        mv.setViewName("menu/shop");
        return mv;
    }

    //카트집어넣기
    @RequestMapping(path="/goodsOrder")
    public ResponseEntity<String> goodsCart(@RequestParam("goodsName") String goodsName,@RequestParam("goodsPrice") int goodsPrice, HttpSession se){
        String msg = "";
        String orderCode="";
        String id = (String)se.getAttribute("id");
        orderCode =(String)se.getAttribute("orderCode");
        System.out.println(goodsName);
       
        
        if(orderCode == null){
                orderCode= goodsDao.orderCode(id, se);
                System.out.println(orderCode);
             }
            GoodsVo vo = new GoodsVo();
            vo.setGoodsName(goodsName);
             vo.setGoodsPrice(goodsPrice);
            msg = goodsDao.goodsCartAdd(orderCode,vo);
          
          
            
       

        return ResponseEntity.ok(msg);
    }
    //결제창으로
    @RequestMapping(path="/gotoPayment")
    public ModelAndView gotoPayment(HttpSession session){
        ModelAndView mv = new ModelAndView();
        String id = (String)session.getAttribute("id");
        //주문코드 가지고 오기

        String orderCode = goodsDao.getOrderCode(id);
        if(!orderCode.equals("empty")){
            // 개인 정보 가져오기
                 MemberVo vo = memberDao.personInfo(id); 
            //주문 카트 목록 가져오기
            List<GoodsVo> list = goodsDao.getGoods(orderCode);
            GoodsCart gc = new GoodsCart();
            gc.setOrders(list);
            int sum = gc.goodsCal();
            mv.addObject("list", list);
            mv.addObject("sum", sum);
            mv.addObject("vo", vo);
            mv.setViewName("shopping/payment");
        }else{
            mv.setViewName("shopping/payment");
        }

        return mv;

    }
    
}
