package hotel.convenient.com.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;
import hotel.convenient.com.R;
import hotel.convenient.com.base.BaseActivity;
import hotel.convenient.com.http.HostUrl;
import hotel.convenient.com.http.HttpUtils;
import hotel.convenient.com.http.RequestParams;
import hotel.convenient.com.http.ResultJson;
import hotel.convenient.com.http.SimpleCallback;
import hotel.convenient.com.utils.ImageUtils;


/**
 * Created by Gyb on 2016/1/6 18:02
 */
public class AccountBankCardActivity extends BaseActivity {
    @Bind(R.id.bank_name)
    TextView bank_name;
    @Bind(R.id.bank_user_name)
    TextView bank_username;
    @Bind(R.id.bank_number)
    TextView bank_number;
    @Bind(R.id.bank_icon)
    ImageView bank_icon;
    @Bind(R.id.ll_normal_bank_card)
    LinearLayout ll_normal_bank_card;
    @Bind(R.id.ll_bank_card)
    LinearLayout ll_bank_card;
    private SimpleCallback simpleCallbackInfo = new SimpleCallback(false) {
        @Override
        public <T> void simpleSuccess(String url, String result, ResultJson<T> resultJson) {
            if (resultJson.getCode() == CODE_SUCCESS) {
                skipActivity(BindingBankCardActivity.class,false);
            } else {
                showShortToast(resultJson.getMsg());
            }
        }
    };
    private SimpleCallback simpleCallback = new SimpleCallback(false) {
        @Override
        public <T> void simpleSuccess(String url, String result, ResultJson<T> resultJson) {
            if(resultJson.getCode()==CODE_SUCCESS){
                hideNormalBank(true);
                JSONObject jsonObject = JSONObject.parseObject(result);
                JSONObject data = jsonObject.getJSONObject("data");
                bank_number.setText(data.getString("card"));
                bank_name.setText(data.getString("bank_name"));
                bank_username.setText(data.getString("username"));
                ImageUtils.setImage(bank_icon,"", R.drawable.card);
            }else {
                hideNormalBank(false);
                showShortToast(resultJson.getMsg());
            }
        }
    };
    @Override
    public int setLayoutView() {
        return R.layout.my_bank_card;
    }
    public void hideNormalBank(boolean isHideNormal) {
        if (isHideNormal) {
            ll_bank_card.setVisibility(View.VISIBLE);
            ll_normal_bank_card.setVisibility(View.GONE);
        } else {
            ll_bank_card.setVisibility(View.GONE);
            ll_normal_bank_card.setVisibility(View.VISIBLE);
        } 
    }
    

    @Override
    public void initData(Bundle savedInstanceState) {
        setTitle("我的银行卡");
        showBackPressed();
        httpByBankCard();
    }

    

    public void httpByBankCard(){
        RequestParams params = new RequestParams(HostUrl.HOST+HostUrl.GET_BAND_DETAIL_INFO);
        HttpUtils.get(params,simpleCallback);
    }
    public void getBankInfoByHttp(){
        RequestParams params = new RequestParams(HostUrl.HOST+HostUrl.URL_GET_BIND_BANK_INFO);
        HttpUtils.get(params, simpleCallbackInfo);
    }
    @OnClick(R.id.ll_normal_bank_card)
     void setBindBankOnclick(View view){
        switch (view.getId()) {
            case R.id.ll_normal_bank_card:
                getBankInfoByHttp();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        httpByBankCard();
    }
}
