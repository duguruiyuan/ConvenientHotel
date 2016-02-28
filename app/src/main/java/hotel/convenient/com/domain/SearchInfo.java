package hotel.convenient.com.domain;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;

public class SearchInfo {
	private String search_key;
	private String result;
	private String city;
	private String district;
	private LatLng latlng;
	private String detailAddress;
	
	public String getDetailAddress() {
		return detailAddress;
	}
	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}
	public SearchInfo(String search_key, String result, String city,
			String district, LatLng latlng, String detailAddress) {
		super();
		this.search_key = search_key;
		this.result = result;
		this.city = city;
		this.district = district;
		this.latlng = latlng;
		this.detailAddress = detailAddress;
	}
	public LatLng getLatlng() {
		return latlng;
	}
	public void setLatlng(LatLng latlng) {
		this.latlng = latlng;
	}
	public SearchInfo(String search_key, String result, String city,
			String district, LatLng latlng) {
		super();
		this.search_key = search_key;
		this.result = result;
		this.city = city;
		this.district = district;
		this.latlng = latlng;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public SearchInfo(String search_key, String result, PoiInfo poiifo) {
		super();
		this.search_key = search_key;
		this.result = result;
		this.poiifo = poiifo;
	}
	private PoiInfo poiifo;
	public SearchInfo(String search_key, PoiInfo poiifo) {
		super();
		this.search_key = search_key;
		this.poiifo = poiifo;
	}
	public String getSearch_key() {
		return search_key;
	}
	public void setSearch_key(String search_key) {
		this.search_key = search_key;
	}
	public PoiInfo getPoiifo() {
		return poiifo;
	}
	public void setPoiifo(PoiInfo poiifo) {
		this.poiifo = poiifo;
	}
}
