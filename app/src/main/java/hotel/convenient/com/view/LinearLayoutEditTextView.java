package hotel.convenient.com.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import hotel.convenient.com.R;
import hotel.convenient.com.utils.DensityUtils;

/**
 * 自定义输入框  通用
 * Created by cwy on 2015/12/11 16:06
 */
public class LinearLayoutEditTextView extends LinearLayout {
    private EditText input_value;  //输入框
    private TextView input_name;  //提示信息
    private ImageView input_image; //提示图标
    private TextView input_button;  //按钮
    private Button input_cancel;  //清除按钮
    private LinearLayout input_item; //整个条目
    private View line_top;        //上分隔线
    private View line_bottom;  //下分隔线
    private boolean isCancel;
    private LinearLayout input_ll_cancel;//为了增大点击区域
    private String btName; //按钮的名字
    private ImageView input_edit_image;
    private TextView input_text;
    private OnClickListener buttonOnclicklistener;
    private int seconds;
    public static final int CHANGE_BUTTON_NAME=0;
    private ScheduledExecutorService service;
    private android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(seconds<1){
                service.shutdown();
                input_button.setText((String) input_button.getTag());
                input_button.setOnClickListener(buttonOnclicklistener);
                return;
            }
            if(msg.what==CHANGE_BUTTON_NAME){
                seconds--;
                input_button.setText("("+seconds+")"+btName);
            }
        }
    };

    public void setButtonOnClickListener(OnClickListener buttonOnClickListener) {
        if(!TextUtils.isEmpty(btName)){
            this.buttonOnclicklistener = buttonOnClickListener;
            input_button.setOnClickListener(buttonOnclicklistener);
        }
    }
    public void setHint(String tip){
        if(!TextUtils.isEmpty(tip)){
            input_value.setHint(tip);
        }
    }

    /**
     * 设置按钮显示的名称 且不可点击并持续一段时间
     * @param name  按纽名称
     * @param duration  不可点击剩余时间
     */
    public void startChangeButtonName(String name,int duration){
        if(TextUtils.isEmpty(name)){
            return;
        }
        seconds = duration;
        btName = name;
        input_button.setTag(input_button.getText());
        input_button.setOnClickListener(null);
        service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(CHANGE_BUTTON_NAME);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public LinearLayoutEditTextView(Context context) {
        super(context);
    }
    public LinearLayoutEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LinearLayoutEditText);
        Drawable image = a.getDrawable(R.styleable.LinearLayoutEditText_nameIcon);
        if(image==null){
            input_image.setVisibility(GONE);
        }else {
            input_image.setImageDrawable(image);
            input_name.setVisibility(GONE);
        }

        String name = a.getString(R.styleable.LinearLayoutEditText_text_name);
        if (TextUtils.isEmpty(name)){
            input_name.setVisibility(GONE);
        }else {
            input_name.setText(name);
            input_name.setVisibility(VISIBLE);
            //得到的是像素
            float nameSize = a.getDimension(R.styleable.LinearLayoutEditText_name_text_size, DensityUtils.sp2px(getContext(), 14));
            nameSize = DensityUtils.px2sp(getContext(),nameSize);
            input_name.setTextSize(nameSize);
            input_name.setTextColor(a.getColor(R.styleable.LinearLayoutEditText_name_text_color, Color.BLACK));
            float textWidth = a.getDimension(R.styleable.LinearLayoutEditText_text_name_width, 0);
            if(textWidth!=0){
                input_name.setWidth((int) textWidth);
            }
        }
        input_value.setHint(a.getString(R.styleable.LinearLayoutEditText_edit_text_hint));
        input_text.setText(a.getString(R.styleable.LinearLayoutEditText_edit_text_hint));
        String editText = a.getString(R.styleable.LinearLayoutEditText_edit_text);
        input_value.setTextColor(a.getColor(R.styleable.LinearLayoutEditText_edit_text_color,getResources().getColor(R.color.deep_black_text_color_more)));
        input_text.setTextColor(a.getColor(R.styleable.LinearLayoutEditText_edit_text_color,getResources().getColor(R.color.deep_black_text_color_more)));
        setInputText(editText);
        float editSize = a.getDimension(R.styleable.LinearLayoutEditText_edit_text_size, DensityUtils.sp2px(getContext(), 14));
        editSize = DensityUtils.px2sp(getContext(),editSize);
        input_value.setTextSize(editSize);
        input_value.setHintTextColor(a.getColor(R.styleable.LinearLayoutEditText_edit_hint_color, getContext().getResources().getColor(R.color.loginPageTextColor)));

        btName = a.getString(R.styleable.LinearLayoutEditText_button_name);
        if (TextUtils.isEmpty(btName)) {
            input_button.setVisibility(GONE);
        } else {
            input_button.setText(btName);
            input_button.setVisibility(VISIBLE);
        }
        
        input_item.setBackgroundColor(a.getColor(R.styleable.LinearLayoutEditText_item_background,Color.WHITE));
        input_value.setBackgroundColor(a.getColor(R.styleable.LinearLayoutEditText_item_background, Color.WHITE));

        isCancel = a.getBoolean(R.styleable.LinearLayoutEditText_isCancel, false);
        if(isCancel){
            input_ll_cancel.setVisibility(GONE);
            input_value.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (TextUtils.isEmpty(s)) {
                        input_ll_cancel.setVisibility(GONE);
                    } else {
                        input_ll_cancel.setVisibility(VISIBLE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    
                }
            });
            input_ll_cancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    input_value.setText("");
                    input_ll_cancel.setVisibility(GONE);
                }
            });
        }else {
            input_ll_cancel.setVisibility(GONE);
        }
        
        
        if(a.getBoolean(R.styleable.LinearLayoutEditText_showBottomLine,true)){
            line_bottom.setVisibility(VISIBLE);
        }else {
            line_bottom.setVisibility(GONE);
        }
        if(a.getBoolean(R.styleable.LinearLayoutEditText_showTopLine,true)){
            line_top.setVisibility(VISIBLE);
        }else {
            line_top.setVisibility(GONE);
        }
        
        if(a.getBoolean(R.styleable.LinearLayoutEditText_isPassword,false)){//设置为密码输入
            input_value.setTransformationMethod(PasswordTransformationMethod.getInstance()); 
        }
        
        if(a.getBoolean(R.styleable.LinearLayoutEditText_hideEditText,false)){
            input_value.setVisibility(GONE);
        }
        Drawable backgroundDrawable = a.getDrawable(R.styleable.LinearLayoutEditText_backgroundDrawable);
        if(backgroundDrawable!=null){
            input_item.setBackgroundDrawable(backgroundDrawable);
        }
        input_item.getLayoutParams().height = (int) a.getDimension(R.styleable.LinearLayoutEditText_itemHeight, DensityUtils.sp2px(getContext(), 48));

        Drawable drawable = a.getDrawable(R.styleable.LinearLayoutEditText_edit_text_image);
        if(drawable!=null){
            input_edit_image.setImageDrawable(drawable);
            input_edit_image.setVisibility(VISIBLE);
        }
    }
    public void disableInput() {
        input_value.setVisibility(GONE);
        input_text.setVisibility(VISIBLE);
    }

    public LinearLayoutEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setInputText(String msg){
        setInputText(msg,getResources().getColor(R.color.deep_black_text_color_more));
    }
    public void setInputText(String msg,int color){
        if(!TextUtils.isEmpty(msg)){
            input_text.setText(msg);
            input_text.setTextColor(color);
            disableInput();
            setText(msg);
        }
    }
        
    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.linearlayout_edittext_item,this);
        input_value = (EditText) view.findViewById(R.id.input_value);
        input_name = (TextView) view.findViewById(R.id.input_name);
        input_image = (ImageView) view.findViewById(R.id.input_image);
        input_button = (TextView) view.findViewById(R.id.input_button);
        input_cancel = (Button) view.findViewById(R.id.input_cancel);
        input_item = (LinearLayout) view.findViewById(R.id.input_item);
        line_top = view.findViewById(R.id.line_top);
        line_bottom = view.findViewById(R.id.line_bottom);
        input_ll_cancel = (LinearLayout) view.findViewById(R.id.input_ll_cancel);
        input_edit_image = (ImageView) view.findViewById(R.id.input_edit_image);
        input_text = (TextView) view.findViewById(R.id.input_text);
        
    }

    public String getText(){
        if(input_value!=null){
            return input_value.getText().toString().trim();
        }
        return "";
    }
    public void setText(String msg){
        input_value.setText(msg);
    }

    public void setImageView(Bitmap bitmap) {
        setImageView(bitmap, null);
    }
    public void setImageView(Bitmap bitmap,OnClickListener listener) {
        input_edit_image.setImageBitmap(bitmap);
        input_edit_image.setVisibility(VISIBLE);
        if(listener!=null){
            input_edit_image.setOnClickListener(listener);
        }
    }
    public void setInputButtonVisibility(int visibility) {
        input_button.setVisibility(visibility);
    }
}
