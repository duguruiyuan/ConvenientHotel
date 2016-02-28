package hotel.convenient.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSONObject;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import hotel.convenient.com.R;
import hotel.convenient.com.base.BaseActivity;
import hotel.convenient.com.domain.Address;
import hotel.convenient.com.domain.PickType;
import hotel.convenient.com.http.HostUrl;
import hotel.convenient.com.http.HttpUtils;
import hotel.convenient.com.http.ResultJson;
import hotel.convenient.com.http.SimpleCallback;
import hotel.convenient.com.utils.LogUtils;
import hotel.convenient.com.view.CityPickerDialog;
import hotel.convenient.com.view.CityPickerDialog.OnCityInfoListener;
import hotel.convenient.com.view.LinearLayoutEditTextView;
import hotel.convenient.com.view.TypePickerDialog;

/**
 * Created by Gyb on 2016/2/28.
 */
@ContentView(R.layout.publish_activity)
public class PublishActivity extends BaseActivity implements OnCityInfoListener,TypePickerDialog.OnTypeInfoListener{
    private LinearLayoutEditTextView name;
    private LinearLayoutEditTextView phone;
    private LinearLayoutEditTextView roomType;
    private LinearLayoutEditTextView chooseCity;
    private LinearLayoutEditTextView chooseCityMap;
    private LinearLayoutEditTextView house_number;
    private Button nextConfirm;



    @ViewInject(R.id.next_confirm)
    private Button next_confirm;

    private CityPickerDialog cityPickerDialog;
    private TypePickerDialog typePickerDialog;
    private String selectedProvince;
    private String selectCity;
    private int selectRoomType;
    private Address address;

    private SimpleCallback simpleCallback = new SimpleCallback(this) {
        @Override
        public <T> void simpleSuccess(String url, String result, ResultJson<T> resultJson) {
            if (resultJson.getCode() == CODE_SUCCESS) {
                addListener();
                String data1 = JSONObject.parseObject(result).getJSONArray("data").toString();
                List<PickType> data = JSONObject.parseArray(data1, PickType.class);
                LogUtils.e(data.toString());
                typePickerDialog.setList(data);
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
        chooseCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityPickerDialog.showBottomSelectWindow(PublishActivity.this);
            }
        });
        roomType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typePickerDialog.showBottomSelectWindow(PublishActivity.this);
            }
        });
        chooseCityMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmpty(selectCity)){
                    showShortToast("请先选择所在地");
                    return;
                }
                Intent intent = new Intent(PublishActivity.this,TakeGoodsSearchAddressActivity.class);
                intent.putExtra(TakeGoodsSearchAddressActivity.CITY,selectCity);
                startActivityForResult(intent,1);
            }
        });
    }
    /**
     * 获取返回的信息 并处理
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                address = (Address) data.getSerializableExtra("address");
                chooseCityMap.setInputText(address.getDistrict()+address.getStreet()+address.getStreetNumber());
            }
        }
    }

    public void findView() {
        name = (LinearLayoutEditTextView) findViewById(R.id.name);
        phone = (LinearLayoutEditTextView) findViewById(R.id.phone);
        roomType = (LinearLayoutEditTextView) findViewById(R.id.room_type);
        chooseCity = (LinearLayoutEditTextView) findViewById(R.id.choose_city);
        chooseCityMap = (LinearLayoutEditTextView) findViewById(R.id.choose_city_map);
        nextConfirm = (Button) findViewById(R.id.next_confirm);
        house_number = (LinearLayoutEditTextView) findViewById(R.id.house_number);
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
}
