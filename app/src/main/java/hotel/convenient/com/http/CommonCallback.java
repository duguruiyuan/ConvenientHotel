package hotel.convenient.com.http;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.app.RequestTracker;
import org.xutils.http.request.UriRequest;

import java.io.EOFException;
import java.net.SocketException;

import hotel.convenient.com.utils.LogUtils;

/**
 * Created by Gyb on 2015/11/30 15:38
 */
public abstract class CommonCallback implements RequestTracker,Callback.ProgressCallback<String> {
    public static final int CODE_SUCCESS = 0;   //成功
    public static final int CODE_LOGOUT  = 9;//登录超时
    @Override
    public void onWaiting() {

    }

    @Override
    public void onStarted() {
        start();
    }

    @Override
    public void onLoading(long total, long current, boolean isDownloading) {

    }
//    用于日志记录
    @Override
    public void onWaiting(UriRequest request) {
//        LogUtils.e(request.getRequestUri() + ": 正在等待中...");
    }

    @Override
    public void onStart(UriRequest request) {
//        LogUtils.e(request.getRequestUri() + "开始");
    }

    @Override
    public void onCache(UriRequest request) {
        LogUtils.e(request.getRequestUri());
    }

    @Override
    public void onSuccess(UriRequest request) {
    }

    @Override
    public void onCancelled(UriRequest request) {
        LogUtils.defaultLog(request.getRequestUri());
        LogUtils.v("");
    }

    @Override
    public void onError(UriRequest request, Throwable ex, boolean isCallbackError) {
        
        if (ex instanceof HttpException) { // 网络错误
            LogUtils.defaultLog("网络访问失败");
            ex.printStackTrace();
        } else if(ex instanceof EOFException){ // 对4.4以下手机 post请求经常性失败
//            ex.printStackTrace();
            // 碰到后  直接重新请求
            HttpUtils.post(request.getParams(),this);
        }else if(ex instanceof SocketException){
            // 碰到后  直接重新请求
            HttpUtils.post(request.getParams(),this);
        } else{ // 其他错误
            LogUtils.defaultLog("服务器未知错误" + ex);
            ex.printStackTrace();
        }
        error(request, ex);
    }

    @Override
    public void onFinished(UriRequest request) {
        LogUtils.e(request.getRequestUri()+" 访问已完成!");
        finish(request);
    }

    public abstract void start();
    public abstract void success(String url,String result);
    public abstract void error(UriRequest request, Throwable ex);
    public abstract void finish(UriRequest url);
    //CommonCallback   
    @Override
    public void onSuccess(String result) {
        LogUtils.e(result);
        success("",result);
    }
    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        
    }

    @Override
    public void onCancelled(CancelledException cex) {
    }
    @Override
    public void onFinished() {
    }
}
