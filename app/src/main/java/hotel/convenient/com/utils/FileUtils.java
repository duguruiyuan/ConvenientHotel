package hotel.convenient.com.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Gyb on 2015/12/30 10:45
 */
public class FileUtils {
    public static final String DIR_CACHE = "CacheDir";
    public static File getCacheDir(Context context) {
        File result;
        if (existsSdcard(context)) {
            result = new File(Environment.getExternalStorageDirectory(),".android/" + context.getPackageName() + "/" + DIR_CACHE);
        } else {
            result = new File(context.getFilesDir(), DIR_CACHE);
        }
        if (result.exists() || result.mkdirs()) {
            return result;
        } else {
            return null;
        }
    }
    public static void clearCacheDir(Context context){
        File result;
        if (existsSdcard(context)) {
            result = new File(Environment.getExternalStorageDirectory(),".android/" + context.getPackageName() );
        } else {
            result = context.getFilesDir();
        }
        if(result.exists()){
            result.delete();
        }
    }

    private static boolean existsSdcard(Context context) {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static File saveBitmap2file(Context context,Bitmap bitmap,String filename){
        FileOutputStream out = null;
        File file = null;
        try {
            File dir = getCacheDir(context);
            if (dir==null){
                throw new FileNotFoundException("文件目录不能为空!");
            }
            file = new File(dir,filename);
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100, out);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(out!=null)
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LogUtils.e("path:"+file.getAbsolutePath());
        return file;
    }
    /**
     * 获取磁盘可用空间
     * @return byte 单位 b
     */
    public static long getDiskAvailableSize(Context context) {
        if (!existsSdcard(context)) return 0;
        File path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
        StatFs stat = new StatFs(path.getAbsolutePath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
        // (availableBlocks * blockSize)/1024 KIB 单位
        // (availableBlocks * blockSize)/1024 /1024 MIB单位
    }
}
