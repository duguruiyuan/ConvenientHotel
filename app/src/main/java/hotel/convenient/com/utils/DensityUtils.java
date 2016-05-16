package hotel.convenient.com.utils;

import android.content.Context;

/**
 * Created by cwy on 2015/11/24 17:36
 */
public class DensityUtils {
    public static int dip2px(Context context,int dpValue){
        float desity =  context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*desity+0.5F);
    }
    public static int px2dip(Context context,int pxValue){
        float desity =  context.getResources().getDisplayMetrics().density;
        return (int)(pxValue/desity+0.5F);
    }
    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
    public static int getScreenWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }
    public static int getScreenHeight(Context context){
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}
