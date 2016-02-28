package hotel.convenient.com.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Gyb on 2016/2/2 15:10
 */
public class DigestUtils {
    static MessageDigest getDigest(String algorithm){
        try{
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static MessageDigest getMd5Digest(){
        return getDigest("MD5");
    }

    public static byte[] md5(byte[] data){
        return getMd5Digest().digest(data);
    }


    public static String md5Hex(byte[] data){
        return Hex.encodeHexString(md5(data));
    }
}
