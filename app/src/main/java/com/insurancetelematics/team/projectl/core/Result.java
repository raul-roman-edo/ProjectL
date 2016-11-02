package com.insurancetelematics.team.projectl.core;

public class Result<T> {
    private T payload = null;

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}