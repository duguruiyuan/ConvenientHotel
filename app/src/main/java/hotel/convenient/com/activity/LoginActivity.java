package hotel.convenient.com.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;
import hotel.convenient.com.R;
import hotel.convenient.com.base.BaseActivity;
import hotel.convenient.com.domain.Dealer;
import hotel.convenient.com.http.HostUrl;
import hotel.convenient.com.http.HttpUtils;
import hotel.convenient.com.http.RequestParams;
import hotel.convenient.com.http.ResultJson;
import hotel.convenient.com.http.SimpleCallBack;
import hotel.convenient.com.utils.PreferenceUtils;
import hotel.convenient.com.view.LinearLayoutEditTextView;

/**
 * 登录的activity
 * Created by Gyb on 2015/11/30 10:00
 */
public class LoginActivity extends BaseActivity {
    @Bind(R.id.login_confirm)
     Button login_confirm;
    @Bind(R.id.login_account)
     LinearLayoutEditTextView login_account;
    @Bind(R.id.login_password)
     LinearLayoutEditTextView login_password;
    
    private String phone;
    private String password;
    
    private String intentData;

    public static final String STATE_LOGOUT = "STATE_LOGOUT";
    @Override
    public void initData(Bundle savedInstanceState) {
        setTitle("登录");
        showBackPressed();
        intentData = getIntent().getStringExtra(STATE_LOGOUT);
    }

    @Override
    public int setLayoutView() {
        return R.layout.login_activity;
    }

    @OnClick({R.id.login_confirm,R.id.login_find_password,R.id.login_register})
     void onConfirmClick(View view){
        switch (view.getId()){
            case R.id.login_confirm:
                httpLogin();
                break;
            case R.id.login_find_password:
                skipActivity(FindLoginPasswordActivity.class,false);
                break;
            case R.id.login_register:
                skipActivity(RegisterActivity.class,false);
                break;
        }
    }
    /**
     * 访问网络以及处理返回结果
     */
    public void httpLogin(){
        phone = login_account.getText();
        password = login_password.getText();
        if (checkInput()) return;
        RequestParams params = new RequestParams(HostUrl.HOST + HostUrl.URL_LOGIN);
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("password",password);
        HttpUtils.post(params, new SimpleCallBack(this) {
            @Override
            public <T> void simpleSuccess(String url, String result, ResultJson<T> resultJson) {
                if (resultJson.getCode() == CODE_SUCCESS) {
                    Dealer data = JSONObject.parseObject(JSONObject.parseObject(result).getJSONObject("data").toString(), Dealer.class);
                    PreferenceUtils.setLoginFlag(LoginActivity.this, data.getPhonenumber(),data.getNickname(),password);
                    showShortToast("登录成功!");
                    skipActivity(MainActivity.class,true,MainActivity.FLAG_SKIP,0);
                } else {
                    showShortToast(resultJson.getMsg());
                }
            }
        });
    }
    /**
     * 访问网络以及处理返回结果  静态方法  供其他类调用
     */
    public static void httpLoginByPreference(final BaseActivity baseActivity){
        String phone = PreferenceUtils.getPhone(baseActivity);
        final String password = PreferenceUtils.getLoginPassword(baseActivity);
        
        RequestParams params = new RequestParams(HostUrl.HOST + HostUrl.URL_LOGIN);
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("password",password);
        HttpUtils.post(params, new SimpleCallBack(baseActivity) {
            @Override
            public <T> void simpleSuccess(String url, String result, ResultJson<T> resultJson) {
                if (resultJson.getCode() == CODE_SUCCESS) {
                    Dealer data = JSONObject.parseObject(JSONObject.parseObject(result).getJSONObject("data").toString(), Dealer.class);
                    PreferenceUtils.setLoginFlag(baseActivity, data.getPhonenumber(),data.getNickname(),password);
                } else {
                    baseActivity.showShortToast(resultJson.getMsg());
                    baseActivity.showShortToast("登录超时");
                    PreferenceUtils.removeLoginFlag(baseActivity);
                    baseActivity.skipActivity(LoginActivity.class,false, STATE_LOGOUT, STATE_LOGOUT);
                }
            }
        });
    }
    //检测输入
    public boolean checkInput() {
        if(TextUtils.isEmpty(phone)){
            showShortToast("用户名不能为空");
            return true;
        }
        if (TextUtils.isEmpty(password)){
            showShortToast(" 密码不能为空");
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        skipActivity(MainActivity.class);
    }
}
