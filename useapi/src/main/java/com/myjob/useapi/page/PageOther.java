package com.myjob.useapi.page;

import lombok.Data;

@Data
public class PageOther {
    
    int nowPage;
    int startNo, endNo;
    int listSize=5;
    int totSize;
    String findStr;

    public void compute(){
        endNo = nowPage * listSize;
        startNo = endNo - listSize;
    }
}
