package demo.lowcode.common.connect;

import okhttp3.*;

import java.io.IOException;
import java.util.Objects;

public class HTTPService {
    private final OkHttpClient client = new OkHttpClient();

    // 发送GET请求
    public String sendGetRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return Objects.requireNonNull(response.body()).string();
        }
    }

    // 发送POST请求
    public String sendPostRequest(String url, String jsonInput) throws IOException {
        RequestBody body = RequestBody.create(
                jsonInput, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return Objects.requireNonNull(response.body()).string();
        }
    }
}
