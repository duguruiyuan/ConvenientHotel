package hotel.convenient.com.http;

/**
 * 接收网络返回的json数据进行封装
 * Created by cwy on 2015/12/2 11:23
 */
public class ResultJson<T>{
    private String msg;
    private int code;
    private T data;

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
    
        return data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }
    public boolean isSuccess(){
        return getCode()==CommonCallback.CODE_SUCCESS;
    }
}
