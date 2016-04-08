package hotel.convenient.com.utils;


import android.content.Context;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

public class BaiduLocalClient {
	private static GeoCoder mSearch;
	private static SuggestionSearch mSuggestionSearch;
	private static LocationClient mLocationClient ;

	public static void getLocaltion(Context context ,BDLocationListener myListener) {
		mLocationClient = new LocationClient(context);     //声明LocationClient类
		mLocationClient.registerLocationListener( myListener );    //注册监听函数
		initLocation();
	}
	public static void close(){
		if(mLocationClient!=null){
			mLocationClient.stop();
			mLocationClient = null;
		}
		if(mSuggestionSearch!=null){
			mSuggestionSearch.destroy();
			mSuggestionSearch= null;
		}
		if(mSearch!=null){
			mSearch.destroy();
			mSearch = null;
		}
	}
	public static void stop(){
		if(mLocationClient!=null){
			mLocationClient.stop();
		}
	}
	public static void startGetLocaltion() {
		mLocationClient.start();
	}
	private static void initLocation(){
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
		int span=1000;
		option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);//可选，默认false,设置是否使用gps
		option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死  
		option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		mLocationClient.setLocOption(option);
	}
	
	public void requestSuggestion(SuggestionSearchOption option,OnGetSuggestionResultListener linstener){
		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(linstener);
		mSuggestionSearch.requestSuggestion(option);
	}
	
	public void getAddressByLatLng(LatLng latlng,OnGetGeoCoderResultListener coderResultListener){
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(coderResultListener);
		mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latlng));  
	}
	

}
