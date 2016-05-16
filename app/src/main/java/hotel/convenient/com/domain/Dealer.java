package hotel.convenient.com.domain;

import java.util.Date;

public class Dealer {
	private int id;
	private String name;
	private String nickname;
	private String password;
	private Date birthday;
	private String sex;
	private int age;
	private String phonenumber;
	private String id_card;
	private String personality_signature;
	private String head_image;
	private String register_time;
	private String img_dir;
	private String bank_card;
	private String email;
	private String status;
	private int type;


	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Dealer(String password, String phonenumber) {
		super();
		this.password = password;
		this.phonenumber = phonenumber;
	}
	public Dealer() {
		super();
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
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	public String getId_card() {
		return id_card;
	}
	public void setId_card(String id_card) {
		this.id_card = id_card;
	}
	public String getPersonality_signature() {
		return personality_signature;
	}
	public void setPersonality_signature(String personality_signature) {
		this.personality_signature = personality_signature;
	}
	public String getHead_image() {
		return head_image;
	}
	public void setHead_image(String head_image) {
		this.head_image = head_image;
	}
	public String getRegister_time() {
		return register_time;
	}
	public void setRegister_time(String register_time) {
		if (register_time.indexOf(".") != -1) {
			this.register_time = register_time.substring(0, register_time.indexOf("."));
		} else {
			this.register_time = register_time;
		}
	}
	public String getImg_dir() {
		return img_dir;
	}
	public void setImg_dir(String img_dir) {
		this.img_dir = img_dir;
	}
	public String getBank_card() {
		return bank_card;
	}
	public void setBank_card(String bank_card) {
		this.bank_card = bank_card;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
