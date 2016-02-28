package hotel.convenient.com.utils;

/**
 * Created by Gyb on 2016/2/2 17:40
 */
public class TextUtils {
    public static boolean checkMoney(String money){
        if(android.text.TextUtils.isEmpty(money)){
            return false;
        }
        if(money.matches("[0-9]+\\.*[0-9]*")){
            return true;
        }else {
            return false;
        }
    }
}
