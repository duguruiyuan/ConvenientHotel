package hotel.convenient.com.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import hotel.convenient.com.base.BaseActivity;
import hotel.convenient.com.R;
import hotel.convenient.com.http.HostUrl;
import hotel.convenient.com.http.HttpUtils;
import hotel.convenient.com.http.ResultJson;
import hotel.convenient.com.http.SimpleCallback;
import hotel.convenient.com.utils.PreferenceUtils;
import hotel.convenient.com.view.LinearLayoutEditTextView;

/**
 * 登录的activity
 * Created by Gyb on 2015/11/30 10:00
 */
@ContentView(R.layout.login_activity)
public class LoginActivity extends BaseActivity {
    @ViewInject(R.id.login_confirm)
    private Button login_confirm;
    @ViewInject(R.id.login_account)
    private LinearLayoutEditTextView login_account;
    @ViewInject(R.id.login_password)
    private LinearLayoutEditTextView login_password;
    
    private String username;
    private String password;
    
    private String intentData;

    public static final String STATE_LOGOUT = "STATE_LOGOUT";
    @Override
    public void initData(Bundle savedInstanceState) {
        setTitle("登录");
        showBackPressed();
        intentData = getIntent().getStringExtra(STATE_LOGOUT);
    }
    @Event({R.id.login_confirm,R.id.login_find_password,R.id.login_register})
    private void onConfirmClick(View view){
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
        username = login_account.getText();
        password = login_password.getText();
        if (checkInput()) return;
        RequestParams params = new RequestParams(HostUrl.HOST + HostUrl.URL_LOGIN);
        params.addBodyParameter("phone",username);
        params.addBodyParameter("password",password);
        HttpUtils.post(params, new SimpleCallback(this) {
            @Override
            public <T> void simpleSuccess(String url, String result, ResultJson<T> resultJson) {
                if (resultJson.getCode() == CODE_SUCCESS) {
                    PreferenceUtils.setLoginFlag(LoginActivity.this,username,password);
                    showShortToast("登录成功!");
                    skipActivity(MainActivity.class);
                } else {
                    showShortToast(resultJson.getMsg());
                }
            }
        });
    }
    //检测输入
    public boolean checkInput() {
        if(TextUtils.isEmpty(username)){
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
