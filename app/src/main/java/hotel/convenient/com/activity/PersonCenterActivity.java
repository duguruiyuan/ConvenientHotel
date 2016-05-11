package hotel.convenient.com.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import hotel.convenient.com.utils.ImageUtils;
import hotel.convenient.com.utils.LogUtils;
import hotel.convenient.com.utils.PreferenceUtils;
import hotel.convenient.com.view.CircleImageView;
import hotel.convenient.com.view.LinearLayoutMenu;

/**
 * 登录的activity
 * Created by Gyb on 2015/11/30 10:00
 */
public class PersonCenterActivity extends BaseActivity {
    @Bind(R.id.ll_telephone)
    LinearLayoutMenu ll_telephone;
    @Bind(R.id.ll_bank_card)
     LinearLayoutMenu ll_bank_card;
    @Bind(R.id.ll_id)
     LinearLayoutMenu ll_id;
    @Bind(R.id.ll_real_name)
     LinearLayoutMenu ll_real_name;

    @Bind(R.id.tv_account_name)
     TextView tv_account_name;
    @Bind(R.id.tv_add_time)
     TextView tv_add_time;
    @Bind(R.id.iv_header)
    CircleImageView iv_header;
    @Bind(R.id.ll_head_image)
    RelativeLayout ll_head_image;
    private Dealer personData;

    @Override
    public void initData(Bundle savedInstanceState) {
        setTitle("个人资料");
        showBackPressed();//显示返回按钮
        RequestParams rq=new RequestParams(HostUrl.HOST+HostUrl.URL_GET_USER_INFO);
        HttpUtils.get(rq, new SimpleCallback() {
            @Override
            public <T> void simpleSuccess(String url, String result, ResultJson<T> resultJson) {
               JSONObject jsonObject= (JSONObject) JSONObject.parse(result);
               if(jsonObject.getInteger("code")==0) {
                    JSONObject dataJson = jsonObject.getJSONObject("data");
                    personData = JSONObject.parseObject(dataJson.toJSONString(), Dealer.class);
                    setData(personData);
                }
            }
        });
    }

    @Override
    public int setLayoutView() {
        return R.layout.person_center;
    }


    private void setData(Dealer personData){
        tv_account_name.setText(personData.getNickname());
        tv_add_time.setText(personData.getRegister_time());
        ll_telephone.setMsg(personData.getPhonenumber());
        String url = HostUrl.HOST + "/" + personData.getImg_dir() + personData.getHead_image();
        LogUtils.e("url:"+url);
        ImageUtils.setImage(iv_header, url,R.drawable.mei_zi);
        if(isEmpty(personData.getName())){
            ll_id.setMsg("未添加");
            ll_real_name.setMsg("无");
            ll_real_name.setArrowVisiable(false);
            ll_id.setArrowVisiable(true);
            ll_id.setClickable(true);
        }else {
            ll_id.setMsg(personData.getId_card());
            ll_real_name.setMsg(personData.getName());
            ll_real_name.setArrowVisiable(false);
            ll_id.setArrowVisiable(false);
            ll_id.setClickable(false);
        }

        if(isEmpty(personData.getBank_card())){
            ll_bank_card.setMsg("未添加");
            ll_bank_card.setArrowVisiable(true);
            ll_bank_card.setClickable(true);
        }else {
            ll_bank_card.setMsg(personData.getBank_card());
            ll_bank_card.setArrowVisiable(false);
            ll_bank_card.setClickable(false);
        }

    }
    private void logout(){
        RequestParams params = new RequestParams(HostUrl.HOST+HostUrl.URL_LOGOUT);
        HttpUtils.get(params, new SimpleCallback() {
            @Override
            public <T> void simpleSuccess(String url, String result, ResultJson<T> resultJson) {
                if (resultJson.getCode() == CODE_SUCCESS) {
                    PreferenceUtils.removeLoginFlag(PersonCenterActivity.this);
                    skipActivity(MainActivity.class);
                }
                showShortToast(resultJson.getMsg());
            }
        });
    }
    @OnClick({R.id.bt_logout,R.id.ll_bank_card,R.id.ll_id,R.id.ll_real_name,R.id.ll_head_image})
     void onConfirmClick(View view){
        if(personData==null){
            showShortToast("账号信息获取中,请稍后...");
            return;
        }
        switch (view.getId()){
            case R.id.bt_logout:
                showAlertDialog("是否退出当前账号", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                });
                break;
            case R.id.ll_bank_card:
                skipActivity(AccountBankCardActivity.class,false);
                break;
            case R.id.ll_id:
            case R.id.ll_real_name:
                skipActivity(CheckRealNameActivity.class, false);
                break;
            case R.id.ll_head_image:
                skipActivity(CheckRealNameActivity.class, false);
                break;
        }
    }
}
