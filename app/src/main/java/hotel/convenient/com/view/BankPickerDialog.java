package hotel.convenient.com.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import hotel.convenient.com.R;
import hotel.convenient.com.domain.Bank;
import hotel.convenient.com.utils.ShowAnimationDialogUtil;
import hotel.convenient.com.wheel.OnWheelChangedListener;
import hotel.convenient.com.wheel.WheelView;
import hotel.convenient.com.wheel.adapter.ArrayWheelAdapter;

/**
 * Created by Gyb on 2016/2/18 15:50
 */
public class BankPickerDialog extends View implements OnWheelChangedListener {
    /**
     * 所有银行
     */
    protected String[] banks;
    /**
     * 当前银行的名称
     */
    protected String mCurrentBankName;

    public BankPickerDialog(Context context) {
        super(context);
    }

    public BankPickerDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BankPickerDialog(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private SelectDialog pd;
    private WheelView mViewBank;
    public interface OnBankInfoListener{
        void getBankInfo(String bankName, int postion);
    }

    private OnBankInfoListener onBankInfoListener;

    public void setOnBankInfoListener(OnBankInfoListener onBankInfoListener) {
        this.onBankInfoListener = onBankInfoListener;
    }


    public void setBanks(String[] banks) {
        this.banks = banks;
    }
    public void setBanks(List<Bank> list) {
        this.banks = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            Bank bank = list.get(i);
            banks[i] = bank.getBank_name();
        }
    }
    

    private void setUpListener() {
        // 添加change事件
        mViewBank.addChangingListener(this);
        // 添加change事件
    }
    private void setUpData() {
        mViewBank.setViewAdapter(new ArrayWheelAdapter(getContext(), banks));
        // 设置可见条目数量
        mViewBank.setVisibleItems(7);
        updateBanks();
    }
    private void setUpViews(View view) {
        mViewBank = (WheelView) view.findViewById(R.id.id_bank);
    }
    // 弹出底部窗
    public void showBottomSelectWindow(Activity activity) {
        View contentView = LayoutInflater.from(getContext()).inflate(
                R.layout.choose_bank, null);
        setUpViews(contentView);
        setUpListener();
        setUpData();
        pd = ShowAnimationDialogUtil.showDialog(activity, contentView);// 弹出
        TextView btn_confirm = (TextView) pd.getView().findViewById(
                R.id.tv_take_confirm);
        btn_confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(onBankInfoListener!=null){
                    onBankInfoListener.getBankInfo(mCurrentBankName,mViewBank.getCurrentItem());
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
     * 获取银行信息
     */
    private void updateBanks() {
        int pCurrent = mViewBank.getCurrentItem();
        mCurrentBankName = banks[pCurrent];
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewBank) {
            updateBanks();
        } 
    }
}
