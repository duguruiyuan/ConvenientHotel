package hotel.convenient.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionResult.SuggestionInfo;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import hotel.convenient.com.R;
import hotel.convenient.com.adapter.TakeGoodsSearchAdapter;
import hotel.convenient.com.base.BaseActivity;
import hotel.convenient.com.domain.Address;
import hotel.convenient.com.domain.SearchInfo;
import hotel.convenient.com.utils.BaiduLocalClient;
import hotel.convenient.com.utils.DensityUtils;
import hotel.convenient.com.utils.LogUtils;
import hotel.convenient.com.view.SearchLinearLayout;

@ContentView(R.layout.takegoods_address_search)
public class TakeGoodsSearchAddressActivity extends BaseActivity {
	public static final String CITY = "city";
	public static final String LONGITUDE = "longitude";
	public static final String LATITUDE = "latitude";
	public static final String MAIN_SEARCH = "main_search";
	@ViewInject(R.id.lv_takegoods_search)
	ListView listview;
	List<SearchInfo> list;
	TakeGoodsSearchAdapter adapter;
	BaiduLocalClient local;
	OnGetPoiSearchResultListener poiListener ;
	String keyword;
	Address address;
	String action;
	private TextWatcher watcher;
	private SearchLinearLayout ssl_search;
	private ImageView leftIcon;
	String city;
	OnGetGeoCoderResultListener coderResultListener;
	private String resultName;
	
	private boolean flag = true;
	@Override
	public void initData(Bundle savedInstanceState) {
		SDKInitializer.initialize(getApplicationContext());
		local = new BaiduLocalClient();
		action = getIntent().getAction();
		ssl_search = (SearchLinearLayout) findViewById(R.id.sll_search);
		leftIcon = (ImageView) findViewById(R.id.leftIcon);
		initViewData(savedInstanceState) ;
		
	}
	public void initViewData(Bundle savedInstanceState) {
		list = new ArrayList<>();
		city = getIntent().getStringExtra(CITY);
		address = (Address) getIntent().getSerializableExtra("address");
		//final String city = "深圳";
		adapter = new TakeGoodsSearchAdapter(this, list);
		listview.setAdapter(adapter);
		listview.setVisibility(View.GONE);
		//BaseActivity.setEmptyViewByListView("若输入的地址无法识别，请更换重试", this, listview);
		leftIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				Intent intent = new Intent();
				SearchInfo info = (SearchInfo) adapter.getItem(position);
				intent.putExtra(LATITUDE, info.getLatlng().latitude);//纬度
				intent.putExtra(LONGITUDE, info.getLatlng().longitude);//经度
				intent.setClass(TakeGoodsSearchAddressActivity.this,TakeGoodsMapActivity.class);
				if(address==null){
					startActivityForResult(intent, 1);
				}else{
					intent.putExtra("address", address);
					startActivityForResult(intent, 1);
				}
			}
		});
		watcher =  new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.length()==0){
					list.clear();
					listview.setVisibility(View.GONE);
					adapter.notifyDataSetChanged();
					return;
				}
				keyword = s.toString();
				//local.getAddressPointMore(city, s.toString(), poiListener);
				local.requestSuggestion(new SuggestionSearchOption().city(city).keyword(s.toString()), new OnGetSuggestionResultListener() {
					
					@Override
					public void onGetSuggestionResult(SuggestionResult result) {
						if(result != null&&SearchResult.ERRORNO.NO_ERROR==result.error){
							try {
								list.clear();
								adapter.notifyDataSetChanged();
								if(result.getAllSuggestions()!=null&&result.getAllSuggestions().size()!=0){
									for(SuggestionInfo suggestionInfo:result.getAllSuggestions()){
										String city = suggestionInfo.city;
										if(city==null){
											if(flag){
												flag =false;
											}
										}else if(city.contains(TakeGoodsSearchAddressActivity.this.city)){
											String district = suggestionInfo.district;
											resultName = suggestionInfo.key;
											LatLng latlng = suggestionInfo.pt;
											String uid = suggestionInfo.uid;
											//LogUtils.e("city:"+city+" district:"+district+" key:"+resultName+" latlng:"+latlng.latitude+" "+latlng.longitude+" uid:"+uid);
											list.add(new SearchInfo(keyword, resultName, city, district, latlng));
											 //反地理编码
											local.getAddressByLatLng(latlng, coderResultListener);
										}else{
											if(flag){
												flag =false;
											}
										}
										
									}
								}else{
									list.clear();
									adapter.notifyDataSetChanged();
									if(flag){
										flag =false;
									}
								}
							} catch (Exception e) {
								if(flag){
									flag =false;
								}
								e.printStackTrace();
							}
						}else{
							LogUtils.e("检索出错!");
							list.clear();
							if(flag){
								flag =false;
							}
							adapter.notifyDataSetChanged();
						}
					}
				});
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		};
		coderResultListener = new OnGetGeoCoderResultListener() {
			
			 //反地理编码回调函数
			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
				 if (result == null  || result.error != SearchResult.ERRORNO.NO_ERROR) {  
	                    // 没有检测到结果  
	             }else {
//	            	 list.add(new SearchInfo(keyword, name, result.getAddressDetail().city, result.getAddressDetail().district, latlng,result.getAddress()));
	            	 LogUtils.e("listsize:"+list.size());
	            	 for(SearchInfo info : list){
	            		 if(Math.abs(info.getLatlng().latitude-result.getLocation().latitude)<0.00001){
	            			 if(Math.abs(info.getLatlng().longitude -result.getLocation().longitude)<0.00001){
		            			 info.setDetailAddress(result.getAddress());
		            		 }
	            		 }
	            	 }
	            	 listview.setVisibility(View.VISIBLE);
				     adapter.notifyDataSetChanged();
				}  
	                
				
			}
			//地理编码回调函数
			@Override
			public void onGetGeoCodeResult(GeoCodeResult arg0) {
				
			}
		};
		poiListener = new OnGetPoiSearchResultListener() {
			
			@Override
			public void onGetPoiResult(PoiResult result) {
				if (result.error != SearchResult.ERRORNO.NO_ERROR) {
			        //详情检索失败
					list.clear();
			    }
			    else {
			        //检索成功
			    	list.clear();
			    	for(PoiInfo info :result.getAllPoi()){
			    		LogUtils.e(info.address+" "+info.name+" "+info.uid+" "+info.postCode);
			    		list.add(new SearchInfo(keyword, "",info));
			    	}
			    	listview.setVisibility(View.VISIBLE);
			    	adapter.notifyDataSetChanged();
			    }
			}
			
			@Override
			public void onGetPoiDetailResult(PoiDetailResult result) {
					
			}
		};
		ssl_search.addTextChangedListener(watcher);
		ssl_search.setHint("输入小区、街道或大厦");
		ssl_search.setSearch_bt_show_forever(SearchLinearLayout.BT_GONE_FOREVER);
		ssl_search.setTextSize(15);
		ssl_search.setLinearLayoutHight(DensityUtils.dip2px(this, 36));
		ssl_search.setSearchIconSize(DensityUtils.dip2px(this, 18), DensityUtils.dip2px(this, 18));
		ssl_search.setEditOnFocusChangeListener(false);
		ssl_search.setEditFocus(true);
		
	}
	/**
	 * 获取返回的信息  并处理
	 */
	 @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 if(requestCode==1){
			 if(resultCode==RESULT_OK){
				 setResult(RESULT_OK,data);
				 finish();
			 }
		 }
	}
}
