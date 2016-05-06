package hotel.convenient.com.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.OnClick;
import hotel.convenient.com.R;
import hotel.convenient.com.base.BaseFragment;
import hotel.convenient.com.utils.LogUtils;

/**
 * Created by Gyb on 2016/4/15 10:50
 */
public class NotDealerFragment extends BaseFragment {
    @Bind(R.id.register_dealer_fragment)
    LinearLayout register_fragment;
    
    @Override
    public int setLayoutView() {
        return R.layout.not_dealer_fragment;
    }

    @Override
    public void initData(View view, Bundle savedInstanceState) {
        
    }
    @OnClick(R.id.register_dealer_fragment)
    public void onRegisterDealerClick(View view){
        switch (view.getId()){
            case R.id.register_dealer_fragment:
                LogUtils.e("dianji");
                break;
        }
    }
}
