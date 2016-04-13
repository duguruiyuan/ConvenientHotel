package hotel.convenient.com.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class User implements Parcelable{
	private int id;
	private String name;
	private String nickname;
	private String password;
	private Date birthday;
	private String sex;
	private int age;
	private String phoneNumber;
	private String id_card;
	private String personality_signature;
	private String head_image;
	private Date register_time;
	private String img_dir;
	private String bank_card;
	private String email;
	private String status;

	protected User(Parcel in) {
		id = in.readInt();
		name = in.readString();
		nickname = in.readString();
		password = in.readString();
		sex = in.readString();
		age = in.readInt();
		phoneNumber = in.readString();
		id_card = in.readString();
		personality_signature = in.readString();
		head_image = in.readString();
		img_dir = in.readString();
		bank_card = in.readString();
		email = in.readString();
		status = in.readString();
	}

	public static final Creator<User> CREATOR = new Creator<User>() {
		@Override
		public User createFromParcel(Parcel in) {
			return new User(in);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(nickname);
		dest.writeString(password);
		dest.writeString(sex);
		dest.writeInt(age);
		dest.writeString(phoneNumber);
		dest.writeString(id_card);
		dest.writeString(personality_signature);
		dest.writeString(head_image);
		dest.writeString(img_dir);
		dest.writeString(bank_card);
		dest.writeString(email);
		dest.writeString(status);
	}
}
