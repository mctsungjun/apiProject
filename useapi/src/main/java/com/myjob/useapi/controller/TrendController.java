package com.myjob.useapi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.myjob.useapi.dto.SearchTrendDto;

import com.myjob.useapi.json.Data;
import com.myjob.useapi.json.Response;
import com.myjob.useapi.json.Result;
import com.myjob.useapi.page.PageSevice;
import com.myjob.useapi.page.TrendPager;
import com.myjob.useapi.searchtrendapi.SearchTrend;

import jakarta.servlet.http.HttpSession;

import com.google.gson.Gson;


@RestController
public class TrendController {
    
    @RequestMapping(path="/trendFormApi")
    public ModelAndView trendApi(@ModelAttribute SearchTrendDto trend ,@RequestParam(name="pageSize",defaultValue = "3") int pagesize, @RequestParam(name="pageNum",defaultValue = "1") int pagenum, HttpSession session){
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
            
            // page처리를 위해서 행갯수
            PageSevice pageSevice = new PageSevice();
            // 전체 행 수
            int totalBoard = periods.size();
            int blockSize = 4;
            int pageNum = pagenum;
            int pageSize = pagesize;
            TrendPager pager = pageSevice.setPager(pageSize, totalBoard, blockSize, pageNum);
            int totalPage = pager.getTotalPage();
            int pagenumer =pager.getPageNum();
             // 세션 고정
            session.setAttribute("startDate",trend.getStartDate());
            session.setAttribute("endDate",trend.getEndDate());
            session.setAttribute("groupName",trend.getGroupName());
            session.setAttribute("keywords",trend.getKeywords()[0] );
            session.setAttribute("groupName2", trend.getGroupName2());
            session.setAttribute("keywords2",trend.getKeywords2()[0] );
            session.setAttribute("pageSize", pager.getPageSize());
           
           
            // 리스트 구간 조회
            List<String> subPeriods =periods.subList(pager.getStartRow(), pager.getEndRow());  
            List<Double> subRatios = ratios.subList(pager.getStartRow(), pager.getEndRow());  
            
            List<Double> subRatios2 = ratios2.subList(pager.getStartRow(), pager.getEndRow()); 
            
            
            mv.addObject("keywords", keywords);
            mv.addObject("keywords2", keywords2);
            mv.addObject("period", subPeriods);
            mv.addObject("period2", periods2);
            mv.addObject("ratios", subRatios);
            mv.addObject("ratios2", subRatios2);
            mv.addObject("trendPager", pager);
           
            mv.setViewName("dashboardshow");
            return mv;
        }

        @RequestMapping(path="/chartadd")
        public Map<String, Object> chartLine(@ModelAttribute SearchTrendDto trend ){
            String keywords = "";
            String keywords2 = "";
            List<String> periods = new ArrayList<>();
            List<Double> ratios = new ArrayList<>();
            List<String> periods2 = new ArrayList<>();
            List<Double> ratios2 = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
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
                map.put("keywords", keywords);
                map.put("keywords2",keywords2);
                map.put("periods",periods);
                map.put("ratios",ratios);
                map.put("ratios2",ratios2);
                return map;
        }


    }



