package com.insurancetelematics.team.projectl.core;

public interface Dispatcher<Result> {
    void dispatch(Result result);
}
