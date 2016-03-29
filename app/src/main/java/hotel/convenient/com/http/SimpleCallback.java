package hotel.convenient.com.http;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;

import java.io.IOException;

import hotel.convenient.com.activity.LoginActivity;
import hotel.convenient.com.base.BaseActivity;
import hotel.convenient.com.utils.LogUtils;

/**
 * Created by Gyb on 2016/3/28 17:16
 */
public abstract class SimpleCallBack extends CommonCallback {

    public abstract<T> void simpleSuccess(String url,String result,ResultJson<T> resultJson);
    protected BaseActivity mBaseActivity;

    public SimpleCallBack(BaseActivity activity) {
        this.mBaseActivity = activity;
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
                LoginActivity.httpLoginByPreference(mBaseActivity);
            } else {
                mBaseActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        simpleSuccess(url,result,resultJson);
                    }
                });
            } 
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void error(Request request, IOException ex) {
        ex.printStackTrace();
    }
}
