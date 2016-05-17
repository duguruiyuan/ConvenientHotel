package hotel.convenient.com.domain;

import java.io.Serializable;

public class UserRecord implements Serializable{
    private int id;
    private int dealerid;
    private int userid;
    private String name;
    private String dealer_name;
    private String room_area ;
    private String room_price ;
    private String room_type ;
    private String room_province;
    private String room_city;
    private String room_address_detail;
    private String room_house_number;
    private String dir_path;
    private String image_name;
    private String publish_end_time;
    private double longitude;
    private double latitude;
    private String publish_time;
    private String create_order_time;


    public String getCreate_order_time() {
        return create_order_time;
    }

    public void setCreate_order_time(String create_order_time) {
        if(create_order_time.indexOf(".")!=-1){
            this.create_order_time = create_order_time.substring(0,create_order_time.indexOf("."));
        }else{
            this.create_order_time = create_order_time;
        }

    }
    public void setDealer_name(String dealer_name) {
        this.dealer_name = dealer_name;
    }

    public String getDealer_name() {

        return dealer_name;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    private String url_head;

    public void setUrl_head(String url_head) {
        this.url_head = url_head;
    }

    public String getUrl_head() {

        return url_head;
    }

    public int getDealerid() {
        return dealerid;
    }

    public void setDealerid(int dealerid) {
        this.dealerid = dealerid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setPublish_end_time(String publish_end_time) {
        this.publish_end_time = publish_end_time;
    }

    public String getPublish_end_time() {

        return publish_end_time;
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
    public String getRoom_area() {
        return room_area;
    }
    public void setRoom_area(String room_area) {
        this.room_area = room_area;
    }
    public String getRoom_price() {
        return room_price;
    }
    public void setRoom_price(String room_price) {
        this.room_price = room_price;
    }
    public String getRoom_type() {
        return room_type;
    }
    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }
    public String getRoom_province() {
        return room_province;
    }
    public void setRoom_province(String room_province) {
        this.room_province = room_province;
    }
    public String getRoom_city() {
        return room_city;
    }
    public void setRoom_city(String room_city) {
        this.room_city = room_city;
    }
    public String getRoom_address_detail() {
        return room_address_detail;
    }
    public void setRoom_address_detail(String room_address_detail) {
        this.room_address_detail = room_address_detail;
    }
    public String getRoom_house_number() {
        return room_house_number;
    }
    public void setRoom_house_number(String room_house_number) {
        this.room_house_number = room_house_number;
    }
    public String getDir_path() {
        return dir_path;
    }
    public void setDir_path(String dir_path) {
        this.dir_path = dir_path;
    }
    public String getImage_name() {
        return image_name;
    }
    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }
    private long startTime;
    private long endTime;
    private int day;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }


	
}
