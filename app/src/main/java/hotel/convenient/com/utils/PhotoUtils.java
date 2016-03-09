package hotel.convenient.com.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import hotel.convenient.com.BuildConfig;
import hotel.convenient.com.base.BaseActivity;

/**
 * Created by Gyb on 2016/3/9 17:57
 */
public class PhotoUtils {
    public static final String IMAGE_TYPE = "image/*";
    /**
     * 从本地相册获取图片
     * @return
     */
    public static void startOpenImageByLocal(Uri imageUri,BaseActivity activity, int resultCode){
        Intent getAlbum;
        if(BuildConfig.VERSION_CODE>=19){
            getAlbum = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        }else{
            getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
        }
//        getAlbum.setType(IMAGE_TYPE);
        getAlbum.putExtra("crop","true");
        getAlbum.setDataAndType(imageUri,IMAGE_TYPE);
        getAlbum.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        getAlbum.putExtra("scale", true);
        // 裁剪框的比例，1：1
        getAlbum.putExtra("aspectX", 1);
        getAlbum.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        getAlbum.putExtra("outputX", 800);
        getAlbum.putExtra("outputY", 800);
        getAlbum.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 图片格式
        activity.startActivityForResult(getAlbum, resultCode);
    }

    public static void startOpenImageByPhotograph(Uri imageUri, BaseActivity activity, int resultCode){
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
            activity.startActivityForResult(getImageByCamera, resultCode);
        }else {
            activity.showShortToast("请确认已经插入SD卡");
        }
    }
    /**
     * 剪切图片
     */
    public static  void crop(Uri imageUri, BaseActivity activity,int code) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        // 裁剪图片意图
        intent.setDataAndType(imageUri, IMAGE_TYPE);
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 800);
        intent.putExtra("outputY", 800);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 图片格式
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        activity.startActivityForResult(intent, code);
    }
}
