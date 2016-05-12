package hotel.convenient.com.domain;

public class Information {
	private int id;
	private int user_id;
	private String create_time;
	private String message;
	private int status;
	private int publishId;
	private int roomOrderId;
	private int type;
	private String title;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public static final int TYPE_REGISTER = 0;
	public static final int TYPE_PUBLISH = 1;
	public static final int TYPE_ORDER_ROOM = 2;
	public int getPublishId() {
		return publishId;
	}
	public void setPublishId(int publishId) {
		this.publishId = publishId;
	}
	public int getRoomOrderId() {
		return roomOrderId;
	}
	public void setRoomOrderId(int roomOrderId) {
		this.roomOrderId = roomOrderId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		if (create_time.indexOf(".") != -1) {
			this.create_time = create_time.substring(0, create_time.indexOf("."));
		} else {
			this.create_time = create_time;
		}
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
