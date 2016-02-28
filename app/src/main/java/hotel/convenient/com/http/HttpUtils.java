package hotel.convenient.com.http;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 网络访问工具类
 * Created by Gyb on 2015/11/30 11:38
 */
public class HttpUtils {
    public static Callback.Cancelable get(RequestParams params, Callback.CommonCallback commonCallback) {
        //可以给params添加固定的请求头  表示客户端类型  3 android
        return x.http().get(params, commonCallback);
    }

    public static Callback.Cancelable post(RequestParams params, Callback.CommonCallback commonCallback) {
        //可以给params添加固定的请求头  表示客户端类型  3 android
        return x.http().post(params, commonCallback);

    }
}