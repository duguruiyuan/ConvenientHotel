package hotel.convenient.com.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.OnClick;
import hotel.convenient.com.R;
import hotel.convenient.com.base.BaseActivity;
import hotel.convenient.com.http.HostUrl;
import hotel.convenient.com.http.RequestParams;
import hotel.convenient.com.view.LinearLayoutEditTextView;

/**
 * Created by cwy on 2015/11/27 11:29
 */
public class FindLoginPasswordActivity extends BaseActivity {
    @Bind(R.id.mobile_phone)
    LinearLayoutEditTextView mobile_phone;
    @Bind(R.id.password)
    LinearLayoutEditTextView password;
    @Bind(R.id.verifyPassword)
    LinearLayoutEditTextView verifyPassword;
    @Bind(R.id.vCode)
    LinearLayoutEditTextView vCode;

    @Bind(R.id.register_confirm)
    Button register_confirm;



    private String mobile;

    private String str_password;
    private String str_verifyPassword;
    private String str_vCode;
    @Override
    public void initData(Bundle savedInstanceState) {
        setTitle("找回登录密码");
        showBackPressed();
        vCode.setButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkMobilPhone()) return;
                getCheckCode();
            }
        });
    }

    @Override
    public int setLayoutView() {
        return R.layout.find_login_password_activity;
    }

    @OnClick({R.id.register_confirm})
     void onButtonClick(View v){
        switch (v.getId()){
            case R.id.register_confirm:
                if(!checkParams()){
                    httpRegister();
                }

                break;

        }
    }

    /**
     * 向服务器发送注册请求
     */
    private void httpRegister() {
        RequestParams params=new RequestParams(HostUrl.HOST+ HostUrl.URL_RESET_PASSWORD);
    }



    private boolean checkParams() {
        str_password=password.getText().toString();
        str_verifyPassword=verifyPassword.getText().toString();
        str_vCode=vCode.getText().toString();
        if (!checkPassword(str_password, str_verifyPassword)) return true;

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
        RequestParams params = new RequestParams(HostUrl.HOST+ HostUrl.URL_CHECK_PHONE);
    }
}
