package com.myjob.useapi.controller;


import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myjob.useapi.dao.GoodsDao;
import com.myjob.useapi.dao.MemberDao;
import com.myjob.useapi.vo.GoodsCart;
import com.myjob.useapi.vo.GoodsVo;
import com.myjob.useapi.vo.MemberVo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
public class NicepayController {
    @Autowired
    GoodsDao goodsDao;
    @Autowired
    MemberDao memberDao;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String CLIENT_ID ="S2_3f4413a5257d47a98cc08bf95daef799";
    private final String SECRET_KEY = "e957bce657464cb18375b112e39e7705";

    @RequestMapping(path="/nicepayment")
    public ModelAndView callNicepayment(HttpSession se){
        ModelAndView mv = new ModelAndView();
        String id = (String)se.getAttribute("id");
        String orderCode = goodsDao.getOrderCode(id);
       List<GoodsVo> list = goodsDao.getGoods(orderCode);
       GoodsCart gc = new GoodsCart();
       gc.setOrders(list);
       List<GoodsVo> lists = gc.getOrders();
      List<String> names = new ArrayList<>();
     MemberVo vo = memberDao.personInfo(id);
       for (GoodsVo v : lists) {
        names.add(v.getGoodsName());
    }

     int quantity = gc.totalEa();
     int sum = gc.goodsCal();
     System.out.println("총수량: "+ quantity);
     int totalAmount = gc.goodsCal();
     mv.addObject("orderId",orderCode);
     mv.addObject("clientId", CLIENT_ID);
     mv.addObject("amount", totalAmount);
     mv.addObject("goodsName", names);
     mv.addObject("list", list);
     mv.addObject("sum", sum);
     mv.addObject("vo", vo);
     mv.setViewName("shopping/payment");
    return mv;
    }

    @RequestMapping(path = "/nice/serverAuth", method=RequestMethod.POST)
    public ModelAndView requestNicepay(@RequestParam("tid") String tid,@RequestParam ("amount")Long amount) throws Exception {
            ModelAndView mv = new ModelAndView();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic "+Base64.getEncoder().encodeToString((CLIENT_ID+":"+SECRET_KEY).getBytes()));
            headers.set("Content-Type", "application/json;charset=utf-8");

            Map<String, Object> AuthenticationMap = new HashMap<>();
            AuthenticationMap.put("amount", String.valueOf(amount));
            HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(AuthenticationMap),headers);

            ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity( "https://sandbox-api.nicepay.co.kr/v1/payments/" + tid, request, JsonNode.class);

            JsonNode responseNode = responseEntity.getBody();
            String resultCode = responseNode.get("resultCode").asText();
            System.out.println(responseNode.toPrettyString());
            mv.setViewName("nicepay/responseNice");
            return mv;
        }

          @RequestMapping(value = "/nice/hook") 
    public ResponseEntity<String> hook(@RequestBody HashMap<String, Object> hookMap) throws Exception {
        String resultCode = hookMap.get("resultCode").toString();

        System.out.println(hookMap);

        if(resultCode.equalsIgnoreCase("0000")){
            return ResponseEntity.status(HttpStatus.OK).body("ok");
        }
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }    
        
    }
