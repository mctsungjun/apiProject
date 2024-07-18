package com.myjob.useapi.json;

import java.util.List;




public class Response {
    public String startDate;
    public String endDate;
    public String timeUnit;
    public List<Result> results;


     // Getters and setters
     public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }


    @Override
    public String toString() {
        return "Response{" +
                "startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", timeUnit='" + timeUnit + '\'' +
                ", results=" + results +
                '}';
    }
    
}
