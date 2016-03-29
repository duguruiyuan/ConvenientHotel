package hotel.convenient.com.http;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Gyb on 2015/11/30 15:38
 */
public abstract class CommonCallback implements Callback{
    public static final int CODE_SUCCESS = 0;   //成功
    public static final int CODE_LOGOUT  = 9;//登录超时  
    
    public abstract void success(String url,String result);
    public abstract void error(Request request,IOException ex);
    @Override
    public void onFailure(Request request, IOException e) {
        e.printStackTrace();
        error(request,e);
    }

    @Override
    public void onResponse(Response response) throws IOException {
        success(response.request().urlString(),response.body().string());
    }
}
