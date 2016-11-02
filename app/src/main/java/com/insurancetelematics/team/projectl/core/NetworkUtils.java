package com.insurancetelematics.team.projectl.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.json.JSONException;
import org.json.JSONObject;

public class NetworkUtils {
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MEDIA_TYPE_PLAIN = MediaType.parse("text/plain; charset=utf-8");
    public static final MediaType MEDIA_TYPE_BINARY = MediaType.parse("application/octet-stream");
    public static final MediaType MEDIA_TYPE_MULTIPART = MediaType.parse("multipart/form-data; charset=utf-8");
    public static final String MULTIPART_HEADER = "Content-Disposition: ";
    public static final String MULTIPART_FIELD_NAME = "form-data; name=\"%s\"";
    public static final String MULTIPART_FIELD_FILE = "form-data; name=\"%s\"; filename=\"%s\"";
    public static final String CONTENT_TYPE_HEADER = "Content-Type: ";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_PLAIN = "text/plain";
    private static final String TAG = NetworkUtils.class.getName();
    private static final int HTTPS_DEFAULT_PORT = 443;
    //private static final String LANG = Locale.getDefault().toString(); // es_ES
    // TODO change to this when API were prepared to support it

    private static NetworkUtils singleton = null;
    private OkHttpClient client = new OkHttpClient();

    private NetworkUtils() {
        acceptAllIncommingServerCertificates();
    }

    public synchronized static NetworkUtils getInstance() {
        if (singleton == null) {
            singleton = new NetworkUtils();
        }
        return singleton;
    }

    public BaseHTTPResponse simpleGetRequest(URL url, String userAgent) {
        return authGetRequestWithHeaders(url, userAgent, null, null, null);
    }

    public BaseHTTPResponse simpleGetRequestWithHeaders(URL url, String userAgent, Map<String, String> extraHeaders) {
        return authGetRequestWithHeaders(url, userAgent, null, extraHeaders, null);
    }

    public BaseHTTPResponse authConditionalGetRequest(URL url, String userAgent, String authToken, String eTag) {
        return authGetRequestWithHeaders(url, userAgent, authToken, null, eTag);
    }

    public BaseHTTPResponse authGetRequest(URL url, String userAgent, String authToken) {
        return authGetRequestWithHeaders(url, userAgent, authToken, null, null);
    }

    public BaseHTTPResponse authGetRequestWithHeaders(URL url, String userAgent, String authToken,
            Map<String, String> extraHeaders, String etag) {
        Request.Builder builder = new Request.Builder().url(url)
                .get()
                .addHeader("User-Agent", userAgent)
                .addHeader("Accept", "application/json");

        if (authToken != null) {
            builder.addHeader("x-auth-token", authToken);
        }

        if (etag != null) {
            builder.addHeader("If-None-Match", etag);
        }

        if (extraHeaders != null) {
            for (Map.Entry<String, String> entry : extraHeaders.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        Request request = builder.build();

        BaseHTTPResponse responseCopy = null;
        try {
            Response response = client.newCall(request).execute();

            responseCopy = copyResponse(response);
        } catch (IOException | IllegalStateException | InternalError ignore) {
        }

        return responseCopy;
    }

    public BaseHTTPResponse simpleJsonPostRequest(URL url, String userAgent, Object json) {
        return request(new Request.Builder().post(getJSONBody(json)), url, userAgent, null, CONTENT_TYPE_JSON);
    }

    public BaseHTTPResponse authJsonPostRequest(URL url, String userAgent, String token, Object json) {
        return request(new Request.Builder().post(getJSONBody(json)), url, userAgent, token, CONTENT_TYPE_JSON);
    }

    public BaseHTTPResponse authJsonPutRequest(URL url, String userAgent, String token, Object json) {
        return request(new Request.Builder().put(getJSONBody(json)), url, userAgent, token, CONTENT_TYPE_JSON);
    }

    public BaseHTTPResponse authJsonDeleteRequest(URL url, String userAgent, String token, Object json) {
        return request(new Request.Builder().delete(getJSONBody(json)), url, userAgent, token, CONTENT_TYPE_JSON);
    }

    public BaseHTTPResponse authDeleteRequestWithTextBody(URL url, String userAgent, String authToken, String textBody)
            throws IOException {

        RequestBody body = RequestBody.create(MEDIA_TYPE_PLAIN, textBody);
        return request(new Request.Builder().delete(body), url, userAgent, authToken, CONTENT_TYPE_PLAIN);
    }

    public BaseHTTPResponse authHeadRequest(URL url, String userAgent, String authToken) throws IOException {
        return request(new Request.Builder().head(), url, userAgent, authToken, null);
    }

    public BaseHTTPResponse request(Request.Builder builder, URL url, String userAgent, String authToken,
            String contentType) {
        builder.url(url).addHeader("User-Agent", userAgent).addHeader("Accept", "application/json");

        if (contentType != null) {
            builder.addHeader("Content-type", contentType);
        }

        if (authToken != null) {
            builder.addHeader("x-auth-token", authToken);
        }

        BaseHTTPResponse responseCopy = null;
        Response response = null;
        try {
            response = client.newCall(builder.build()).execute();

            responseCopy = copyResponse(response);
        } catch (IOException | IllegalStateException e) {
            try {
                responseCopy = null;
                if (response != null) {
                    response.body().close();
                }
            } catch (Exception ignored) {
            }
        }

        return responseCopy;
    }

    public String getStringFromJSON(JSONObject jSONObject, String str) throws JSONException {
        if (jSONObject != null && jSONObject.has(str)) {
            String result = jSONObject.getString(str);
            if (!"null".equals(result)) {
                return result;
            }
        }
        return null;
    }

    public boolean getBooleanFromJSON(JSONObject jSONObject, String str) throws JSONException {
        if (jSONObject != null && jSONObject.has(str)) {
            Boolean result = jSONObject.getBoolean(str);
            return result;
        }
        return false;
    }

    public boolean isInternetAvailable(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr == null) {
            return false;
        }
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return false;
        }
        if (networkInfo.isAvailable() && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public boolean isWifiInternetAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }

    public void acceptAllIncommingServerCertificates() {
        acceptAllIncommingServerCertificates(client);
    }

    public void acceptAllIncommingServerCertificates(OkHttpClient client) {
        client.setSslSocketFactory(getDefaultSSLFactory());
        client.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }

    public javax.net.ssl.SSLSocketFactory getDefaultSSLFactory() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
                                throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
                                throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            return sslSocketFactory;
        } catch (NoSuchAlgorithmException | KeyManagementException ignore) {
        }
        return null;
    }

    public BaseHTTPResponse postFileOKHttp(URL url, String userAgent, List<MultipartField> fields, String authToken) {
        return uploadFileOKHttp(url, userAgent, fields, authToken, RequestMethod.POST);
    }

    public BaseHTTPResponse putFileOKHttp(URL url, String userAgent, List<MultipartField> fields, String authToken) {
        return uploadFileOKHttp(url, userAgent, fields, authToken, RequestMethod.PUT);
    }

    private BaseHTTPResponse uploadFileOKHttp(URL url, String userAgent, List<MultipartField> fields, String authToken,
            RequestMethod type) {
        MultipartBuilder builder = new MultipartBuilder().type(NetworkUtils.MEDIA_TYPE_MULTIPART);

        boolean isFile, isByteArray;
        for (MultipartField field : fields) {
            isFile = field.getFile() != null;
            isByteArray = field.getByteArray() != null;

            RequestBody body;
            if (isFile) {
                body = RequestBody.create(NetworkUtils.MEDIA_TYPE_BINARY, field.getFile());
            } else if (isByteArray) {
                body = RequestBody.create(NetworkUtils.MEDIA_TYPE_BINARY, field.getByteArray());
            } else {
                body = RequestBody.create(NetworkUtils.MEDIA_TYPE_PLAIN, field.getField());
            }

            builder.addPart(Headers.of("Content-Disposition", field.getMultipartName()), body);
        }

        Request.Builder rBuilder = new Request.Builder();

        BaseHTTPResponse response =
                request(type == RequestMethod.PUT ? rBuilder.put(builder.build()) : rBuilder.post(builder.build()), url,
                        userAgent, authToken, null);
        if (response == null) {
            return null;
        }
        return response;
    }

    private BaseHTTPResponse copyResponse(Response response) {
        BaseHTTPResponse reusableResponse = null;
        try {
            String rawBody = null;
            if (response.body() != null) {
                try {
                    rawBody = response.body().string();
                } catch (IOException ignore) {
                }
                response.body().close();
            }
            reusableResponse =
                    new BaseHTTPResponse().code(response.code()).headers(response.headers().toMultimap()).body(rawBody);
            if (response.body() != null) {
                response.body().close();
            }
        } catch (Exception e) {
            try {
                response.body().close();
            } catch (Exception ignore) {
            }
        }
        return reusableResponse;
    }

    private RequestBody getJSONBody(Object content) {
        String flatContent;
        if (content == null) {
            flatContent = "{}";
        } else if (content instanceof JSONObject) {
            flatContent = content.toString();
        } else if (content instanceof String) {
            flatContent = (String) content;
        } else {
            flatContent = "{}";
        }
        return RequestBody.create(MEDIA_TYPE_JSON, flatContent);
    }

    public enum RequestMethod {
        GET, POST, PUT, DELETE;
    }
}
