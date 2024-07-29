package com.myjob.useapi.controller;

import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.myjob.useapi.dao.GoodsDao;

import com.myjob.useapi.vo.GoodsVo;
import com.myjob.useapi.vo.ProductPhoto;
import com.myjob.useapi.vo.UserOrder;

import jakarta.servlet.http.HttpSession;

@RestController
public class ShopController {

    public static int cnt =0;
    public static String uploadfolder = "H:\\job4project\\useapi\\src\\main\\resources\\static\\upload\\";
    @Autowired
    GoodsDao goodsDao;
    
    // 쇼핑결제폼 보기
    @RequestMapping(path="checkout")
    public ModelAndView checkout(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("shopping/payment");
        return mv;
    }

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
    public ResponseEntity<String> goodsCart(@RequestParam("goodsName") String goodsName, @RequestParam(name = "orderCode", required=false) String code , Model model,HttpSession se){
        String msg = "";
        String orderCode="";
        String id = (String)se.getAttribute("id");
        System.out.println(goodsName);
        System.out.println(code);
        
        if(code.equals("f") ){
                orderCode= goodsDao.orderCode(id);
                System.out.println(orderCode);
             }
            GoodsVo vo = new GoodsVo();
            vo.setGoodsName(goodsName);

            msg = goodsDao.goodsCartAdd(orderCode,vo);
             model.addAttribute("code", orderCode);
       
       

        return ResponseEntity.ok(msg);
    }
    
}
