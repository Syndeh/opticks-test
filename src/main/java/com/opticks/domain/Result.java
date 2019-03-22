package com.opticks.domain;

public class Result {

    private Long result;

    public Result() {
        this.result = System.currentTimeMillis();
    }

    public Long getResult() {
        return result;
    }

    public void setResult(Long result) {
        this.result = result;
    }
}
