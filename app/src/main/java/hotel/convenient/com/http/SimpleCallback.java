package hotel.convenient.com.http;

import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;

import java.io.IOException;

import hotel.convenient.com.activity.LoginActivity;
import hotel.convenient.com.app.App;
import hotel.convenient.com.utils.LogUtils;
import hotel.convenient.com.utils.ToastUtil;

/**
 * Created by cwy on 2016/3/28 17:16
 */
public abstract class SimpleCallback extends CommonCallback {

    public abstract<T> void simpleSuccess(String url,String result,ResultJson<T> resultJson);
    public  void simpleError(Request request, IOException ex){};
    public static String errorMsg = "当前网络不稳定，请检查你的网络";
    private boolean isShowProgress;

    private static InternetHandler internetHandler = new InternetHandler();
    public static class InternetHandler extends android.os.Handler{
        public static int SUCCESS_STATUS = 0;
        public static int ERROR_STATUS = 1;

        public InternetHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
           
            if(msg.what == SUCCESS_STATUS){
                ResultData resultData =(ResultData) msg.obj;
                resultData.simpleCallback.simpleSuccess(resultData.url,resultData.result,resultData.resultJson);
            } else if (msg.what == ERROR_STATUS) {
                ResultData resultData = (ResultData) msg.obj;
                ToastUtil.showShortToast(errorMsg);
                resultData.simpleCallback.simpleError(resultData.request, resultData.ex);
            } else {
                ToastUtil.showShortToast((String) msg.obj);
            } 

        }

    }

    public SimpleCallback() {
    }
    public SimpleCallback(boolean isShowProgress) {
        this.isShowProgress = isShowProgress;
    }

    @Override
    public void success(final String url, final String result) {
        try{
            if(TextUtils.isEmpty(result)){
                throw new NullPointerException("返回结果为空");
            }
            LogUtils.e(url);
            LogUtils.e(result);
            final ResultJson resultJson = JSONObject.parseObject(result, ResultJson.class);
            if (resultJson.getCode() == CODE_LOGOUT || (resultJson.getMsg() != null && resultJson.getMsg().contains("登录过期"))) {//登录超时
                LoginActivity.httpLoginByPreference(App.getInstanceApp().getApplicationContext());
            } else {
                ResultData resultData = new ResultData();
                resultData.url = url;
                resultData.result = result;
                resultData.simpleCallback = this;
                resultData.resultJson = resultJson;
                handlerMethod(resultData,InternetHandler.SUCCESS_STATUS);
            }
        }catch (Exception e){
            e.printStackTrace();
            Message msg = Message.obtain();
            msg.what = 1111;
            msg.obj = "服务器异常,请更新至最新版本";
            internetHandler.sendMessage(msg);
        }
    }

    private void handlerMethod(ResultData resultData,int status) {
        Message msg = Message.obtain();
        msg.what = status;
        msg.obj = resultData;
        internetHandler.sendMessage(msg);
    }

    @Override
    public void error(final Request request, final IOException ex) {
        ex.printStackTrace();
        ResultData resultData = new ResultData();
        resultData.request = request;
        resultData.ex = ex;
        resultData.simpleCallback = this;
        handlerMethod(resultData,InternetHandler.ERROR_STATUS);
    }
    public static class ResultData{
        SimpleCallback simpleCallback;
        Request request;
        IOException ex;
        String url;
        String result;
        ResultJson resultJson;
    }
}
