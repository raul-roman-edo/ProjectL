package com.insurancetelematics.team.projectl.core;

public interface AsyncActionListener<T> {
    void onFinished(T result);
}
