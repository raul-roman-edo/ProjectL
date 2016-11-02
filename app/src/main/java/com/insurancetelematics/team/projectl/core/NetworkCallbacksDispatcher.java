package com.insurancetelematics.team.projectl.core;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class NetworkCallbacksDispatcher<K extends BaseApiResponse> implements Dispatcher<K> {
    private static final int STATUS_CONNECTIVITY_ERROR = 0;
    private static final int STATUS_OK = 100;
    private static final int STATUS_ERROR = 400;
    private final Map<Integer, AsyncActionListener<K>> statusCallbacks;
    private final Map<String, AsyncActionListener<K>> errorCallbacks;
    private final NavigableMap<Integer, AsyncActionListener<K>> globalCallbacks;

    public NetworkCallbacksDispatcher(AsyncActionListener<K> success, AsyncActionListener<K> error,
            AsyncActionListener<K> connectivityError) {
        statusCallbacks = new HashMap<>();
        errorCallbacks = new HashMap<>();
        globalCallbacks = new TreeMap<>();
        globalCallbacks.put(STATUS_OK, success);
        globalCallbacks.put(STATUS_ERROR, error);
        globalCallbacks.put(STATUS_CONNECTIVITY_ERROR, connectivityError);
    }

    public NetworkCallbacksDispatcher addStatusCallback(int status, AsyncActionListener<K> callback) {
        statusCallbacks.put(status, callback);
        return this;
    }

    public NetworkCallbacksDispatcher addErrorCodeCallback(String errorCode, AsyncActionListener<K> callback) {
        errorCallbacks.put(errorCode, callback);
        return this;
    }

    @Override
    public void dispatch(K result) {
        int status = result.getCode();
        boolean isError = status >= STATUS_ERROR;
        AsyncActionListener<K> callback = null;
        if (isError) {
            callback = errorCallbacks.get(result.getErrorCode());
        }
        if (callback == null) {
            callback = statusCallbacks.get(status);
        }
        if (callback == null) {
            callback = globalCallbacks.floorEntry(status).getValue();
        }
        callback.onFinished(result);
    }
}
