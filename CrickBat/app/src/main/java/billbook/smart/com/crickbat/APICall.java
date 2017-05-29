package billbook.smart.com.crickbat;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Jay-Andriod on 05-Apr-17.
 */

public class APICall {


    public static final MediaType JSON = MediaType.parse("text/plain");

    static OkHttpClient client = new OkHttpClient();
    public static String post(String url, String json ,String method) throws IOException {
        if(method.equalsIgnoreCase("post")) {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            }
        }else if(method.equalsIgnoreCase("get")){
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            }
        }
    return null;}
}
