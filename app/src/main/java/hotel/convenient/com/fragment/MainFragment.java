package hotel.convenient.com.fragment;

import android.os.Bundle;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

import hotel.convenient.com.R;
import hotel.convenient.com.base.BaseFragment;
import hotel.convenient.com.utils.BaiduLocalClient;

/**
 * Created by Gyb on 2016/3/31 14:33
 */
public class MainFragment extends BaseFragment implements BDLocationListener,MainListFragment.GetLocation {
    MainListFragment mainListFragment;
    private double latitude;
    private double longitude;
    @Override
    public int setLayoutView() {
        return R.layout.dealer_fragment;
    }

    @Override
    public void initData(View view, Bundle savedInstanceState) {
        BaiduLocalClient.getLocaltion(getContext(),this);
        BaiduLocalClient.startGetLocaltion();
        mainListFragment = new MainListFragment();
        mainListFragment.setGetLocation(this);
        getChildFragmentManager().beginTransaction().replace(R.id.dealer_list_fragment, mainListFragment).commitAllowingStateLoss();
    }
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (bdLocation != null) {
            latitude = bdLocation.getLatitude();
            longitude = bdLocation.getLongitude();
            mainListFragment.setLatLng(latitude,longitude);
        } else {
            mBaseActivity.showShortToast("重新失败...");
        }
    }

    @Override
    public void getLocation() {
        BaiduLocalClient.startGetLocaltion();
    }
}
