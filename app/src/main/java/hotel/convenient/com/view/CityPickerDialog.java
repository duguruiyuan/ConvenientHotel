package hotel.convenient.com.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import hotel.convenient.com.R;
import hotel.convenient.com.domain.CityModel;
import hotel.convenient.com.domain.ProvinceModel;
import hotel.convenient.com.utils.ShowAnimationDialogUtil;
import hotel.convenient.com.utils.XmlParserHandler;
import hotel.convenient.com.wheel.OnWheelChangedListener;
import hotel.convenient.com.wheel.WheelView;
import hotel.convenient.com.wheel.adapter.ArrayWheelAdapter;

/**
 * Created by Gyb on 2016/2/18 15:50
 */
public class CityPickerDialog extends View implements OnWheelChangedListener {
    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<>();
    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 解析省市区的XML数据
     */
    private String fromId;

    public CityPickerDialog(Context context) {
        super(context);
    }

    public CityPickerDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CityPickerDialog(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void initProvinceData() {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = getContext().getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            // */ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                }
            }
            // */
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }
    private SelectDialog pd;
    private WheelView mViewProvince;
    private WheelView mViewCity;
    
    public interface OnCityInfoListener{
        void getCityInfo(String province, String city);
    }

    private OnCityInfoListener onCityInfoListener;

    public void setOnCityInfoListener(OnCityInfoListener onCityInfoListener) {
        this.onCityInfoListener = onCityInfoListener;
    }


    private void setUpListener() {
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
    }
    private void setUpData() {
        initProvinceData();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter(getContext(),mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        updateCities();
    }
    private void setUpViews(View view) {
        mViewProvince = (WheelView) view.findViewById(R.id.id_province);
        mViewCity = (WheelView) view.findViewById(R.id.id_city);
    }
    // 弹出底部窗
    public void showBottomSelectWindow(Activity activity) {
        View contentView = LayoutInflater.from(getContext()).inflate(
                R.layout.choose_city, null);
        setUpViews(contentView);
        setUpListener();
        setUpData();
        pd = ShowAnimationDialogUtil.showDialog(activity, contentView);// 弹出
        TextView btn_confirm = (TextView) pd.getView().findViewById(
                R.id.tv_take_confirm);
        btn_confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(onCityInfoListener!=null){
                    onCityInfoListener.getCityInfo(mCurrentProviceName,mCurrentCityName);
                }
                pd.cancel();
            }
        });
        TextView tv_take_cancel = (TextView) pd.getView().findViewById(
                R.id.tv_take_cancel);
        tv_take_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                pd.cancel();
            }
        });
    }


    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[] { "" };
        }
        ArrayWheelAdapter<String> stringArrayWheelAdapter = new ArrayWheelAdapter<>(getContext(), cities);
        mViewCity.setViewAdapter(stringArrayWheelAdapter);
        mViewCity.setCurrentItem(0);
        mCurrentCityName = cities[0];
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            int pCurrent = mViewCity.getCurrentItem();
            mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        } 
    }
}
