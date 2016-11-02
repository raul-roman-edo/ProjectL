package com.insurancetelematics.team.projectl.core;

public class BaseApiResponse<T> extends Result<T> {
    private static final String EMPTY = "";
    private static final int DEFAULT_DATE = 0;
    private int code = 0;
    private String errorCode = EMPTY;
    private String errorMessage = EMPTY;
    private long lastModify = DEFAULT_DATE;

    public int getCode() {
        return code;
    }

    public BaseApiResponse setCode(int code) {
        this.code = code;
        return this;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public BaseApiResponse setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public BaseApiResponse setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public long getLastModify() {
        return lastModify;
    }

    public void setLastModify(long lastModify) {
        this.lastModify = lastModify;
    }
}
