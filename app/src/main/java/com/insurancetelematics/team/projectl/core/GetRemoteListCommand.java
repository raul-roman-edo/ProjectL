package com.insurancetelematics.team.projectl.core;

import com.google.gson.Gson;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class GetRemoteListCommand<Item> {
    private static final String USER_AGENT = "Android";
    private static final String EMPTY_LIST = "[]";
    private final NetworkUtils network;
    private final String url;
    private final Type listType;

    public GetRemoteListCommand(NetworkUtils network, String url, Type listType) {
        this.network = network;
        this.url = url;
        this.listType = listType;
    }

    public BaseApiResponse<List<Item>> request() {
        BaseApiResponse<List<Item>> apiResponse = performRequest();

        return apiResponse;
    }

    private BaseApiResponse<List<Item>> performRequest() {
        BaseApiResponse<List<Item>> apiResponse = new BaseApiResponse<>();
        try {
            BaseHTTPResponse response = network.simpleGetRequest(new URL(url), USER_AGENT);
            Gson gson = new Gson();
            String rawBody = response.body();
            if (rawBody == null || rawBody.isEmpty()) {
                rawBody = EMPTY_LIST;
            }
            List<Item> items = gson.fromJson(rawBody, listType);
            apiResponse.setCode(response.code());
            apiResponse.setPayload(items);
        } catch (MalformedURLException ignore) {
        }
        return apiResponse;
    }
}
