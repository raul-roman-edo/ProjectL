package com.insurancetelematics.team.projectl.comments;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.insurancetelematics.team.projectl.BuildConfig;
import com.insurancetelematics.team.projectl.comments.cards.comments.Comment;
import com.insurancetelematics.team.projectl.core.BaseApiResponse;
import com.insurancetelematics.team.projectl.core.BaseHTTPResponse;
import com.insurancetelematics.team.projectl.core.NetworkUtils;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class GetRemoteCommentsCommand {
    private static final String URL = BuildConfig.COMMENTS_URL;
    private static final String USER_AGENT = "Android";
    private static final String EMPTY_LIST = "[]";
    private final NetworkUtils network;

    public GetRemoteCommentsCommand(NetworkUtils network) {
        this.network = network;
    }

    public BaseApiResponse<List<Comment>> request() {
        BaseApiResponse<List<Comment>> apiResponse = new BaseApiResponse<>();
        try {
            BaseHTTPResponse response = network.simpleGetRequest(new URL(URL), USER_AGENT);
            Gson gson = new Gson();
            java.lang.reflect.Type listType = new TypeToken<List<Comment>>() {
            }.getType();
            String rawBody = response.body();
            if (rawBody == null || rawBody.isEmpty()) {
                rawBody = EMPTY_LIST;
            }
            List<Comment> comments = gson.fromJson(rawBody, listType);
            apiResponse.setCode(response.code());
            apiResponse.setPayload(comments);
        } catch (MalformedURLException ignore) {
        }

        return apiResponse;
    }
}
