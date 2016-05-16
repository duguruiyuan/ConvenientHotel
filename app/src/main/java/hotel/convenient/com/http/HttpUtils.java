package hotel.convenient.com.http;

import android.support.annotation.NonNull;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.Map;

/**
 * Created by cwy on 2016/3/28 17:43
 */
public class HttpUtils {
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpeg");
    public static OkHttpClient mOkHttpClient = new OkHttpClient();
    public static CookieHandler cookieHandler = new CookieManager();
    public static void get(RequestParams params, Callback callback) {
        Request request = new Request.Builder().url(params.getUrl()).build();
        Call call = getOkHttpClient().newCall(request);
        call.enqueue(callback);
    }

    public static void post(RequestParams params, Callback callback) {
        if (params.getPostParams().size() > 0) {
            FormEncodingBuilder builder = new FormEncodingBuilder();
            for (Map.Entry<String, String> entry : params.getPostParams().entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
            RequestBody build = builder.build();
            Request request = new Request.Builder().url(params.getUrl()).post(build).build();
            Call call = getOkHttpClient().newCall(request);
            call.enqueue(callback);
        } else {
            Request request = new Request.Builder().url(params.getUrl()).build();
            Call call = getOkHttpClient().newCall(request);
            call.enqueue(callback);
        }
    }
    public static void postFile(RequestParams params, Callback callback) {
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        for (Map.Entry<String, String> entry : params.getPostParams().entrySet()) {
            builder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        for (RequestParams.FileBody entry : params.getPostFileParams()) {
            builder.addFormDataPart(entry.key, entry.key, RequestBody.create(MEDIA_TYPE_JPG,entry.file));
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(params.getUrl()).post(requestBody).build();
        Call call = getOkHttpClient().newCall(request);
        call.enqueue(callback);
    }

    @NonNull
    private static OkHttpClient getOkHttpClient() {
        mOkHttpClient.setCookieHandler(cookieHandler);
        return mOkHttpClient;
    }
}
