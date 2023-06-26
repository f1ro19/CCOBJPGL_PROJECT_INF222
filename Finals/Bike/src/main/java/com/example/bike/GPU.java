package com.example.bike;

public class GPU {
    private String name;
    private int performance;

    public GPU(String name, String performance) {
        this.name = name;
        this.performance = Integer.parseInt(performance);
    }

    public String getName() {
        return name;
    }

    public int getPerformance() {
        return performance;
    }

    @Override
    public String toString() {
        return name;
    }
}
