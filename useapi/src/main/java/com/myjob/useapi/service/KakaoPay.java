package com.myjob.useapi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myjob.useapi.dto.PayCancelDto;
import com.myjob.useapi.vo.ApproveResponse;
import com.myjob.useapi.vo.GoodsCart;
import com.myjob.useapi.vo.GoodsVo;
import com.myjob.useapi.vo.ReadyResponse;

import lombok.extern.log4j.Log4j;


@Log4j
@Service
public class KakaoPay {
    // 주석으로 처리한 코드는 아마도  MultiValueMap에서 문제가 발생함 정확한 원인은 모름
    
    // private HttpHeaders getHeaders(){
    //    HttpHeaders headers = new HttpHeaders();
    //    headers.set("Authorization","SECRET_KEY DEV42B8A4C24E846AA7693A8CE5BE5E9A098CA2A");
    //    headers.set("Content-Type", "application/json");
    //     return headers;
    // }

    // public ReadyResponse payReady(String orderCode, String id, GoodsCart gc){
        
      
       
    //     String orderId= orderCode;
    //     List<GoodsVo> list = gc.getOrders();
    //     List<String> names = new ArrayList<>();
    //     for(GoodsVo v: list){
    //         names.add(v.getGoodsName());
    //     }

    //     int quantity = gc.totalEa();
    //     int totalAmount =gc.goodsCal();


    //     //하나의 키에 여러개의 값을 저장하는 특징. 스프링에서 제공
    //     MultiValueMap<String, String> paramters = new LinkedMultiValueMap<String,String>();
    //     paramters.add("cid", "TC0ONETIME");
    //     paramters.add("partner_order_id", orderId+"}");
    //     paramters.add("partner_user_id", id);
        
         
    //     paramters.add("item_name", names.toString());
    
    // paramters.add("quantity", String.valueOf(quantity));
    //     paramters.add("total_amount", String.valueOf(totalAmount));
    //     paramters.add("tax_free_amount", "0");
    //     paramters.add("approval_url", 
    //                     "http://localhost:8080/kakaopaysuccess");
    //     paramters.add("cancel_url", "http://localhost:8080/kakaopaysfail");
    //     paramters.add("fail_url", "http://localhost:8080/kakaopayscancel");
    //     System.out.println("Request Parameters: " + paramters);
    //     HttpEntity<MultiValueMap<String,String>> requesEntity = new HttpEntity<>(paramters, this.getHeaders());
    //     String url =  "https://open-api.kakaopay.com/online/v1/payment/ready";
    //     RestTemplate template = new RestTemplate();
        
    //     ReadyResponse readyResponse = template.postForObject(url, requesEntity, ReadyResponse.class);
    //     System.out.println(readyResponse);
    //         return readyResponse;
        

    // }
    // json으로 변환해서 보냄 처음 요청하면 tid 와 url(결제 qr코드)받아옴
    public ReadyResponse payReady(String orderCode, String id, GoodsCart gc) throws Exception {
  
    List<GoodsVo> list = gc.getOrders();
    List<String> names = new ArrayList<>();
    for (GoodsVo v : list) {
        names.add(v.getGoodsName());
    }

    int quantity = gc.totalEa();
    System.out.println("총수량: "+ quantity);
    int totalAmount = gc.goodsCal();

    // Create a Map to hold the request parameters
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("cid", "TC0ONETIME");
    parameters.put("partner_order_id", orderCode);
    parameters.put("partner_user_id", id);
    parameters.put("item_name", names.toString());
    parameters.put("quantity", String.valueOf(quantity));
    parameters.put("total_amount", totalAmount);
    parameters.put("tax_free_amount", 0);
    parameters.put("approval_url", "http://localhost:8080/kakaopaysuccess");
    parameters.put("cancel_url", "http://localhost:8080/kakaopaysfail");
    parameters.put("fail_url", "http://localhost:8080/kakaopayscancel");

    // Convert Map to JSON
    ObjectMapper objectMapper = new ObjectMapper();
    String json = objectMapper.writeValueAsString(parameters);

    // Create HttpHeaders and set Content-Type to application/json
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization","SECRET_KEY DEV42B8A4C24E846AA7693A8CE5BE5E9A098CA2A");
    headers.set("Content-Type", "application/json");

    // Create HttpEntity with JSON body and headers
    HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);

    // Define the URL for the request
    String url = "https://open-api.kakaopay.com/online/v1/payment/ready";

    // Create RestTemplate instance
    RestTemplate template = new RestTemplate();

    // Send POST request and get the response
    ResponseEntity<ReadyResponse> responseEntity = template.exchange(url, HttpMethod.POST, requestEntity, ReadyResponse.class);

    // Extract response body
    ReadyResponse readyResponse = responseEntity.getBody();
    
    
    return readyResponse;
    }
    public ApproveResponse payApprove(String tid, String id, String pgToken, String orderCode) throws Exception {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("cid", "TC0ONETIME");
        parameters.put("tid",tid);
        parameters.put("partner_order_id",orderCode);
        parameters.put("partner_user_id",id);
        parameters.put("pg_token",pgToken);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(parameters);

        HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization","SECRET_KEY DEV42B8A4C24E846AA7693A8CE5BE5E9A098CA2A");
    headers.set("Content-Type", "application/json");

    HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);

    // Define the URL for the request
    String url = "https://open-api.kakaopay.com/online/v1/payment/approve";
    RestTemplate restTemplate = new RestTemplate();
    
    ResponseEntity<ApproveResponse> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, ApproveResponse.class);
    ApproveResponse approveResponse = responseEntity.getBody();
    System.out.println(approveResponse);
        return approveResponse;
    }

    //카카오 취소 요청
    public PayCancelDto kakaoPayCancel(GoodsCart goodsCart, int total) throws Exception{
        String tid =goodsCart.getTid();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("cid", "TC0ONETIME");
        parameters.put("tid",tid);
        parameters.put("cancel_amount",total);
        parameters.put("cancel_tax_free_amount",0);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(parameters);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","SECRET_KEY DEV42B8A4C24E846AA7693A8CE5BE5E9A098CA2A");
        headers.set("Content-Type", "application/json");

        HttpEntity<String> requesEntity = new HttpEntity<>(json, headers);

        String url = "https://open-api.kakaopay.com/online/v1/payment/cancel";
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<PayCancelDto> responseEntity = restTemplate.exchange(url,HttpMethod.POST, requesEntity, PayCancelDto.class);
        PayCancelDto payCancelDto = responseEntity.getBody();
        
        return payCancelDto;

    }

}
