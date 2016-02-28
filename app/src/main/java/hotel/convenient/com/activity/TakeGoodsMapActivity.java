package hotel.convenient.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import org.xutils.view.annotation.ContentView;


import hotel.convenient.com.R;
import hotel.convenient.com.base.BaseActivity;
import hotel.convenient.com.domain.Address;
import hotel.convenient.com.utils.LogUtils;

@ContentView(R.layout.takegoods_baidumap)
public class TakeGoodsMapActivity extends BaseActivity {
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private TextView addressTextView;
	private GeoCoder geoCoder ;  
	private Button bt_takegoods_map_confirm;
	private Address address;
	@Override
	public void initData(Bundle savedInstanceState) {
		SDKInitializer.initialize(getApplicationContext());
		showBackPressed();
		setTitle("确认地图位置");
		initView();
		initData();
		initListener();
	}
	private void initView() {
		addressTextView = (TextView) findViewById(R.id.address);
		bt_takegoods_map_confirm = (Button) findViewById(R.id.confirm);
	}
	private void initData() {
		//初始化address类
		address = (Address) getIntent().getSerializableExtra("address");
		if(address==null){
			address = new Address();
		}
		
		
		//初始化百度编码类
		geoCoder = GeoCoder.newInstance();
		
		Intent intent = getIntent();
        
        double latitude = intent.getDoubleExtra("latitude", 0);
        double longitude = intent.getDoubleExtra("longitude", 0);
        LatLng point = new LatLng(latitude, longitude);
        //反地理编码
		 geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(point));  
        
        
        
        mMapView = (MapView) findViewById(R.id.bmapView);  
        mBaiduMap = mMapView.getMap();  
        //普通地图  
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //设置地图定位到point点
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(point));
        //设置地图的缩放比例
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(point, 15);
        mBaiduMap.setMapStatus(msu);
       
	}
	private void initListener() {
		 mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {
				
				@Override
				public void onMapStatusChangeStart(MapStatus arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onMapStatusChangeFinish(MapStatus status) {
					LatLng target = status.target;
					//反地理编码
					 geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(target));  
				}
				
				@Override
				public void onMapStatusChange(MapStatus arg0) {
					// TODO Auto-generated method stub
					
				}
			});
		 
		 geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
			
			 //反地理编码回调函数
			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
				 if (result == null  || result.error != SearchResult.ERRORNO.NO_ERROR) {  
	                    // 没有检测到结果  
	             }else {
	            	 address.setAddress(result.getAddress());
	            	 address.setCity(result.getAddressDetail().city);
	            	 address.setDistrict(result.getAddressDetail().district);
	            	 address.setProvince(result.getAddressDetail().province);
	            	 address.setStreet(result.getAddressDetail().street);
	            	 address.setStreetNumber(result.getAddressDetail().streetNumber);
	            	 address.setLatitude(result.getLocation().latitude);
	            	 address.setLongitude(result.getLocation().longitude);
	            	 addressTextView.setText("推荐地址:"+result.getAddress());
	            	 LogUtils.e(result.getAddressDetail().province+":"+result.getAddressDetail().city+":"+result.getAddressDetail().district+":"+result.getAddressDetail().street+":"+result.getAddressDetail().streetNumber);
				}  
	                
				
			}
			//地理编码回调函数
			@Override
			public void onGetGeoCodeResult(GeoCodeResult arg0) {
				
			}
		});
		 
		 bt_takegoods_map_confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TakeGoodsMapActivity.this,Address.class);
				intent.putExtra("address", address);
				TakeGoodsMapActivity.this.setResult(RESULT_OK,intent);
				finish();
			}
		});
	}
	
	

}
