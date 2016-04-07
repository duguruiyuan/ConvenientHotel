package hotel.convenient.com.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.Bind;
import butterknife.OnClick;
import hotel.convenient.com.R;
import hotel.convenient.com.base.BaseFragment;
import hotel.convenient.com.utils.BaiduLocalClient;
import hotel.convenient.com.utils.LogUtils;
import hotel.convenient.com.utils.ToastUtil;
import hotel.convenient.com.view.CityPickerDialog;

/**
 * Created by Gyb on 2016/3/31 14:33
 */
public class MainFragment extends BaseFragment implements BDLocationListener,MainListFragment.GetLocation {
    MainListFragment mainListFragment;
    private double latitude;
    private double longitude;
    private TextView pickCity;
    private TextView pickDescLocal;
    private LinearLayout pickStartDate;
    private LinearLayout pickEndDate;
    private CityPickerDialog cityPickerDialog;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private GregorianCalendar endCalendar;
    private GregorianCalendar startCalendar;
    @Bind(R.id.start_date)
    TextView start_date;
    @Bind(R.id.end_date)
    TextView end_date;
    @Bind(R.id.start_date_info)
    TextView start_date_info;
    @Bind(R.id.end_date_info)
    TextView end_date_info;


    @Override
    public int setLayoutView() {
        return R.layout.main_head_fragment;
    }

    @Override
    public void initData(View view, Bundle savedInstanceState) {
        pickCity = (TextView) view.findViewById(R.id.pick_city);
        pickDescLocal = (TextView) view.findViewById(R.id.pick_desc_local);
        pickStartDate = (LinearLayout) view.findViewById(R.id.pick_start_date);
        pickEndDate = (LinearLayout) view.findViewById(R.id.pick_end_date);
        BaiduLocalClient.getLocaltion(getContext(),this);
        BaiduLocalClient.startGetLocaltion();
        mainListFragment = new MainListFragment();
        mainListFragment.setGetLocation(this);
        getChildFragmentManager().beginTransaction().replace(R.id.dealer_list_fragment, mainListFragment).commitAllowingStateLoss();

        cityPickerDialog = new CityPickerDialog(getContext());
        cityPickerDialog.setOnCityInfoListener(new CityPickerDialog.OnCityInfoListener() {
            @Override
            public void getCityInfo(String province, String city) {
//                LogUtils.e("province:"+province);
//                LogUtils.e("city:"+city);
                pickCity.setText(city);
            }
        });
        startCalendar = new GregorianCalendar();
        startDatePicker = new DatePicker(getContext());
        startDatePicker.setCalendarViewShown(false);
        startDatePicker.setMinDate(startCalendar.getTime().getTime());
        pickDateInit(startDatePicker,startCalendar);
        endCalendar = new GregorianCalendar();
        endCalendar = new GregorianCalendar(endCalendar.get(Calendar.YEAR),endCalendar.get(Calendar.MONTH),endCalendar.get(Calendar.DAY_OF_MONTH));
        endCalendar.add(GregorianCalendar.DAY_OF_MONTH,1);
        endDatePicker = new DatePicker(getContext());
        endDatePicker.setCalendarViewShown(false);
        endDatePicker.setMinDate(endCalendar.getTime().getTime());
        pickDateInit(endDatePicker,endCalendar);
        setDateByCalendar(start_date,startCalendar);
        setDateByCalendar(end_date,endCalendar);
    }
    @OnClick({R.id.pick_city,R.id.pick_desc_local,R.id.pick_start_date,R.id.pick_end_date})
    void onPickClick(View view){
        switch (view.getId()){
            case R.id.pick_city:
                cityPickerDialog.showSelectAlert(mBaseActivity);
                break;
            case R.id.pick_desc_local:
                
                break;
            case R.id.pick_start_date:
                mBaseActivity.showAlertDialog("选择入住日期", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = startDatePicker.getYear();
                        int month = startDatePicker.getMonth();
                        int day = startDatePicker.getDayOfMonth();
                        GregorianCalendar calendar = new GregorianCalendar(year, month, day);
                        startCalendar.set(year, month, day);
                        if(getDay(startCalendar,endCalendar)<=0){
                            calendar.add(Calendar.DAY_OF_MONTH,1);
                            LogUtils.e("改变离店日期");
                            endCalendar = new GregorianCalendar(
                                    calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                            pickDateInit(endDatePicker,endCalendar);
                            changeDate(end_date, endCalendar, end_date_info);
                        }
                        changeDate(start_date, startCalendar, start_date_info);
                    }
                },startDatePicker);
                break;
            case R.id.pick_end_date:
                mBaseActivity.showAlertDialog("选择离店日期", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = endDatePicker.getYear();
                        int month = endDatePicker.getMonth();
                        int day = endDatePicker.getDayOfMonth();
                        GregorianCalendar calendar = new GregorianCalendar(year, month, day);
                        if(getDay(startCalendar,calendar)<1){
                            ToastUtil.showLongToast("住店时间必须大于一天");
                            return;
                        }
                        endCalendar.set(year, month, day);
                        changeDate(end_date, endCalendar, end_date_info);
                    }
                },endDatePicker);
                break;
        }
    }

    private void changeDate(TextView date, GregorianCalendar calendar, TextView info) {
        setDateByCalendar(date, calendar);
        setDateInfoByCalendar(info, calendar);
    }
    private void pickDateInit(DatePicker picker,GregorianCalendar calendar) {
        picker.init(calendar.get(GregorianCalendar.YEAR), calendar.get(GregorianCalendar.MONTH),
                calendar.get(GregorianCalendar.DAY_OF_MONTH), null);
    }

    private void setDateByCalendar(TextView textView,GregorianCalendar calendar) {
        textView.setText(new SimpleDateFormat("MM-dd").format(calendar.getTime()));
    }
    private void setDateInfoByCalendar(TextView textView,GregorianCalendar calendar) {
        GregorianCalendar today = new GregorianCalendar();
        int day = getDay(today,calendar);
        textView.setVisibility(View.VISIBLE);
        if(day==0){
            textView.setText("今天");
            return;
        }else if(day==1){
            textView.setText("明天");
            return;
        }else if(day==2){
            textView.setText("后天");
            return;
        }
        textView.setVisibility(View.GONE);
    }

    private int getDay(GregorianCalendar calendar1, GregorianCalendar calendar2) {
        long time = calendar2.getTime().getTime()-calendar1.getTime().getTime();
        int day = (int) (time/(1000*3600*24));
        if(time>0&&time%(1000*3600*24)>0){
            day = day+1;
        }
        return day;
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (bdLocation != null) {
            latitude = bdLocation.getLatitude();
            longitude = bdLocation.getLongitude();
            pickCity.setText(bdLocation.getAddress().city);
            mainListFragment.setLatLng(latitude,longitude);
        } else {
            mBaseActivity.showShortToast("定位失败...");
        }
    }

    @Override
    public void getLocation() {
        BaiduLocalClient.startGetLocaltion();
    }
}
