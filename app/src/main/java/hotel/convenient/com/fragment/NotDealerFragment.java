package hotel.convenient.com.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.OnClick;
import hotel.convenient.com.R;
import hotel.convenient.com.activity.AccountBankCardActivity;
import hotel.convenient.com.activity.CheckRealNameActivity;
import hotel.convenient.com.app.App;
import hotel.convenient.com.base.BaseFragment;

/**
 * Created by cwy on 2016/4/15 10:50
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
                if( mBaseActivity.isEmpty(App.getInstanceApp().getDealer().getId_card())){
                    mBaseActivity.showAlertDialog("成为商家需先实名认证!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mBaseActivity.skipActivity(CheckRealNameActivity.class,false);
                        }
                    });
                    return;
                }else if(mBaseActivity.isEmpty(App.getInstanceApp().getDealer().getBank_card())){
                    mBaseActivity.showAlertDialog("成为商家需先实名认证!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mBaseActivity.skipActivity(AccountBankCardActivity.class,false);
                        }
                    });
                    return;
                }
                break;
        }
    }
}
