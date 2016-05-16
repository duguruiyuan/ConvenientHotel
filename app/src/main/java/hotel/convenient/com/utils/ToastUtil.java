package hotel.convenient.com.utils;

import android.widget.Toast;

import hotel.convenient.com.app.App;


/**
 * Created by cwy on 2016/3/30 14:38
 */
public class ToastUtil {
    private static Toast toast; //保证不重复使用toast
    public static void showLongToast(String msg){
        if(android.text.TextUtils.isEmpty(msg)){
            return;
        }
        if (toast==null){
            toast = Toast.makeText(App.getInstanceApp(),msg,Toast.LENGTH_LONG);
        }else{
            toast.setText(msg);
        }
        toast.show();
    }
    public static void showShortToast(String msg){
        if(android.text.TextUtils.isEmpty(msg)){
            return;
        }
        if (toast==null){
            toast = Toast.makeText(App.getInstanceApp(),msg,Toast.LENGTH_SHORT);
        }else{
            toast.setText(msg);
        }
        toast.show();
    }
}
