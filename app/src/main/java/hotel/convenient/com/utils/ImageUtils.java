package hotel.convenient.com.utils;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import hotel.convenient.com.R;

/**
 * Created by Gyb on 2015/11/24 16:27
 */
public class ImageUtils {
    /**
     * @param imageView   图片控件
     * @param url   图片的url地址
     * @param defaultDrawableId   默认占位符
     * @param defaultErrorDrawableId  错误占位符
     */
    public static void setImage(ImageView imageView,String url,int defaultDrawableId,int defaultErrorDrawableId){
        try {
            RequestCreator load = Picasso.with(imageView.getContext()).load(url);
            load.placeholder(defaultDrawableId).error(defaultErrorDrawableId).into(imageView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void setImage(ImageView imageView,String url,int defaultDrawableId){
        setImage(imageView,url, defaultDrawableId,defaultDrawableId);
    }
    public static void setImage(ImageView imageView,String url){
        setImage(imageView,url, R.mipmap.ic_launcher);
    }

    /**
     * 得到程序的最大运行内存  单位 KB
     * @return
     */
    public long getMaxMemory() {
        return Runtime.getRuntime().maxMemory() / 1024;
    }
}
