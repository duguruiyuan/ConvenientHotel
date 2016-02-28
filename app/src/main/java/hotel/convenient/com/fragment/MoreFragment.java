package hotel.convenient.com.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import hotel.convenient.com.R;
import hotel.convenient.com.base.BaseFragment;
import hotel.convenient.com.view.AlertDialog;
import hotel.convenient.com.view.LinearLayoutMenu;

/**
 * Created by Gyb on 2015/11/20.
 */
@ContentView(R.layout.more_fragment)
public class MoreFragment extends BaseFragment{
    @ViewInject(R.id.miv_news)
    private LinearLayoutMenu miv_news;
    @ViewInject(R.id.miv_innermess)
    private LinearLayoutMenu miv_inner_letter;
    @ViewInject(R.id.tv_phone)
    private LinearLayoutMenu rl_server_phone;
    @ViewInject(R.id.miv_about)
    private LinearLayoutMenu miv_about;

    @Override
    public void initData(View view, Bundle savedInstanceState) {
        
    }
    @Event({R.id.miv_innermess,R.id.miv_news,R.id.tv_phone,R.id.miv_about})
    private void clickItem(View view){
        switch (view.getId()){
            case R.id.miv_news:
//                mBaseActivity.skipActivity( AnnounceListActivity.class,false);
                break;
            case R.id.miv_innermess:
//                mBaseActivity.skipActivity(InnerLetterListActivity.class,false);
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
//                mBaseActivity.skipActivity( AboutActivity.class,false);false
                break;
        }
    }


}
