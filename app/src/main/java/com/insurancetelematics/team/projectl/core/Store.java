package com.insurancetelematics.team.projectl.core;

public interface Store<T> {
    T load();

    void save(T data);
}