package hotel.convenient.com.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import hotel.convenient.com.R;
import hotel.convenient.com.domain.PickType;
import hotel.convenient.com.utils.ShowAnimationDialogUtil;
import hotel.convenient.com.wheel.OnWheelChangedListener;
import hotel.convenient.com.wheel.WheelView;
import hotel.convenient.com.wheel.adapter.ArrayWheelAdapter;

/**
 * Created by Gyb on 2016/2/18 15:50
 */
public class TypePickerDialog extends View implements OnWheelChangedListener {
    /**
     * 所有类型
     */
    protected String[] datas;
    /**
     * 当前类型
     */
    protected String mCurrentName;

    public TypePickerDialog(Context context) {
        super(context);
    }

    public TypePickerDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TypePickerDialog(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private SelectDialog pd;
    private WheelView mViewType;
    public interface OnTypeInfoListener{
        void getTypeInfo(String typeName, int position);
    }

    private OnTypeInfoListener onTypeInfoListener;

    public void setOnTypeInfoListener(OnTypeInfoListener onTypeInfoListener) {
        this.onTypeInfoListener = onTypeInfoListener;
    }
    public void setList(List<PickType> list) {
        this.datas = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            PickType type = list.get(i);
            datas[i] = type.getType();
        }
    }


    private void setUpListener() {
        // 添加change事件
        mViewType.addChangingListener(this);
        // 添加change事件
    }
    private void setUpData() {
        mViewType.setViewAdapter(new ArrayWheelAdapter(getContext(), datas));
        // 设置可见条目数量
        mViewType.setVisibleItems(7);
        updateTypes();
    }
    private void setUpViews(View view) {
        mViewType = (WheelView) view.findViewById(R.id.id_type);
    }
    // 弹出底部窗
    public void showBottomSelectWindow(Activity activity) {
        View contentView = LayoutInflater.from(getContext()).inflate(
                R.layout.choose_type, null);
        setUpViews(contentView);
        setUpListener();
        setUpData();
        pd = ShowAnimationDialogUtil.showDialog(activity, contentView);// 弹出
        TextView btn_confirm = (TextView) pd.getView().findViewById(
                R.id.tv_take_confirm);
        btn_confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(onTypeInfoListener!=null){
                    onTypeInfoListener.getTypeInfo(mCurrentName, mViewType.getCurrentItem());
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
     * 获取信息
     */
    private void updateTypes() {
        int pCurrent = mViewType.getCurrentItem();
        if (datas == null) {
            mCurrentName = "";
        } else {
            mCurrentName = datas[pCurrent];
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewType) {
            updateTypes();
        } 
    }


}
