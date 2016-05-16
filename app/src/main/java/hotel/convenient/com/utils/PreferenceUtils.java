package hotel.convenient.com.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by cwy on 2015/12/3 09:41
 */
public class PreferenceUtils {
   
    public static final String LOGIN_FLAG = "login_flag";
    public static final String IS_LOGIN = "isLogin";
    public static final String USERNAME = "username";
    public static final String PHONE = "phone";
    public static final String PASSWORD = "password";
    public static final String ID_CARD = "idCard";
    public static final String LAT = "lat";
    public static final String LNG = "lng";
    public static final String LOCATION = "location";
    public static final String HEAD_URL = "headImageUrl";
    public static final String IS_DEALER = "isDealer";
    /**
     * 设置登录状态为已登录
     * @param context 
     */
    public static void setLoginFlag(Context context,String phone,String nickname, String password,String idCard,String headImageUrl,boolean isDealer){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_FLAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(IS_LOGIN, true);
        edit.putString(USERNAME, nickname);
        edit.putString(PHONE, phone);
        edit.putString(ID_CARD, idCard);
        edit.putString(HEAD_URL, headImageUrl);
        password = MD5Utils.convertMD5(password);//简单加密  并不是md5加密
        edit.putString(PASSWORD,password);
        edit.putBoolean(IS_DEALER,isDealer);
        edit.commit();
    }
    public static void setIDCard(Context context,String username,String idCard){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_FLAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(USERNAME, username);
        edit.putString(ID_CARD, idCard);
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
        edit.putBoolean(IS_LOGIN, true);
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
        if(sharedPreferences==null){
            return false;
        }
        return sharedPreferences.getBoolean(IS_LOGIN,false);
    }
    /**
     * 是否登录
     * @param context
     * @return
     */
    public static boolean isDealer(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_FLAG, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_DEALER,false);
    }

    /**
     * 得到用户名
     * @param context
     * @return
     */
    public static String getNickname(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_FLAG, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USERNAME, "");
    }
    /**
     * 得到身份证
     * @param context
     * @return
     */
    public static String getIdCard(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_FLAG, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ID_CARD, "");
    }
    /**
     * 得到头像地址
     * @param context
     * @return
     */
    public static String getHeadUrl(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_FLAG, Context.MODE_PRIVATE);
        return sharedPreferences.getString(HEAD_URL, "");
    }
    /**
     * 设置头像地址
     * @param context
     * @return
     */
    public static void setHeadUrl(Context context,String headUrl){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_FLAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(HEAD_URL, headUrl);
        edit.commit();
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
