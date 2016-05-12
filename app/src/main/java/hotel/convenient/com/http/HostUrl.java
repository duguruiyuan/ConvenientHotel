package hotel.convenient.com.http;

/**
 * Created by Gyb on 2015/11/30 15:45
 */
public class HostUrl {
//    public static final String HOST = "http://192.168.0.110:8080/tij2";
//    public static final String HOST = "http://192.168.0.104:8080/tij2";
    public static final String HOST = "http://172.16.7.211:8080/tij2";
    /**
     * 1：登陆接口
     方式: post
     接口：/app/login/login
     接口说明：用户登陆接口
     传入：[{“username”用户名, “password”:密码，“client_type”：设备类型，}]
     返回：{msg”:错误信息，“code”:错误码,”｝
     */
    public static final String URL_LOGIN = "/dealer/login";
    public static final String URL_LOGOUT = "/dealer/logout";
    /**
     * 手机注册验证码
     * 传入：[{“mobile_phone”手机号码,}]
     返回：{msg”:错误信息，“code”:错误码,”｝
     一天三次
     */
    public static final String URL_GET_CODE = "/dealer/getCheckCode";
    /**
     * 方式: post
     接口说明：忘记登录密码--验证手机
     传入：[{“mobile_phone”电话号码, “vCode”:验证码，}]
     返回：{msg”:错误信息，“code”:错误码,”｝
     */
    public static final String URL_CHECK_PHONE = "/dealer/reset_login";

    /**
     * 重置登陆密码   找回密码时使用   需要先验证手机
     */
    public static final String URL_RESET_PASSWORD = "/dealer/reset_login_pwd";
    /**
     * 方式:post
     接口说明：手机注册
     传入：[{“mobile_phone”手机号码,“password”密码, “verify_password”确认密码，“code”验证码，“username”用户名,“client_type”设备类型“referee_id”推荐人id,}]
     返回：{msg”:错误信息，“code”:错误码,”｝
     */
    public static final String URL_REGISTER = "/dealer/register";

    public static final String URL_GET_ROOM_INFO = "/dealer/get_room_info";

    /**
     * 发布房间
     */
    public static final String URL_POST_PUBLISH_ROOM = "/dealer/post_publish_room";
    /**
     * 获取发布房间列表
     */
    public static final String URL_GET_PUBLISH_INFO = "/dealer/get_publish_info";
    /**
     * 获取离我最近的发布房间列表
     */
    public static final String URL_GET_PUBLISH_INFO_LAT_LNG = "/dealer/get_room_info_by_lat_lng";
    /**
     * 删除发布信息
     */
    public static final String URL_REMOVE_PUBLISH = "/dealer/remove_publish";
    /**
     * 检查预约日期是否冲突
     */
    public static final String URL_CHECK_ORDER_DATE = "/dealer/check_order_data";
    /**
     * 发起预约
     */
    public static final String URL_CONFIRM_ORDER = "/dealer/confirm_order";
    /**
     * 实名认证
     */
    public static final String URL_CHECK_NAME = "/dealer/check_name";
    /**
     * 获取银行卡绑定必要信息   get
     */
    public static final String URL_GET_BIND_BANK_INFO = "/dealer/get_bank_info";
    /**
     * 绑定银行卡    post
     */
    public static final String URL_BIND_CARD = "/dealer/bind_bank_card";
    /**
     * 得到银行卡信息
     */
    public static final String GET_BAND_DETAIL_INFO = "/dealer/get_band_detail_info";
    /**
     * 得到用户信息
     */
    public static final String URL_GET_USER_INFO = "/dealer/get_user_info";
    /**
     * 得到用户头像url
     */
    public static final String URL_SET_HEAD_IMAGE = "/dealer/setHeadimage";
    /**
     * 得到站内信
     */
    public static final String URL_GET_MESSAGE = "/dealer/get_message";
    /**
     * 得到站内信未读数
     */
    public static final String URL_GET_NOT_READ_MESSAGE = "/dealer/get_not_read_message";
    /**
     * 站内信设置为已读
     */
    public static final String URL_SET_STATUS_MESSAGE_TO_READ = "/dealer/read_message";
}
