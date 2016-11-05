package com.insurancetelematics.team.projectl.images.gallery;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.insurancetelematics.team.projectl.BuildConfig;
import com.insurancetelematics.team.projectl.core.BaseApiResponse;
import com.insurancetelematics.team.projectl.core.BaseHTTPResponse;
import com.insurancetelematics.team.projectl.core.NetworkUtils;
import com.insurancetelematics.team.projectl.images.gallery.cards.photos.Photo;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class GetRemotePhotosCommand {
    private static final String URL = BuildConfig.PHOTOS_URL;
    private static final String USER_AGENT = "Android";
    private static final String EMPTY_LIST = "[]";
    private final NetworkUtils network;

    public GetRemotePhotosCommand(NetworkUtils network) {
        this.network = network;
    }

    public BaseApiResponse<List<Photo>> request() {
        BaseApiResponse<List<Photo>> apiResponse = new BaseApiResponse<>();
        try {
            BaseHTTPResponse response = network.simpleGetRequest(new URL(URL), USER_AGENT);
            Gson gson = new Gson();
            java.lang.reflect.Type listType = new TypeToken<List<Photo>>() {
            }.getType();
            String rawBody = response.body();
            if (rawBody == null || rawBody.isEmpty()) {
                rawBody = EMPTY_LIST;
            }
            List<Photo> photos = gson.fromJson(rawBody, listType);
            apiResponse.setCode(response.code());
            apiResponse.setPayload(photos);
        } catch (MalformedURLException ignore) {
        }

        return apiResponse;
    }
}
