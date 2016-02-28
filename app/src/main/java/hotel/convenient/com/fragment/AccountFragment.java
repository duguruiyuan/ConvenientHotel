package hotel.convenient.com.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import org.xutils.http.RequestParams;
import org.xutils.http.request.UriRequest;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import hotel.convenient.com.R;
import hotel.convenient.com.base.BaseFragment;

@ContentView(R.layout.account_fragment)
public class AccountFragment extends BaseFragment {

    @Override
    public void initData(final View view, Bundle savedInstanceState) {

    }

    @Event({R.id.account_safe,R.id.account_detailed,R.id.account_bank_card})
    private void onItemOnclick(View view) {
        switch (view.getId()) {
            case R.id.account_safe:       //账户安全
//                mBaseActivity.skipActivity(AccountSafeActivity.class,false);
                break;
            case R.id.account_detailed:   //账户详情
//                mBaseActivity.skipActivity(AccountDetailedActivity.class,false);
                break;
            case R.id.account_bank_card:   //我的银行卡
//                mBaseActivity.skipActivity(AccountBankCardActivity.class, false);
                break;
        }
    }
}
