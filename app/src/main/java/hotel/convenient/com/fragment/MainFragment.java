package hotel.convenient.com.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;

import java.util.GregorianCalendar;

import butterknife.Bind;
import butterknife.OnClick;
import hotel.convenient.com.R;
import hotel.convenient.com.base.BaseFragment;
import hotel.convenient.com.utils.BaiduLocalClient;
import hotel.convenient.com.utils.LogUtils;
import hotel.convenient.com.utils.ToastUtil;
import hotel.convenient.com.view.CityPickerDialog;
import hotel.convenient.com.view.DateLinearLayout;

/**
 * Created by Gyb on 2016/3/31 14:33
 */
public class MainFragment extends BaseFragment implements BDLocationListener,MainListFragment.GetLocation {
    MainListFragment mainListFragment;
    private double latitude;
    private double longitude;
    private TextView pickCity;
    private TextView pickDescLocal;
    private CityPickerDialog cityPickerDialog;
    @Bind(R.id.date_pick_ll)
    DateLinearLayout dateLinearLayout;


    @Override
    public int setLayoutView() {
        return R.layout.main_head_fragment;
    }

    @Override
    public void initData(View view, Bundle savedInstanceState) {
        pickCity = (TextView) view.findViewById(R.id.pick_city);
        pickDescLocal = (TextView) view.findViewById(R.id.pick_desc_local);
        BaiduLocalClient.getLocaltion(getContext(),this);
        BaiduLocalClient.startGetLocaltion();
        mainListFragment = new MainListFragment();
        mainListFragment.setGetLocation(this);
        getChildFragmentManager().beginTransaction().replace(R.id.dealer_list_fragment, mainListFragment).commitAllowingStateLoss();

        cityPickerDialog = new CityPickerDialog(getContext());
        cityPickerDialog.setOnCityInfoListener(new CityPickerDialog.OnCityInfoListener() {
            @Override
            public void getCityInfo(String province, String city) {
//                LogUtils.e("province:"+province);
//                LogUtils.e("city:"+city);
                pickCity.setText(city);
                
                BaiduLocalClient.getLatLngByGeoPoint(mBaseActivity.getApplication().getApplicationContext(),
                        new PoiCitySearchOption().city(city).keyword("银行"), new OnGetPoiSearchResultListener() {
                            @Override
                            public void onGetPoiResult(PoiResult poiResult) {
                                if (poiResult.getAllPoi() != null && poiResult.getAllPoi().size() > 0) {
                                    //获取地理编码结果  
                                    latitude = poiResult.getAllPoi().get(0).location.latitude;
                                    longitude = poiResult.getAllPoi().get(0).location.longitude;
                                    mainListFragment.setLatLng(latitude, longitude);
                                    
                                } else {
                                    ToastUtil.showShortToast("该地点暂不支持此服务");
                                }
                                BaiduLocalClient.stop();
                            }

                            @Override
                            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

                            }
                        });

                        
                   
            }
        });
        dateLinearLayout.setmBaseActivity(mBaseActivity);
        dateLinearLayout.setOnStartDateClickListener(new DateLinearLayout.OnStartDateClickListener() {
            @Override
            public void onStartClick(View view, GregorianCalendar startCalendar, GregorianCalendar endCalendar) {
                setDateToMainListFragment(startCalendar,endCalendar);
            }
        });
        dateLinearLayout.setOnEndDateClickListener(new DateLinearLayout.OnEndDateClickListener() {
            @Override
            public void onEndClick(View view, GregorianCalendar startCalendar, GregorianCalendar endCalendar) {
                setDateToMainListFragment(startCalendar,endCalendar);
            }
        });
        setDateToMainListFragment(dateLinearLayout.getStartCalendar(),dateLinearLayout.getEndCalendar());
    }
    
    public void setDateToMainListFragment(GregorianCalendar startCalendar,GregorianCalendar endCalendar){
        mainListFragment.setEndCalendar(endCalendar);
        mainListFragment.setStartCalendar(startCalendar);
    }
    @OnClick({R.id.pick_city,R.id.pick_desc_local})
    void onPickClick(View view){
        switch (view.getId()){
            case R.id.pick_city:
                cityPickerDialog.showSelectAlert(mBaseActivity);
                break;
            case R.id.pick_desc_local:
                
                break;
        }
    }
    

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (bdLocation != null) {
            latitude = bdLocation.getLatitude();
            longitude = bdLocation.getLongitude();
            LogUtils.e("latitude:"+latitude+" longitude:"+longitude);
            pickCity.setText(bdLocation.getAddress().city);
            mainListFragment.setLatLng(latitude,longitude);
            BaiduLocalClient.stop();
        } else {
            mBaseActivity.showShortToast("定位失败...");
        }
    }

    @Override
    public void getLocation() {
        BaiduLocalClient.startGetLocaltion();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BaiduLocalClient.close();
    }
}
