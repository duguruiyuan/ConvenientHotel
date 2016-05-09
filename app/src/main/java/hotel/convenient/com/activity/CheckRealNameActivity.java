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
import hotel.convenient.com.http.SimpleCallback;
import hotel.convenient.com.utils.PreferenceUtils;
import hotel.convenient.com.view.LinearLayoutEditTextView;

/**
 * Created by Gyb on 2015/12/10 15:22
 */
public class CheckRealNameActivity extends BaseActivity{
    @Bind(R.id.id_card)
    LinearLayoutEditTextView id_card;
    @Bind(R.id.real_name)
     LinearLayoutEditTextView real_name;
    @Bind(R.id.card_confirm)
     Button card_confirm;
    private String idcard;
    private String realname;
    
    private SimpleCallback commonCallback = new SimpleCallback() {
        @Override
        public <T> void simpleSuccess(String url, String result, ResultJson<T> resultJson) {
            if(resultJson.isSuccess()){
                Dealer data = JSONObject.parseObject(JSONObject.parseObject(result).getJSONObject("data").toString(), Dealer.class);
                PreferenceUtils.setIDCard(CheckRealNameActivity.this,data.getNickname(),data.getId_card());
                finish();
            }
            showShortToast(resultJson.getMsg());
        }
    };

    @Override
    public void initData(Bundle savedInstanceState) {
        setTitle("实名认证");
        showBackPressed();
        if (isEmpty(PreferenceUtils.getIdCard(this))) {
            showShortToast("您还未实名认证,请填写身份信息");
        } else {
            showShortToast("您已通过实名认证!");
            id_card.setInputText(PreferenceUtils.getIdCard(this));
            real_name.setInputText(PreferenceUtils.getNickname(this));
            card_confirm.setVisibility(View.GONE);
        } 
    }

    @Override
    public int setLayoutView() {
        return R.layout.check_realname_fragment;
    }

    
    @OnClick({R.id.card_confirm})
     void onButtonClick(View view){
        switch (view.getId()) {
            case R.id.card_confirm:
                idcard = id_card.getText();
                realname = real_name.getText();
                if (checkEditInput(idcard,"身份证号码不能为空")) return;
                if (checkEditInput(realname, "姓名不能为空")) return;
                httpCheckIDCard();
                break;
           
        }
    }

    private void httpCheckIDCard() {
        RequestParams params = new RequestParams(HostUrl.HOST+HostUrl.URL_CHECK_NAME);
        params.addBodyParameter("name",realname);
        params.addBodyParameter("idCard",idcard);
        HttpUtils.post(params,commonCallback);
    }

    private boolean checkEditInput(String input, String info) {
        if (TextUtils.isEmpty(input)) {
            showShortToast(info);
            return true;
        }
        return false;
    }
}
