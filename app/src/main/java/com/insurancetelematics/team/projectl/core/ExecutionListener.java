package com.insurancetelematics.team.projectl.core;

public interface ExecutionListener<T extends Result> {
    void onFinished(T result);
}
