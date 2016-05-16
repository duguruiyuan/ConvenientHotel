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
import hotel.convenient.com.http.HostUrl;
import hotel.convenient.com.http.HttpUtils;
import hotel.convenient.com.http.RequestParams;
import hotel.convenient.com.http.ResultJson;
import hotel.convenient.com.http.SimpleCallback;

/**
 * Created by cwy on 2016/4/15 10:50
 */
public class NotDealerFragment extends BaseFragment {
    @Bind(R.id.register_dealer_fragment)
    LinearLayout register_fragment;
    public interface IBecomeDealer{
        void becomeDealer();
    }
    private IBecomeDealer iBecomeDealer;

    public IBecomeDealer getiBecomeDealer() {
        return iBecomeDealer;
    }

    public void setiBecomeDealer(IBecomeDealer iBecomeDealer) {
        this.iBecomeDealer = iBecomeDealer;
    }

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
                HttpUtils.get(new RequestParams(HostUrl.HOST + HostUrl.URL_BECOME_DEALER), new SimpleCallback() {
                    @Override
                    public <T> void simpleSuccess(String url, String result, ResultJson<T> resultJson) {
                        if(resultJson.isSuccess()){
                            mBaseActivity.showShortToast(resultJson.getMsg());
                            //通知更换成dealerFragment
                            if(iBecomeDealer!=null){
                                iBecomeDealer.becomeDealer();
                            }
                        }
                    }
                });
                break;
        }
    }
}
