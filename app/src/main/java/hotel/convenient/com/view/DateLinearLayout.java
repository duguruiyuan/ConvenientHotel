package hotel.convenient.com.view;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import hotel.convenient.com.R;
import hotel.convenient.com.base.BaseActivity;
import hotel.convenient.com.utils.LogUtils;
import hotel.convenient.com.utils.ToastUtil;

/**
 * Created by cwy on 2016/4/12 10:13
 */
public class DateLinearLayout extends LinearLayout implements View.OnClickListener{
    private TextView start_date;
    private TextView end_date;
    private TextView start_date_info;
    private TextView end_date_info;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private GregorianCalendar endCalendar;
    private GregorianCalendar startCalendar;
    private LinearLayout pickStartDate;
    private LinearLayout pickEndDate;
    private BaseActivity mBaseActivity;
    private TextView tv_count_day;
    
    public interface OnStartDateClickListener{
        void onStartClick(View view,GregorianCalendar startCalendar,GregorianCalendar endCalendar);
    }
    public interface OnEndDateClickListener{
        void onEndClick(View view,GregorianCalendar startCalendar,GregorianCalendar endCalendar);
    }
    private OnStartDateClickListener onStartDateClickListener;
    private OnEndDateClickListener onEndDateClickListener;

    public void setOnStartDateClickListener(OnStartDateClickListener onStartDateClickListener) {
        this.onStartDateClickListener = onStartDateClickListener;
    }

    public void setOnEndDateClickListener(OnEndDateClickListener onEndDateClickListener) {
        this.onEndDateClickListener = onEndDateClickListener;
    }

    public GregorianCalendar getEndCalendar() {
        return endCalendar;
    }

    public GregorianCalendar getStartCalendar() {
        return startCalendar;
    }

    public void setmBaseActivity(BaseActivity mBaseActivity) {
        this.mBaseActivity = mBaseActivity;
    }

    public void setCalendar(GregorianCalendar startCalendar,GregorianCalendar endCalendar) {
        this.startCalendar = startCalendar;
        this.endCalendar = endCalendar;
        setInfo();
    }

    public DateLinearLayout(Context context) {
        super(context);
    }

    public DateLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initListener();
    }

    private void initListener() {
        pickStartDate.setOnClickListener(this);
        pickEndDate.setOnClickListener(this);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.linearlayout_date_item,this);
        pickStartDate = (LinearLayout) view.findViewById(R.id.pick_start_date);
        pickEndDate = (LinearLayout) view.findViewById(R.id.pick_end_date);
        start_date = (TextView) view.findViewById(R.id.start_date);
        end_date = (TextView) view.findViewById(R.id.end_date);
        start_date_info = (TextView) view.findViewById(R.id.start_date_info);
        end_date_info = (TextView) view.findViewById(R.id.end_date_info);
        tv_count_day = (TextView) view.findViewById(R.id.tv_count_day);
        startCalendar = new GregorianCalendar();
        startDatePicker = new DatePicker(context);
        startDatePicker.setCalendarViewShown(false);
        startDatePicker.setMinDate(startCalendar.getTime().getTime());
        endCalendar = new GregorianCalendar();
        endCalendar = new GregorianCalendar(endCalendar.get(Calendar.YEAR),endCalendar.get(Calendar.MONTH),endCalendar.get(Calendar.DAY_OF_MONTH));
        endCalendar.add(GregorianCalendar.DAY_OF_MONTH,1);
        endDatePicker = new DatePicker(context);
        endDatePicker.setCalendarViewShown(false);
        endDatePicker.setMinDate(endCalendar.getTime().getTime());


        setInfo();
    }

    private void setInfo() {
        pickDateInit(startDatePicker,startCalendar);
        pickDateInit(endDatePicker,endCalendar);
        setDateByCalendar(start_date,startCalendar);
        setDateByCalendar(end_date,endCalendar);
        setCountDay();
    }

    private void setCountDay() {
        tv_count_day.setText("共"+getDay(startCalendar,endCalendar)+"天");
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
    public void onClick(final View v) {
        switch (v.getId()){
            case R.id.pick_start_date:
                if(mBaseActivity==null){
                    return;
                }
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
                        setCountDay();
                        if(onStartDateClickListener!=null){
                            onStartDateClickListener.onStartClick(v,startCalendar,endCalendar);
                        }
                    }
                },startDatePicker);
                
                
                break;
            case R.id.pick_end_date:
                if(mBaseActivity==null){
                    return;
                }
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
                        setCountDay();
                        if(onEndDateClickListener!=null){
                            onEndDateClickListener.onEndClick(v,startCalendar,endCalendar);
                        }
                    }
                },endDatePicker);
                
                break;
        }
        
    }
}
