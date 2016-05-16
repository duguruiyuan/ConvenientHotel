package hotel.convenient.com.domain;

public class RoomOrder {
	private int id;
	private int publishId;
	private long startTime;
	private long endTime;
	private int day;
	private int userId;

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public RoomOrder(int publishId, long startTime, long endTime, int day) {
		this.publishId = publishId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.day = day;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPublishId() {
		return publishId;
	}
	public void setPublishId(int publishId) {
		this.publishId = publishId;
	}
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
