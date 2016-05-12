package hotel.convenient.com.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;


import com.alibaba.fastjson.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;
import hotel.convenient.com.R;
import hotel.convenient.com.activity.AboutActivity;
import hotel.convenient.com.activity.InformationListActivity;
import hotel.convenient.com.base.BaseFragment;
import hotel.convenient.com.http.HostUrl;
import hotel.convenient.com.http.HttpUtils;
import hotel.convenient.com.http.RequestParams;
import hotel.convenient.com.http.ResultJson;
import hotel.convenient.com.http.SimpleCallback;
import hotel.convenient.com.view.AlertDialog;
import hotel.convenient.com.view.LinearLayoutMenu;

/**
 * Created by Gyb on 2015/11/20.
 */
public class MoreFragment extends BaseFragment{
    @Bind(R.id.miv_news)
     LinearLayoutMenu miv_news;
    @Bind(R.id.miv_innermess)
     LinearLayoutMenu miv_inner_letter;
    @Bind(R.id.tv_phone)
     LinearLayoutMenu rl_server_phone;
    @Bind(R.id.miv_about)
     LinearLayoutMenu miv_about;

    @Override
    public int setLayoutView() {
        return R.layout.more_fragment;
    }

    @Override
    public void initData(View view, Bundle savedInstanceState) {
        
        HttpUtils.get(new RequestParams(HostUrl.HOST + HostUrl.URL_GET_NOT_READ_MESSAGE), new SimpleCallback() {
            @Override
            public <T> void simpleSuccess(String url, String result, ResultJson<T> resultJson) {
                if(resultJson.isSuccess()){
                    miv_inner_letter.setNotReadMsgCount(JSONObject.parseObject(result).getString("data"));
                }
            }
        });
    }
    @OnClick({R.id.miv_innermess,R.id.miv_news,R.id.tv_phone,R.id.miv_about})
     void clickItem(View view){
        switch (view.getId()){
            case R.id.miv_news:
//                mBaseActivity.skipActivity( AnnounceListActivity.class,false);
                break;
            case R.id.miv_innermess:
                HttpUtils.post(new RequestParams(HostUrl.HOST + HostUrl.URL_SET_STATUS_MESSAGE_TO_READ), new SimpleCallback() {
                    @Override
                    public <T> void simpleSuccess(String url, String result, ResultJson<T> resultJson) {
                        if(resultJson.isSuccess()){
                            miv_inner_letter.setNotReadMsgCount(0+"");
                            mBaseActivity.skipActivity(InformationListActivity.class,false);
                        }
                    }
                });
               
                break;
            case R.id.tv_phone:
                final String phone = rl_server_phone.getMsg();
                new AlertDialog(getActivity()).builder().setTitle(phone).setPositiveButton("拨打",getActivity().getResources().getColor(R.color.colorPrimary),new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     Intent intents = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ phone.replaceAll("-","")));
                        startActivity(intents);
                    }
                }).setNegativeButton("取消", getActivity().getResources().getColor(R.color.black_text_color), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
                break;
            case R.id.miv_about:
                mBaseActivity.skipActivity( AboutActivity.class,false);
                break;
        }
    }


}
