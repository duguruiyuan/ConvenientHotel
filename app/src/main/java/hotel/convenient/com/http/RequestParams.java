package hotel.convenient.com.http;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cwy on 2016/3/28 17:53
 */
public class RequestParams {
    private String url;

    private Map<String,String> postParams;
    private List<FileBody> fileList;
    public RequestParams(String url) {
        this.url = url;
        postParams = new HashMap<>();
        fileList = new ArrayList<>();
    }
    public void addBodyParameterOrFile(String name, File value){
        fileList.add(new FileBody(name,value));
    }

    public List<FileBody> getPostFileParams() {
        return fileList;
    }

    public void addBodyParameter(String name, String value){
        postParams.put(name,value);
    }
    public void addQueryStringParameter(String name, String value) {
        if (url.indexOf("?") != -1) {
            url = url + "&" + name + "=" + value;
        } else {
            url = url + "?" + name + "=" + value;
        } 
    }

    public Map<String, String> getPostParams() {
        return postParams;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }
    
    
    
    public class FileBody{
        String key;
        File file;

        public FileBody(String key, File file) {
            this.key = key;
            this.file = file;
        }
    }
}
