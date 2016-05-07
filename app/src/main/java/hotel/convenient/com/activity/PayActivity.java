package hotel.convenient.com.activity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;
import hotel.convenient.com.R;
import hotel.convenient.com.base.BaseActivity;
import hotel.convenient.com.domain.ChooseDealerInfo;
import hotel.convenient.com.domain.RoomOrder;
import hotel.convenient.com.http.HostUrl;
import hotel.convenient.com.http.HttpUtils;
import hotel.convenient.com.http.RequestParams;
import hotel.convenient.com.http.ResultJson;
import hotel.convenient.com.http.SimpleCallback;

/**
 * Created by Gyb on 2016/5/7 22:29
 */
public class PayActivity extends BaseActivity {
    @Bind(R.id.msg)
    TextView msg;
    @Bind(R.id.back_main)
    LinearLayout backMain;
    String msgStr = "成功";
    ChooseDealerInfo dealerInfo;
    @Override
    public void initData(Bundle savedInstanceState) {
        setTitle("前往付款");
        showBackPressed();
        String data = getIntent().getStringExtra("data");
        if(!isEmpty(data)){
            msgStr = data;
        }
        dealerInfo = (ChooseDealerInfo) getIntent().getSerializableExtra("dealerInfo");
        if(dealerInfo==null){
            return;
        }
        msg.setText(msgStr);
    }
    private void sendOrderInfoToHttp() {
        RequestParams requestParams = new RequestParams(HostUrl.HOST+HostUrl.URL_CONFIRM_ORDER);
        int day = (int) ((dealerInfo.getEndCalendar().getTimeInMillis()-dealerInfo.getStartCalendar().getTimeInMillis())/(1000*60*60*24))+1;
        requestParams.addBodyParameter("json", JSONObject.toJSONString(new RoomOrder(
                dealerInfo.getPublish().getId(),dealerInfo.getStartCalendar().getTimeInMillis(),
                dealerInfo.getEndCalendar().getTimeInMillis(),day)));
        HttpUtils.post(requestParams, new SimpleCallback() {
            @Override
            public <T> void simpleSuccess(String url, String result, ResultJson<T> resultJson) {
                if (resultJson.getCode() == CODE_SUCCESS) {
                    stopProgress();
                    skipActivity(ResultActivity.class,true,"data",resultJson.getMsg());
                } else {
                    showShortToast(resultJson.getMsg());
                }
            }
        });
    }
    @Override
    public int setLayoutView() {
        return R.layout.success_activity;
    }


    @OnClick(R.id.back_main)
    public void onClick() {
        showProgress();
        sendOrderInfoToHttp();
    }
}
