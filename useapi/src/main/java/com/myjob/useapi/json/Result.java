package com.myjob.useapi.json;

import java.util.List;

public class Result {
      private String title;
    private List<String> keywords;
    private List<Data> data;

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "title='" + title + '\'' +
                ", keywords=" + keywords +
                ", data=" + data +
                '}';
    }
}
