package hotel.convenient.com.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

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
import hotel.convenient.com.view.LinearLayoutEditTextView;

/**
 * Created by Gyb on 2015/11/27 11:29
 */
@ContentView(R.layout.register_activity)
public class RegisterActivity extends BaseActivity {
    @ViewInject(R.id.mobile_phone)
    private LinearLayoutEditTextView mobile_phone;
    @ViewInject(R.id.password)
    private LinearLayoutEditTextView password;
    @ViewInject(R.id.verifyPassword)
    private LinearLayoutEditTextView verifyPassword;
    @ViewInject(R.id.vCode)
    private LinearLayoutEditTextView vCode;
    @ViewInject(R.id.username)
    private LinearLayoutEditTextView username;
    @ViewInject(R.id.register_confirm)
    private Button register_confirm;
    @ViewInject(R.id.register_checkbox)
    private CheckBox register_checkbox;
    @ViewInject(R.id.register_upload_url)
    private TextView register_upload_url;
    
    private String mobile;
    private String str_password = "";
    private String str_verifyPassword = "";
    private String str_username = "";
    private String str_vCode = "";
    private SimpleCallback simpleCallback = new SimpleCallback(this) {
            @Override
            public <T> void simpleSuccess(String url, String result, ResultJson<T> resultJson) {
                if (resultJson.getCode() == CODE_SUCCESS) {
                    showShortToast(resultJson.getMsg());
                    RegisterActivity.this.finish();
                } else {
                    showShortToast(resultJson.getMsg());
                }
            }
        };
    @Override
    public void initData(Bundle savedInstanceState) {
        setTitle("用户注册");
        showBackPressed();
        String checkbox_text = "<a href=''>《注册协议》</a>";
        register_upload_url.setText(Html.fromHtml(checkbox_text));
        vCode.setButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkMobilPhone()) return;
                getCheckCode();
            }
        });
    }
    @Event({R.id.register_confirm,R.id.register_upload_url})
    private void onButtonClick(View v){
        switch (v.getId()){
            case R.id.register_confirm:
                if(!register_checkbox.isChecked()){
                    showShortToast("同意注册协议后才能注册");
                    return ;
                }
                if(!setRegisterParams()){
                    return;
                }
                httpRegister();
                break;
            case R.id.register_upload_url:
                break;
        }
    }
    private boolean setRegisterParams() {
        mobile = mobile_phone.getText();
        str_password = password.getText();
        str_verifyPassword = verifyPassword.getText();
        str_username = username.getText();
        str_vCode = vCode.getText();

        if (checkParams(str_password, str_verifyPassword, str_username, str_vCode)) return false;
        return true;
    }
    /**
     * 向服务器发送注册请求
     */
    private void httpRegister() {
        RequestParams params = new RequestParams(HostUrl.HOST+ HostUrl.URL_REGISTER);
        params.addBodyParameter("phone",mobile );
        params.addBodyParameter("nickname",str_username );
        params.addBodyParameter("password",str_password );
        params.addBodyParameter("code",str_vCode );
        HttpUtils.post(params, simpleCallback);
    }

    private boolean checkParams(String str_password, String str_verifyPassword, String str_username, String str_vCode) {
        if(!checkMobilPhone()){
            return true;
        }
        if (!checkPassword(str_password, str_verifyPassword)) return true;
        if (isEmpty(str_username)){
            showShortToast("用户名不能为空!");
            return true;
        }
        if (isEmpty(str_vCode)){
            showShortToast("验证码不能为空!");
            return true;
        }
        return false;
    }

    private boolean checkPassword(String str_password, String str_verifyPassword) {
        if (isEmpty(str_password)){
            showShortToast("密码不能为空!");
            return false;
        }
        if (!str_password.equals(str_verifyPassword)){
            showShortToast("两次输入的密码不一致");
            return false;
        }
        return true;
    }

    /**
     * 检查电话号码
     */
    private boolean checkMobilPhone() {
        mobile = mobile_phone.getText().toString().trim();
         if(isEmpty(mobile)){
             showShortToast("电话号码不能为空!");
             return false;
         }
        if(mobile.length()!=11){
            showShortToast("电话号码格式错误!");
            return false;
        }
        return true;
    }

    /**
     * 获取验证码
     */
    public void getCheckCode() {
        RequestParams params = new RequestParams(HostUrl.HOST+ HostUrl.URL_GET_CODE);
        params.addQueryStringParameter("phone", mobile);
        HttpUtils.get(params, new SimpleCallback(this, "验证码获取失败") {
            @Override
            public <T> void simpleSuccess(String url, String result, ResultJson<T> resultJson) {
                if (resultJson.getCode() == CODE_SUCCESS) {
                    showShortToast("验证码获取成功");
                    vCode.startChangeButtonName("重新获取", 30);
                } else {
                    showShortToast(resultJson.getMsg());
                }
            }
        });
    }
}
