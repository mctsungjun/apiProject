package com.myjob.useapi.controller;

import java.util.ArrayList;
import java.util.List;



import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.myjob.useapi.dto.SearchTrendDto;

import com.myjob.useapi.json.Data;
import com.myjob.useapi.json.Response;
import com.myjob.useapi.json.Result;
import com.myjob.useapi.page.TrendPager;
import com.myjob.useapi.searchtrendapi.SearchTrend;
import com.google.gson.Gson;


@RestController
public class TrendController {
    
    @RequestMapping(path="/trendFormApi")
    public ModelAndView trendApi(@ModelAttribute SearchTrendDto trend ){
        ModelAndView mv = new ModelAndView();
        String keywords = "";
        String keywords2 = "";
        List<String> periods = new ArrayList<>();
        List<Double> ratios = new ArrayList<>();
        List<String> periods2 = new ArrayList<>();
        List<Double> ratios2 = new ArrayList<>();
        
        String requestString = "";
        //폼으로 자료를 받아서 네이버api로 요청하기 위해 객체 생성
        SearchTrend st = new SearchTrend();
        //keywords 가 배열이어서
        String[] titles1 = trend.getKeywords();
        String[] titles2 = trend.getKeywords2();
        requestString = st.post(st.apiUrl, st.getHeaders(), st.getBody(trend.getStartDate(),trend.getEndDate(),trend.getTimeUnit(),trend.getGroupName(),trend.getKeywords(),trend.getGroupName2(),trend.getKeywords2(),trend.getDevice(),trend.getAge(),trend.getGender()));

        //json 파싱
        Gson gson = new Gson();
        Response response = gson.fromJson(requestString, Response.class);
        List<Result> results = response.getResults();
            for(Result result:results){
                if(result.getTitle().equals(titles1[0])){
                    keywords =result.getTitle();
                    List<Data> datas = result.getData();
                    for(Data data : datas){
                        String period = data.getPeriod();
                        double ratio = data.getRatio();
                        periods.add(period);
                        ratios.add(ratio);
                    }
                }
                if(result.getTitle().equals(titles2[0])){
                    keywords2 =result.getTitle();
                    List<Data> datas = result.getData();
                    for(Data data : datas){
                        String period2 = data.getPeriod();
                        double ratio2 = data.getRatio();
                        periods2.add(period2);
                        ratios2.add(ratio2);
                    }
                }
            }
          
         
            mv.addObject("keywords", keywords);
            mv.addObject("keywords2", keywords2);
            mv.addObject("period", periods);
            
            mv.addObject("ratios", ratios);
            mv.addObject("ratios2", ratios2);

            mv.setViewName("dashboardshow");
            return mv;
        }


    }



