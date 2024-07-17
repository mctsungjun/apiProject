package com.myjob.useapi.controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myjob.useapi.dto.SearchTrendDto;
import com.myjob.useapi.searchtrendapi.SearchTrend;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
public class TrendController {
    
    @RequestMapping(path="/trendFormApi")
    public String trendApi(@ModelAttribute SearchTrendDto trend){
        System.out.println(trend);
        String requestString = "";
        //폼으로 자료를 받아서 네이버api로 요청하기 위해 객체 생성
        SearchTrend st = new SearchTrend();

        requestString = st.post(st.apiUrl, st.getHeaders(), st.getBody(trend.getStartDate(),trend.getEndDate(),trend.getTimeUnit(),trend.getGroupName(),trend.getKeywords(),trend.getGroupName2(),trend.getKeywords2(),trend.getDevice(),trend.getAge(),trend.getGender()));

        //json 파싱
        JsonParser parser = new JsonParser();
        JsonElement je = parser.parse(requestString);
        System.out.println(je);
        return requestString;

    }


}
