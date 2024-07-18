package com.myjob.useapi.page;




public class PageSevice {

    public TrendPager setPager(int pageSize, int totalBoard,int blockSize, int pageNum){
        TrendPager trendPager = new TrendPager(pageNum,totalBoard, pageSize,blockSize);
        
        return trendPager;
    }

    

}
