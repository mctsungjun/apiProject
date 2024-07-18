package com.myjob.useapi.json;

public class Data {
    private String period;
    private double ratio;

    // Getters and setters
    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    @Override
    public String toString() {
        return "Data{" +
                "period='" + period + '\'' +
                ", ratio=" + ratio +
                '}';
    }
}
