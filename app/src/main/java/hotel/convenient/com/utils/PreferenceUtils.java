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
    public static final String PASSWORD = "password";
    /**
     * 设置登录状态为已登录
     * @param context 
     */
    public static void setLoginFlag(Context context,String username,String password){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_FLAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(ISLOGIN, true);
        edit.putString(USERNAME, username);
        password = MD5Utils.convertMD5(password);//简单加密  并不是md5加密
        edit.putString(PASSWORD,password);
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
