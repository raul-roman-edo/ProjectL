package com.insurancetelematics.team.projectl.core;

import java.util.List;
import java.util.Map;

public class BaseHTTPResponse {

    private int code = 0;
    private Map<String, List<String>> headers = null;
    private String body = null;

    public BaseHTTPResponse code(int code) {
        this.code = code;
        return this;
    }

    public int code() {
        return code;
    }

    public BaseHTTPResponse headers(Map<String, List<String>> headers) {
        this.headers = headers;
        return this;
    }

    public Map<String, List<String>> headers() {
        return headers;
    }

    public List<String> headers(String key) {
        if (headers == null) {
            return null;
        }
        return headers.get(key);
    }

    public BaseHTTPResponse body(String body) {
        this.body = body;
        return this;
    }

    public String body() {
        return body;
    }
}
