package hotel.convenient.com.activity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import hotel.convenient.com.R;
import hotel.convenient.com.base.BaseActivity;

/**
 * Created by cwy on 2016/5/7 23:47
 */
public class ResultActivity extends BaseActivity{
    @Bind(R.id.msg)
    TextView msg;
    @Bind(R.id.back_main)
    LinearLayout backMain;
    String msgStr = "成功";
    @Override
    public void initData(Bundle savedInstanceState) {
        setTitle("付款成功");
        showBackPressed();
        String data = getIntent().getStringExtra("data");
        if(!isEmpty(data)){
            msgStr = data;
        }
        msg.setText(msgStr);
    }
    @OnClick(R.id.back_main)
    public void onClick() {
        skipActivity(MainActivity.class);
    }
    @Override
    public int setLayoutView() {
        return R.layout.result_activity;
    }
}
