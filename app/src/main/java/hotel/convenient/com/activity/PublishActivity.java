package hotel.convenient.com.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import hotel.convenient.com.R;
import hotel.convenient.com.base.BaseActivity;
import hotel.convenient.com.domain.Address;
import hotel.convenient.com.domain.PickType;
import hotel.convenient.com.domain.Publish;
import hotel.convenient.com.http.HostUrl;
import hotel.convenient.com.http.HttpUtils;
import hotel.convenient.com.http.RequestParams;
import hotel.convenient.com.http.ResultJson;
import hotel.convenient.com.http.SimpleCallback;
import hotel.convenient.com.utils.DensityUtils;
import hotel.convenient.com.utils.FileUtils;
import hotel.convenient.com.utils.LogUtils;
import hotel.convenient.com.utils.PhotoUtils;
import hotel.convenient.com.utils.TextUtils;
import hotel.convenient.com.view.CityPickerDialog;
import hotel.convenient.com.view.CityPickerDialog.OnCityInfoListener;
import hotel.convenient.com.view.ImageViewCanDelete;
import hotel.convenient.com.view.LinearLayoutEditTextView;
import hotel.convenient.com.view.TypePickerDialog;

/**
 * Created by Gyb on 2016/2/28.
 */
public class PublishActivity extends BaseActivity implements OnCityInfoListener,TypePickerDialog.OnTypeInfoListener,ImageViewCanDelete.OnDeleteClick,View.OnClickListener{
    private LinearLayoutEditTextView name;
    private LinearLayoutEditTextView area;
    private LinearLayoutEditTextView roomType;
    private LinearLayoutEditTextView chooseCity;
    private LinearLayoutEditTextView chooseCityMap;
    private LinearLayoutEditTextView house_number;
    private LinearLayoutEditTextView room_description;
    private Button nextConfirm;
    private LinearLayout llAddImageGroup;
    private LinearLayout llAddImage;
    private LinearLayoutEditTextView roomPrice;
    private LinearLayoutEditTextView choosePublishEndDate;
    private List<ImageViewCanDelete> imageViewCanDeletes = new ArrayList<>();
    private CityPickerDialog cityPickerDialog;
    private TypePickerDialog typePickerDialog;
    private String selectedProvince;
    private String selectCity;
    private int selectRoomType=-1;
    private Address address;
    private View loadAlertDialog;
    private TextView load_choose_local_image;
    private TextView load_photograph;
    private DatePicker datePicker;

    
    private final int IMAGE_LOCAL_CODE = 10;
    private final int IMAGE_PHOTOGRAPH_CODE = 11;
    public static final int TAG_CROP = 15;
    private Uri imageUri;
    private File file;
    private Calendar today;
    private SparseArray<File> fileSparseArray = new SparseArray<>();
    private SimpleCallback simpleCallback = new SimpleCallback() {
        @Override
        public <T> void simpleSuccess(String url, String result, ResultJson<T> resultJson) {
            if (resultJson.getCode() == CODE_SUCCESS) {
                String data1 = JSONObject.parseObject(result).getJSONArray("data").toString();
                List<PickType> data = JSONObject.parseArray(data1, PickType.class);
                typePickerDialog.setList(data);
            } else {
                showShortToast(resultJson.getMsg());
            }
        }
    };
    
    private SimpleCallback simpleCallbackByUpload = new SimpleCallback() {
            @Override
            public <T> void simpleSuccess(String url, String result, ResultJson<T> resultJson) {
                if (resultJson.getCode() == CODE_SUCCESS) {
                    showShortToast(resultJson.getMsg());
                    PublishActivity.this.skipActivity(MainActivity.class,true,MainActivity.FLAG_SKIP,0);
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
        addListener();
        getRoomInfoByHttp();
        chooseCity.disableInput();
        roomType.disableInput();
        chooseCityMap.disableInput();
        choosePublishEndDate.disableInput();
    }

    @Override
    public int setLayoutView() {
        return R.layout.publish_activity;
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
        nextConfirm.setOnClickListener(this);
        choosePublishEndDate.setOnClickListener(this);
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
        roomPrice = (LinearLayoutEditTextView) findViewById(R.id.room_price);
        choosePublishEndDate = (LinearLayoutEditTextView) findViewById(R.id.choose_publish_end_date);
        room_description = (LinearLayoutEditTextView) findViewById(R.id.room_description);
        initLoadDialog();
        today = new GregorianCalendar();
        today.add(Calendar.WEEK_OF_MONTH,1);
        //时间选择器
        datePicker = new DatePicker(this);
        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                
            }
        });
        datePicker.setCalendarViewShown(false);
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
            case R.id.next_confirm:
                uploadByHttp();
                break;
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
                PhotoUtils.startOpenImageByLocal(imageUri,this,TAG_CROP);
                break;
            case R.id.load_photograph:  //拍照
                closeDialog();
                PhotoUtils.startOpenImageByPhotograph(imageUri,this,IMAGE_PHOTOGRAPH_CODE);
                break;
            case R.id.choose_publish_end_date:  //选择日期
                
                showAlertDialog("选择结束日期", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth();
                        int day = datePicker.getDayOfMonth();
                        GregorianCalendar calendar = new GregorianCalendar(year, month, day);
                        today = new GregorianCalendar();
                        today.add(Calendar.DAY_OF_MONTH,6);
                        if(today.after(calendar)){
                            showShortToast("发布的持续时间必须大于7天");
                            return;
                        }
                        choosePublishEndDate.setInputText(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
                    }
                },datePicker);
                break;
            
        }
    }
    private void uploadByHttp(){
        String nameText = name.getText();
        String areaText = area.getText();
        String roomPriceText = roomPrice.getText();
        String chooseCityMapText = chooseCityMap.getText();
        String house_numberText = house_number.getText();
        String choosePublishEndDateText = choosePublishEndDate.getText();
        if(isEmpty(nameText)){
            showShortToast("房子名称不能为空");
            return;
        }
        if(isEmpty(areaText)){
            showShortToast("房间面积不能为空");
            return;
        }
        if(!TextUtils.checkMoney(areaText)){
            showShortToast("房间面积格式错误");
            return;
        }
        if(isEmpty(roomPriceText)){
            showShortToast("房间价格不能为空");
            return;
        }
        if(!TextUtils.checkMoney(roomPriceText)){
            showShortToast("房间价格格式错误");
            return;
        }
        if(selectRoomType==-1){
            showShortToast("请选择房间类型");
            return;
        }
        if(isEmpty(selectedProvince)){
            showShortToast("请选择房间所在地");
            return;
        }
        if(isEmpty(chooseCityMapText)){
            showShortToast("请选择详细地址");
            return;
        }
        if(isEmpty(house_numberText)){
            showShortToast("门派号不能为空");
            return;
        }
        if(isEmpty(choosePublishEndDateText)){
            showShortToast("结束日期不能为空");
            return;
        }
        if(fileSparseArray.size()<1){
            showShortToast("至少需要上传一张房间照片");
            return;
        }
        RequestParams params = new RequestParams(HostUrl.HOST+ HostUrl.URL_POST_PUBLISH_ROOM);
        Publish publish = new Publish();
        publish.setName(nameText);
        publish.setRoom_area(areaText);
        publish.setRoom_price(roomPriceText);
        publish.setRoom_type(selectRoomType+"");
        publish.setRoom_province(selectedProvince);
        publish.setRoom_city(selectCity);
        publish.setRoom_address_detail(chooseCityMapText);
        publish.setRoom_house_number(house_numberText);
        publish.setPublish_end_time(choosePublishEndDateText);
        publish.setLatitude(address.getLatitude());
        publish.setLongitude(address.getLongitude());
        publish.setDescription(room_description.getText());
        params.addBodyParameter("json",JSONObject.toJSONString(publish));
        for (int i = 0; i < fileSparseArray.size(); i++) {
            params.addBodyParameterOrFile("room_image",fileSparseArray.valueAt(i) );
        }
        HttpUtils.postFile(params, simpleCallbackByUpload);
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
                chooseCityMap.setInputText(address.getDistrict()+address.getStreet());
                house_number.setText(address.getStreetNumber());
            }
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

            ImageViewCanDelete imageViewCanDelete = imageViewCanDeletes.get(imageViewCanDeletes.size() - 1);
            imageViewCanDelete.setImageBitmap(bitmap);
            File file = FileUtils.saveBitmap2file(this,bitmap, "room"+imageViewCanDeletes.size()+".jpg");
            fileSparseArray.put(imageViewCanDeletes.size() - 1,file);
        }
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
        fileSparseArray.remove((int)view.getTag());
        llAddImageGroup.removeView(view);
        imageViewCanDeletes.remove(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileUtils.clearCacheDir(this);
    }
}
