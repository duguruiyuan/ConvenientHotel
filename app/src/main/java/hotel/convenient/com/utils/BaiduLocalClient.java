package hotel.convenient.com.utils;

import android.content.Context;
import android.text.TextUtils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

public class BaiduLocalClient {
	private LocalListener localListener;
	private GeoCoder mSearch;
	private PoiSearch mPoiSearch;
	private SuggestionSearch mSuggestionSearch;
	public BaiduLocalClient() {
	}
	public BaiduLocalClient(Context context, LocalListener localListener) {
		this.localListener = localListener;
	}
	public interface LocalListener {
		public void callBack(String local, double latitude, double longitude);
	}

	public void getAddressPoint(String city,String address,OnGetGeoCoderResultListener coderResultListener){
		mSearch = GeoCoder.newInstance();
		GeoCodeOption geoCodeOption = new GeoCodeOption();
		if(!TextUtils.isEmpty(city)){
			geoCodeOption.city(city);
		}
		if(!TextUtils.isEmpty(address)){
			geoCodeOption.address(address);
		}else{
			
		}
		if(coderResultListener==null){
			throw new NullPointerException("亲，记得给回调接口");
		}
		mSearch.geocode(geoCodeOption);
		mSearch.setOnGetGeoCodeResultListener(coderResultListener);
		mSearch.destroy();
	}
	
	public void destory(){
		if(mPoiSearch!=null){
			mPoiSearch.destroy();
		}
		if(mSearch!=null){
			mSearch.destroy();
		}
	}
	
	public void getAddressPointMore(String city,String keyword,OnGetPoiSearchResultListener poiListener ){
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
		mPoiSearch.searchInCity((new PoiCitySearchOption())  
			    .city(city)  
			    .keyword(keyword)  
			    .pageNum(2)
			    .pageCapacity(20)
				);
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
