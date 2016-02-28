package hotel.convenient.com.domain;

import java.io.Serializable;

import android.text.TextUtils;

/**
 *
 */
public class Address implements Serializable{
	private int id;
	private String name;
	private String address;
	private String phone;
	private boolean isSelect;
	private String province;
	private String city;
	private String district;
	private String street;
	private String streetNumber;
	private String shortAddress;
	private double latitude; // 纬度
	private double longitude; // 经度
	private String aid;
	public String getShortAddress() {
		return shortAddress;
	}

	public void setShortAddress(String shortAddress) {
		this.shortAddress = shortAddress;
	}
	public Address() {
		super();
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
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

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	@Override
	public String toString() {
		return "Address [id=" + id + ", name=" + name + ", address=" + address
				+ ", phone=" + phone + ", isSelect=" + isSelect + ", province="
				+ province + ", city=" + city + ", district=" + district
				+ ", street=" + street + ", streetNumber=" + streetNumber
				+ ", latitude=" + latitude + ", longitude=" + longitude
				+ ", aid=" + aid + "]";
	}

	/**
	 * 得到详细的收货地址   getProvince()+getCity()+getDistrict()+getStreet()+getStreetNumber()
	 * @return
	 */
	public String getDetailAddress(){
		return getProvince()+getCity()+getDistrict()+getStreet()+getStreetNumber();
	}

	public Address(int id, String name, String phone, boolean isSelect,
			String province, String city, String district, String street,
			String streetNumber) {
		super();
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.isSelect = isSelect;
		this.province = province;
		this.city = city;
		this.district = district;
		this.street = street;
		this.streetNumber = streetNumber;
		this.address = province+city+district+street+streetNumber;
	}

	public Address(int id, String name, String phone,
			boolean isSelect, String province, String city, String district,
			String street, String streetNumber, 
			String latitude, String longitude, String aid) {
		super();
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.isSelect = isSelect;
		this.province = province;
		this.city = city;
		this.district = district;
		this.street = street;
		this.streetNumber = streetNumber;
		if(!TextUtils.isEmpty(latitude)){
			this.latitude = Double.parseDouble(latitude);
		}
		if(!TextUtils.isEmpty(longitude)){
			this.longitude = Double.parseDouble(longitude);
		}
		this.aid = aid;
		this.address = province+city+district+street+streetNumber;
	}
	
}
