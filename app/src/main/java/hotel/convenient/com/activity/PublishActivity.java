package hotel.convenient.com.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import hotel.convenient.com.BuildConfig;
import hotel.convenient.com.R;
import hotel.convenient.com.base.BaseActivity;
import hotel.convenient.com.domain.Address;
import hotel.convenient.com.domain.PickType;
import hotel.convenient.com.http.HostUrl;
import hotel.convenient.com.http.HttpUtils;
import hotel.convenient.com.http.ResultJson;
import hotel.convenient.com.http.SimpleCallback;
import hotel.convenient.com.utils.DensityUtils;
import hotel.convenient.com.utils.FileUtils;
import hotel.convenient.com.utils.LogUtils;
import hotel.convenient.com.view.CityPickerDialog;
import hotel.convenient.com.view.CityPickerDialog.OnCityInfoListener;
import hotel.convenient.com.view.ImageViewCanDelete;
import hotel.convenient.com.view.LinearLayoutEditTextView;
import hotel.convenient.com.view.TypePickerDialog;

/**
 * Created by Gyb on 2016/2/28.
 */
@ContentView(R.layout.publish_activity)
public class PublishActivity extends BaseActivity implements OnCityInfoListener,TypePickerDialog.OnTypeInfoListener,ImageViewCanDelete.OnDeleteClick,View.OnClickListener{
    private LinearLayoutEditTextView name;
    private LinearLayoutEditTextView area;
    private LinearLayoutEditTextView roomType;
    private LinearLayoutEditTextView chooseCity;
    private LinearLayoutEditTextView chooseCityMap;
    private LinearLayoutEditTextView house_number;
    private Button nextConfirm;
    private LinearLayout llAddImageGroup;
    private LinearLayout llAddImage;
    private Button next_confirm;

    private List<ImageViewCanDelete> imageViewCanDeletes = new ArrayList<>();

    private CityPickerDialog cityPickerDialog;
    private TypePickerDialog typePickerDialog;
    private String selectedProvince;
    private String selectCity;
    private int selectRoomType;
    private Address address;
    private View loadAlertDialog;
    private TextView load_choose_local_image;
    private TextView load_photograph;

    private final String IMAGE_TYPE = "image/*";
    private final int IMAGE_LOCAL_CODE = 10;
    private final int IMAGE_PHOTOGRAPH_CODE = 11;
    public static final int TAG_CROP = 15;
    private Uri imageUri;
    private File file;
    private List<File> fileList = new ArrayList<>();

    private SimpleCallback simpleCallback = new SimpleCallback(this) {
        @Override
        public <T> void simpleSuccess(String url, String result, ResultJson<T> resultJson) {
            if (resultJson.getCode() == CODE_SUCCESS) {
                String data1 = JSONObject.parseObject(result).getJSONArray("data").toString();
                List<PickType> data = JSONObject.parseArray(data1, PickType.class);
                LogUtils.e(data.toString());
                typePickerDialog.setList(data);
            } else {
                showShortToast(resultJson.getMsg());
            }
        }
    };
    
    private SimpleCallback simpleCallbackByUpload = new SimpleCallback(this) {
            @Override
            public <T> void simpleSuccess(String url, String result, ResultJson<T> resultJson) {
                if (resultJson.getCode() == CODE_SUCCESS) {
                    JSONObject data = JSONObject.parseObject(result).getJSONObject("data");
                } else {
                    showShortToast(resultJson.getMsg());
                } 
            }
        };
    @Override
    public void initData(Bundle savedInstanceState) {
        showBackPressed();
        setTitle("填写发布信息");
        findView();
        getRoomInfoByHttp();
        chooseCity.disableInput();
        roomType.disableInput();
        chooseCityMap.disableInput();
        addListener();
    }
    public void getRoomInfoByHttp(){
        RequestParams params = new RequestParams(HostUrl.HOST+HostUrl.URL_GET_ROOM_INFO);
        HttpUtils.get(params,simpleCallback);
    }
    private void addListener() {
        cityPickerDialog = new CityPickerDialog(this);
        typePickerDialog = new TypePickerDialog(this);
        cityPickerDialog.setOnCityInfoListener(this);
        typePickerDialog.setOnTypeInfoListener(this);
        chooseCity.setOnClickListener(this);
        roomType.setOnClickListener(this);
        chooseCityMap.setOnClickListener(this);
        llAddImage.setOnClickListener(this);
    }


    public void findView() {
        name = (LinearLayoutEditTextView) findViewById(R.id.name);
        area = (LinearLayoutEditTextView) findViewById(R.id.room_area);
        roomType = (LinearLayoutEditTextView) findViewById(R.id.room_type);
        chooseCity = (LinearLayoutEditTextView) findViewById(R.id.choose_city);
        chooseCityMap = (LinearLayoutEditTextView) findViewById(R.id.choose_city_map);
        nextConfirm = (Button) findViewById(R.id.next_confirm);
        house_number = (LinearLayoutEditTextView) findViewById(R.id.house_number);
        llAddImageGroup = (LinearLayout) findViewById(R.id.ll_add_image_group);
        llAddImage = (LinearLayout) findViewById(R.id.ll_add_image);
        initLoadDialog();
    }

    @Override
    public void getCityInfo(String province, String city) {
        chooseCity.setInputText(province+"-"+city);
        selectedProvince = province;
        selectCity = city;
    }

    @Override
    public void getTypeInfo(String typeName, int position) {
        roomType.setInputText(typeName);
        selectRoomType = position;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_city_map:
                if(isEmpty(selectCity)){
                    showShortToast("请先选择所在地");
                    return;
                }
                Intent intent = new Intent(PublishActivity.this,TakeGoodsSearchAddressActivity.class);
                intent.putExtra(TakeGoodsSearchAddressActivity.CITY,selectCity);
                startActivityForResult(intent,1);
                break;
            case R.id.room_type:
                typePickerDialog.showBottomSelectWindow(PublishActivity.this);
                break;
            case R.id.choose_city:
                cityPickerDialog.showBottomSelectWindow(PublishActivity.this);
                break;
            case R.id.ll_add_image:
                showAlertDialog("设置房间照片","取消",loadAlertDialog);
                createImage();
                file = new File(FileUtils.getCacheDir(this), ""+ UUID.randomUUID()+".jpg");
                imageUri = Uri.fromFile(file);
                break;
            case R.id.load_choose_local_image: //从本地选择图片
                closeDialog();
                startOpenImageByLocal(IMAGE_LOCAL_CODE);
                break;
            case R.id.load_photograph:  //拍照
                closeDialog();
                startOpenImageByPhotograph(IMAGE_PHOTOGRAPH_CODE);
                break;
            case R.id.next_confirm:
                uploadByHttp();
                break;
        }
    }
    private void uploadByHttp(){
        String nameText = name.getText();
        String areaText = area.getText();
        String chooseCityMapText = chooseCityMap.getText();
        String house_numberText = house_number.getText();
        RequestParams params = new RequestParams(HostUrl.HOST+ HostUrl.URL_POST_PUBLISH_ROOM);
        params.addBodyParameter("name",nameText );
        params.addBodyParameter("room_area",areaText );
        params.addBodyParameter("room_type",selectRoomType+"" );
        params.addBodyParameter("room_province",selectedProvince );
        params.addBodyParameter("room_city",selectCity );
        params.addBodyParameter("room_address_detail",chooseCityMapText ); 
        params.addBodyParameter("room_house_number",house_numberText );
        for (int i = 0; i < fileList.size(); i++) {
            params.addBodyParameter("room_image",fileList.get(i) );
        }
        HttpUtils.post(params, simpleCallbackByUpload);
    }
    /**
     * 从本地相册获取图片
     * @return
     */
    public void startOpenImageByLocal(int resultCode){
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
        getAlbum.putExtra("outputX", 300);
        getAlbum.putExtra("outputY", 300);
        getAlbum.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 图片格式
        startActivityForResult(getAlbum, TAG_CROP);
    }
    public void startOpenImageByPhotograph(int resultCode){
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
            startActivityForResult(getImageByCamera, resultCode);
        }else {
            showShortToast("请确认已经插入SD卡");
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {        //此处的 RESULT_OK 是系统自定义得一个常量
            LogUtils.e("ActivityResult resultCode error");
            return;
        }
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                address = (Address) data.getSerializableExtra("address");
                chooseCityMap.setInputText(address.getDistrict()+address.getStreet()+address.getStreetNumber());
            }
        }
        //外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
        if (requestCode == IMAGE_LOCAL_CODE||requestCode == IMAGE_PHOTOGRAPH_CODE) {
            crop(TAG_CROP);//开启裁剪程序
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

            imageViewCanDeletes.get(imageViewCanDeletes.size()-1).setImageBitmap(bitmap);
            File file = FileUtils.saveBitmap2file(this,bitmap, "room"+imageViewCanDeletes.size()+".jpg");
//            File file = FileUtils.saveInputStream2File(this,imageUri,"room"+imageViewCanDeletes.size()+".jpg");
//            try {
//                imageViewCanDeletes.get(imageViewCanDeletes.size()-1).setImageBitmap(BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri)));
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
            fileList.add(file);
        }
    }
    /**
     * 剪切图片
     */
    private void crop(int code) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        // 裁剪图片意图
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 图片格式
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, code);
    }
    private void initLoadDialog() {
        loadAlertDialog = LayoutInflater.from(this).inflate(R.layout.load_image_alert_dialog,null,false);
        load_choose_local_image = (TextView) loadAlertDialog.findViewById(R.id.load_choose_local_image);
        load_photograph = (TextView) loadAlertDialog.findViewById(R.id.load_photograph);
        load_choose_local_image.setOnClickListener(this);
        load_photograph.setOnClickListener(this);
    }
    private void createImage() {
        ImageViewCanDelete child = new ImageViewCanDelete(this);
        child.setTag(imageViewCanDeletes.size());
        child.setOnDeleteClick(this);
        llAddImageGroup.addView(child,imageViewCanDeletes.size());
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) child.getLayoutParams();
        layoutParams.leftMargin = DensityUtils.dip2px(this,8);
        layoutParams.gravity = Gravity.CENTER;
        child.setLayoutParams(layoutParams);
        imageViewCanDeletes.add(child);
        child.setVisibility(View.GONE);
    }

    @Override
    public void onDeleteClick(View view) {
        fileList.remove((int)view.getTag());
        llAddImageGroup.removeView(view);
        imageViewCanDeletes.remove(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileUtils.clearCacheDir(this);
    }
}
