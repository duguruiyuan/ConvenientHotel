package hotel.convenient.com.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;

import butterknife.Bind;
import hotel.convenient.com.R;
import hotel.convenient.com.base.BaseActivity;
import hotel.convenient.com.domain.ChooseDealerInfo;
import hotel.convenient.com.domain.Publish;
import hotel.convenient.com.http.HostUrl;
import hotel.convenient.com.utils.ImageUtils;
import hotel.convenient.com.utils.LogUtils;
import hotel.convenient.com.view.DateLinearLayout;

/**
 * Created by Gonyb on 2016/04/10.
 */
public class DealerInfoActivity extends BaseActivity{
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ChooseDealerInfo dealerInfo;
    @Bind(R.id.date_pick_ll)
    DateLinearLayout dateLinearLayout;
    @Bind(R.id.main_image)
    ImageView main_image;
    @Bind(R.id.tv_count_image)
    TextView tv_count_image;
    @Bind(R.id.address)
    TextView address;
    @Bind(R.id.mapView)
    MapView mMapView;
    private BaiduMap mBaiduMap;
    

    @Override
    public void initData(Bundle savedInstanceState) {
        dealerInfo = (ChooseDealerInfo) getIntent().getSerializableExtra("data");
        if(dealerInfo==null){
            return;
        }
        initView();
        initData();
    }

    @Override
    public int setLayoutView() {
        SDKInitializer.initialize(getApplicationContext());
        return R.layout.dealer_info_activity;
    }

    private void initData() {
        final Publish publish = dealerInfo.getPublish();
        initMap(publish);
        collapsingToolbarLayout.setTitle(publish.getDealer_name());
        if(publish.getImage_name().indexOf(",")==-1){
            LogUtils.e(HostUrl.HOST+publish.getDir_path()+"/"+publish.getImage_name());
            ImageUtils.setImage(main_image, HostUrl.HOST+publish.getDir_path()+"/"+publish.getImage_name(),R.drawable.mei_zi);
        }else{
            LogUtils.e(HostUrl.HOST+publish.getDir_path()+"/"+publish.getImage_name().split(",")[0]);
            ImageUtils.setImage(main_image,HostUrl.HOST+publish.getDir_path()+"/"+publish.getImage_name().split(",")[0],R.drawable.mei_zi);
        }
        tv_count_image.setText(publish.getImage_name().split(",").length+"张");
        address.setText(publish.getRoom_province()+publish.getRoom_city()+publish.getRoom_address_detail()+publish.getRoom_house_number());
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));//设置还没收缩时状态下字体颜色
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色


        dateLinearLayout.setmBaseActivity(this);
        dateLinearLayout.setCalendar(dealerInfo.getStartCalendar(),dealerInfo.getEndCalendar());
        main_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipActivity(PreviewRoomPhotoActivity.class,false,"data",publish);
            }
        });

        
    }
    
    
    private void initMap(Publish publish) {
        mBaiduMap = mMapView.getMap();
        //普通地图  
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //设置地图定位到point点
        LogUtils.e("publish.getLatitude():"+publish.getLatitude());
        LogUtils.e("publish.getLongitude():"+publish.getLongitude());
        LatLng latLng = new LatLng(publish.getLatitude(), publish.getLongitude());
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
        //设置地图的缩放比例
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(latLng, 18);
        mBaiduMap.setMapStatus(msu);

        UiSettings uiSettings = mBaiduMap.getUiSettings();
        uiSettings.setZoomGesturesEnabled(false);
        uiSettings.setAllGesturesEnabled(false);
        uiSettings.setCompassEnabled(false);
        uiSettings.setOverlookingGesturesEnabled(false);
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setScrollGesturesEnabled(false);
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.ctl_collapsing);
        
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
        mMapView.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
        mMapView.onPause();
    }
}