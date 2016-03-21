package hotel.convenient.com.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Gyb on 2015/12/3 09:41
 */
public class PreferenceUtils {
   
    public static final String LOGIN_FLAG = "login_flag";
    public static final String ISLOGIN = "isLogin";
    public static final String USERNAME = "username";
    public static final String PHONE = "phone";
    public static final String PASSWORD = "password";
    public static final String LAT = "lat";
    public static final String LNG = "lng";
    public static final String LOCATION = "location";
    /**
     * 设置登录状态为已登录
     * @param context 
     */
    public static void setLoginFlag(Context context,String phone,String username, String password){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_FLAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(ISLOGIN, true);
        edit.putString(USERNAME, username);
        edit.putString(PHONE, phone);
        password = MD5Utils.convertMD5(password);//简单加密  并不是md5加密
        edit.putString(PASSWORD,password);
        edit.commit();
    }

    /**
     * 保存位置信息
     * @param context
     * @param lat
     * @param lng
     */
    public static void setLatlng(Context context,String lat,String lng){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOCATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(ISLOGIN, true);
        edit.putString(LAT, lat);
        edit.putString(LNG, lng);
        edit.commit();
    }
    public static String getLat(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOCATION, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LAT,"");
    }
    public static String getLng(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOCATION, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LNG,"");
    }
    public  static void removeLatLng(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOCATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.commit();
    }
    /**
     * 删除登录状态  全部清空
     * @param context
     */
    public  static void removeLoginFlag(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_FLAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.commit();
    }

    /**
     * 是否登录
     * @param context
     * @return
     */
    public static boolean isLogin(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_FLAG, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(ISLOGIN,false);
    }

    /**
     * 得到用户名
     * @param context
     * @return
     */
    public static String getLoginUsername(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_FLAG, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USERNAME, "");
    }
    /**
     * 得到电话号码
     * @param context
     * @return
     */
    public static String getPhone(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_FLAG, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PHONE, "");
    }
    /**
     * 得到用户登录密码
     * @param context
     * @return
     */
    public static String getLoginPassword(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_FLAG, Context.MODE_PRIVATE);
        String password = sharedPreferences.getString(PASSWORD, "");
        password = MD5Utils.convertMD5(password);
        return password;
    }
}
