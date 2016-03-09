package hotel.convenient.com.http;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;

import org.xutils.http.request.UriRequest;

import java.io.EOFException;

import hotel.convenient.com.activity.LoginActivity;
import hotel.convenient.com.base.BaseActivity;

/**
 * 简单封装版回调接口 处理了捕获异常 开关进度条的常用工作
 * 可以让你专注处理返回的信息
 * Created by Gyb on 2015/12/1 15:15
 */
public abstract class SimpleCallback extends CommonCallback {
    protected BaseActivity baseActivity;
    private String errorMsg = "服务器未知的错误";
    private boolean isShowProgress = true;
    private UriRequest sendUrl;
    public SimpleCallback(BaseActivity baseActivity){
        this(baseActivity,"",true);
    }
    public SimpleCallback(BaseActivity baseActivity, boolean isShowProgress){
        this(baseActivity,"",isShowProgress);
    }
    public SimpleCallback(BaseActivity baseActivity, String errorMsg){
        this(baseActivity,errorMsg,true);
    }
    public SimpleCallback(BaseActivity baseActivity, String errorMsg, boolean isShowProgress){
        this.baseActivity = baseActivity;
        setErrorMsg(errorMsg);
        this.isShowProgress = isShowProgress;
    }
    @Override
    public void start() {
        if (baseActivity==null){
            return;
        }
        if(isShowProgress)
        baseActivity.showProgress();
    }

    @Override
    public void success(String url, String result) {
        try {
            if(TextUtils.isEmpty(result)){
                throw new NullPointerException("返回结果为空");
            }
            ResultJson resultJson = JSONObject.parseObject(result, ResultJson.class);
            if(resultJson.getCode()==CODE_LOGOUT||(resultJson.getMsg()!=null&&resultJson.getMsg().contains("登录过期"))){//登录超时
                LoginActivity.httpLoginByPreference(baseActivity);
            }else {
                simpleSuccess(url, result, resultJson);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void error(UriRequest request, Throwable ex) {
        if (baseActivity==null){
            return;
        }
        if(ex instanceof EOFException){
        }else {
            baseActivity.showShortToast(errorMsg);
        }
        if(isShowProgress)
        baseActivity.stopProgress();
    }

    @Override
    public void finish(UriRequest url) {
        if (baseActivity==null){
            return;
        }
        if(isShowProgress)
            baseActivity.stopProgress();
        sendUrl = url;
    }
    public abstract<T> void simpleSuccess(String url,String result,ResultJson<T> resultJson);
    
    public void setErrorMsg(String msg){
        if(!TextUtils.isEmpty(msg)){
            errorMsg = msg;
        }
    }
}
