package hotel.convenient.com.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

import butterknife.Bind;
import hotel.convenient.com.R;
import hotel.convenient.com.base.BaseActivity;
import hotel.convenient.com.domain.Bank;
import hotel.convenient.com.domain.BankCard;
import hotel.convenient.com.http.HostUrl;
import hotel.convenient.com.http.HttpUtils;
import hotel.convenient.com.http.RequestParams;
import hotel.convenient.com.http.ResultJson;
import hotel.convenient.com.http.SimpleCallback;
import hotel.convenient.com.view.BankPickerDialog;
import hotel.convenient.com.view.CityPickerDialog;
import hotel.convenient.com.view.LinearLayoutEditTextView;

/**
 * Created by Gyb on 2016/2/18 17:25
 */
public class BindingBankCardActivity extends BaseActivity implements CityPickerDialog.OnCityInfoListener,BankPickerDialog.OnBankInfoListener,View.OnClickListener{
    private LinearLayoutEditTextView name;
    private LinearLayoutEditTextView phone;
    private LinearLayoutEditTextView bankCard;
    private LinearLayoutEditTextView chooseCity;
    private LinearLayoutEditTextView chooseOpenAccountBank;
    private LinearLayoutEditTextView openAccountBranchBank;
    @Bind(R.id.next_confirm)
     Button next_confirm;
    
    private CityPickerDialog cityPickerDialog;
    private BankPickerDialog bankPickerDialog;
    private List<Bank> banks;
    private int selectedBankId;
    private String selectedProvince;
    private String selectCity;
    private SimpleCallback simpleCallback = new SimpleCallback() {
            @Override
            public <T> void simpleSuccess(String url, String result, ResultJson<T> resultJson) {
                if (resultJson.getCode() == CODE_SUCCESS) {
                    JSONObject data = JSONObject.parseObject(result).getJSONObject("data");
                    String real_name = data.getString("real_name");
                    name.setInputText(real_name);
                    banks = JSONObject.parseArray(data.getJSONArray("bank").toString(), Bank.class);
                    addListener();
                    bankPickerDialog.setBanks(banks);
                } else {
                    showShortToast(resultJson.getMsg());
                } 
            }
    };
    private SimpleCallback simpleCallbackByBind = new SimpleCallback() {
            @Override
            public <T> void simpleSuccess(String url, String result, ResultJson<T> resultJson) {
                if (resultJson.getCode() == CODE_SUCCESS) {
                    showShortToast(resultJson.getMsg());
                    BindingBankCardActivity.this.finish();
                } else {
                    showShortToast(resultJson.getMsg());
                } 
            }
    };

    @Override
    public void initData(Bundle savedInstanceState) {
        showBackPressed();
        setTitle("绑定银行卡");
        findView();
        chooseCity.disableInput();
        chooseOpenAccountBank.disableInput();
        getBankInfoByHttp();
        
    }

    @Override
    public int setLayoutView() {
        return R.layout.binding_bank_card_activity;
    }

    private void addListener() {
        cityPickerDialog = new CityPickerDialog(this);
        bankPickerDialog = new BankPickerDialog(this);
        bankPickerDialog.setOnBankInfoListener(this);
        cityPickerDialog.setOnCityInfoListener(this);
        next_confirm.setOnClickListener(this);
        chooseCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityPickerDialog.showBottomSelectWindow(BindingBankCardActivity.this);
            }
        });
        chooseOpenAccountBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bankPickerDialog.showBottomSelectWindow(BindingBankCardActivity.this);
            }
        });
    }

    public void findView() {
        name = (LinearLayoutEditTextView) findViewById(R.id.name);
        phone = (LinearLayoutEditTextView) findViewById(R.id.phone);
        bankCard = (LinearLayoutEditTextView) findViewById(R.id.bank_card);
        chooseCity = (LinearLayoutEditTextView) findViewById(R.id.choose_city);
        chooseOpenAccountBank = (LinearLayoutEditTextView) findViewById(R.id.choose_open_account_bank);
        openAccountBranchBank = (LinearLayoutEditTextView) findViewById(R.id.open_account_branch_bank);
    }

    @Override
    public void getCityInfo(String province, String city) {
        chooseCity.setInputText(province+"-"+city);
        selectedProvince = province;
        selectCity = city;
    }

   
    
    public void getBankInfoByHttp(){
        RequestParams params = new RequestParams(HostUrl.HOST+HostUrl.URL_GET_BIND_BANK_INFO);
        HttpUtils.get(params,simpleCallback);
    }

    public void bindBankInfoByHttp(){
        if (checkInput()) return;
        RequestParams params = new RequestParams(HostUrl.HOST+HostUrl.URL_BIND_CARD);
        BankCard card = new BankCard();
        card.setUsername(name.getText());
        card.setBank_id(selectedBankId+"");
        card.setBranch(openAccountBranchBank.getText());
        card.setCard(bankCard.getText());
        card.setCity(selectCity);
        card.setMobile_phone(phone.getText());
        card.setProvince(selectedProvince);
        params.addBodyParameter("json",JSONObject.toJSONString(card));
        HttpUtils.post(params,simpleCallbackByBind);
    }

    private boolean checkInput() {
        String bankCardText = bankCard.getText();
        if(isEmpty(bankCardText)){
            showShortToast("银行卡号不能为空");
            return true;
        }
        String phoneText = phone.getText();
        if(isEmpty(phoneText)){
            showShortToast("手机号不能为空");
            return true;
        }
        if(phoneText.length()!=11||!bankCardText.matches("\\d+")){
            showShortToast("手机号格式错误");
            return true;
        }
        if(bankCardText.length()<12||!bankCardText.matches("\\d+")){
            showShortToast("银行卡格式错误");
            return true;
        }
        if(isEmpty(selectedProvince)||isEmpty(selectCity)){
            showShortToast("请选择省市");
            return true;
        }
        if(isEmpty(openAccountBranchBank.getText())){
            showShortToast("开户支行不能为空");
            return true;
        }
        if(selectedBankId==0){
            showShortToast("请选择开户银行");
            return true;
        }
        return false;
    }


    @Override
    public void getBankInfo(String bankName, int postion) {
        chooseOpenAccountBank.setInputText(bankName);
        selectedBankId = banks.get(postion).getId();
    }

    @Override
    public void onClick(View v) {
        bindBankInfoByHttp();
    }
}
