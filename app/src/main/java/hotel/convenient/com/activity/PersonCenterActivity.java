package hotel.convenient.com.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.UUID;

import butterknife.Bind;
import butterknife.OnClick;
import hotel.convenient.com.R;
import hotel.convenient.com.base.BaseActivity;
import hotel.convenient.com.domain.Dealer;
import hotel.convenient.com.http.HostUrl;
import hotel.convenient.com.http.HttpUtils;
import hotel.convenient.com.http.RequestParams;
import hotel.convenient.com.http.ResultJson;
import hotel.convenient.com.http.SimpleCallback;
import hotel.convenient.com.utils.FileUtils;
import hotel.convenient.com.utils.ImageUtils;
import hotel.convenient.com.utils.LogUtils;
import hotel.convenient.com.utils.PhotoUtils;
import hotel.convenient.com.utils.PreferenceUtils;
import hotel.convenient.com.view.CircleImageView;
import hotel.convenient.com.view.LinearLayoutMenu;

/**
 * 登录的activity
 * Created by cwy on 2015/11/30 10:00
 */
public class PersonCenterActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.ll_telephone)
    LinearLayoutMenu ll_telephone;
    @Bind(R.id.ll_bank_card)
     LinearLayoutMenu ll_bank_card;
    @Bind(R.id.ll_id)
     LinearLayoutMenu ll_id;
    @Bind(R.id.ll_real_name)
     LinearLayoutMenu ll_real_name;

    @Bind(R.id.tv_account_name)
     TextView tv_account_name;
    @Bind(R.id.tv_add_time)
     TextView tv_add_time;
    @Bind(R.id.iv_header)
    CircleImageView iv_header;
    @Bind(R.id.ll_head_image)
    RelativeLayout ll_head_image;
    private Dealer personData;
    private View loadAlertDialog;
    private TextView load_choose_local_image;
    private TextView load_photograph;
    private final int IMAGE_LOCAL_CODE = 10;
    private final int IMAGE_PHOTOGRAPH_CODE = 11;
    public static final int TAG_CROP = 15;
    private Uri imageUri;
    private File file;
    @Override
    public void initData(Bundle savedInstanceState) {
        setTitle("个人资料");
        showBackPressed();//显示返回按钮
        RequestParams rq=new RequestParams(HostUrl.HOST+HostUrl.URL_GET_USER_INFO);
        HttpUtils.get(rq, new SimpleCallback() {
            @Override
            public <T> void simpleSuccess(String url, String result, ResultJson<T> resultJson) {
               if(resultJson.isSuccess()) {
                   JSONObject jsonObject= JSONObject.parseObject(result);
                    JSONObject dataJson = jsonObject.getJSONObject("data");
                    personData = JSONObject.parseObject(dataJson.toJSONString(), Dealer.class);
                    setData(personData);
                }
                PersonCenterActivity.this.showShortToast(resultJson.getMsg());
            }
        });
    }

    @Override
    public int setLayoutView() {
        return R.layout.person_center;
    }


    private void setData(Dealer personData){
        tv_account_name.setText(personData.getNickname());
        tv_add_time.setText(personData.getRegister_time());
        ll_telephone.setMsg(personData.getPhonenumber());
        String url = HostUrl.HOST + "/" + personData.getImg_dir() + personData.getHead_image();
        LogUtils.e("url:"+url);
        ImageUtils.setImage(iv_header, url,R.drawable.mei_zi);
        if(isEmpty(personData.getName())){
            ll_id.setMsg("未添加");
            ll_real_name.setMsg("无");
            ll_real_name.setArrowVisiable(false);
            ll_id.setArrowVisiable(true);
            ll_id.setClickable(true);
        }else {
            ll_id.setMsg(personData.getId_card());
            ll_real_name.setMsg(personData.getName());
            ll_real_name.setArrowVisiable(false);
            ll_id.setArrowVisiable(false);
            ll_id.setClickable(false);
        }

        if(isEmpty(personData.getBank_card())){
            ll_bank_card.setMsg("未添加");
            ll_bank_card.setArrowVisiable(true);
            ll_bank_card.setClickable(true);
        }else {
            ll_bank_card.setMsg(personData.getBank_card());
            ll_bank_card.setArrowVisiable(false);
            ll_bank_card.setClickable(false);
        }
        initLoadDialog();
    }
    private void logout(){
        RequestParams params = new RequestParams(HostUrl.HOST+HostUrl.URL_LOGOUT);
        HttpUtils.get(params, new SimpleCallback() {
            @Override
            public <T> void simpleSuccess(String url, String result, ResultJson<T> resultJson) {
                if (resultJson.getCode() == CODE_SUCCESS) {
                    PreferenceUtils.removeLoginFlag(PersonCenterActivity.this);
                    skipActivity(MainActivity.class);
                }
                showShortToast(resultJson.getMsg());
            }
        });
    }
    private void initLoadDialog() {
        loadAlertDialog = LayoutInflater.from(this).inflate(R.layout.load_image_alert_dialog,null,false);
        load_choose_local_image = (TextView) loadAlertDialog.findViewById(R.id.load_choose_local_image);
        load_photograph = (TextView) loadAlertDialog.findViewById(R.id.load_photograph);
        load_choose_local_image.setOnClickListener(this);
        load_photograph.setOnClickListener(this);
    }
    @OnClick({R.id.bt_logout,R.id.ll_bank_card,R.id.ll_id,R.id.ll_real_name,R.id.ll_head_image})
     void onConfirmClick(View view){
        if(personData==null){
            showShortToast("账号信息获取中,请稍后...");
            return;
        }
        switch (view.getId()){
            case R.id.bt_logout:
                showAlertDialog("是否退出当前账号", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                });
                break;
            case R.id.ll_bank_card:
                skipActivity(AccountBankCardActivity.class,false);
                break;
            case R.id.ll_id:
            case R.id.ll_real_name:
                skipActivity(CheckRealNameActivity.class, false);
                break;
            case R.id.ll_head_image:
                showAlertDialog("设置房间照片","取消",loadAlertDialog);
                file = new File(FileUtils.getCacheDir(this), ""+ UUID.randomUUID()+".jpg");
                imageUri = Uri.fromFile(file);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.load_choose_local_image: //从本地选择图片
                closeDialog();
                PhotoUtils.startOpenImageByLocal(imageUri,this,TAG_CROP);
                break;
            case R.id.load_photograph:  //拍照
                closeDialog();
                PhotoUtils.startOpenImageByPhotograph(imageUri,this,IMAGE_PHOTOGRAPH_CODE);
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {        //此处的 RESULT_OK 是系统自定义得一个常量
            LogUtils.e("ActivityResult resultCode error");
            return;
        }
        //外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
        if (requestCode == IMAGE_LOCAL_CODE||requestCode == IMAGE_PHOTOGRAPH_CODE) {
            PhotoUtils.crop(imageUri,this,TAG_CROP);//开启裁剪程序
        }else if(requestCode == TAG_CROP){//获取裁剪后的正面照片
            // 从剪切图片返回的数据
            Bitmap bitmap = null;
            try {
                if (data.getData() != null) {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                } else {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            
            file = FileUtils.saveBitmap2file(this,bitmap, "head.jpg");
            RequestParams requestParams = new RequestParams(HostUrl.HOST+HostUrl.URL_SET_HEAD_IMAGE);
            requestParams.addBodyParameterOrFile("head_image",file);
            final Bitmap finalBitmap = bitmap;
            HttpUtils.postFile(requestParams, new SimpleCallback() {
                @Override
                public <T> void simpleSuccess(String url, String result, ResultJson<T> resultJson) {
                    if (resultJson.isSuccess()) {
//                        PreferenceUtils.setHeadUrl(this,);
                        Dealer data = JSONObject.parseObject(JSONObject.parseObject(result).getJSONObject("data").toString(), Dealer.class);
                        PreferenceUtils.setHeadUrl(PersonCenterActivity.this, HostUrl.HOST + "/" + data.getImg_dir() + data.getHead_image());
                        iv_header.setImageBitmap(finalBitmap);
                        showShortToast(resultJson.getMsg());
                    } else {
                        showShortToast("网络错误,头像上传失败");
                    } 
                    
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileUtils.clearCacheDir(this);
    }
}
