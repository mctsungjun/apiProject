package com.myjob.useapi.dto;

import java.util.List;

import lombok.Data;

@Data
public class SearchTrendDto {
    
    String startDate;
    String endDate ;
    String timeUnit;
    String groupName;
    String[] keywords;
    String groupName2 ;
    String[] keywords2 ;
    String device ;
    String gender ;
    String[] age={};
    
}
