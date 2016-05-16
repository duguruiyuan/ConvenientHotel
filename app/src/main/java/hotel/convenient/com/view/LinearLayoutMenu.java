package hotel.convenient.com.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import hotel.convenient.com.R;
import hotel.convenient.com.utils.DensityUtils;


/**
 * Created by cwy on 2015/12/9 14:14
 */
public class LinearLayoutMenu extends LinearLayout {
    public static final int DOWN_FILL = 0;
    public static final int DOWN_GAP = 1;
    public static final int UP_FILL = 2;
    public static final int UP_GAP = 3;

    public static final int DOWN_GONE = 5;
    public static final int UP_GONE = 4;
    
    private View line_up_fill;
    private View line_up_gap;
    private View line_down_fill;
    private View line_down_gap;
    
    private TextView title;
    private ImageView image;
    private TextView msg;
    private ImageView img; //左边图标
    private ToggleButton toggleButton;
    public boolean canClick;
    public boolean showArrow;

    public TextView not_read_msg;
    /**
     * 得到左边图标
     * @return
     */
    public ImageView getLeftImage(){
        return img;
    }
    public LinearLayoutMenu(Context context) {
        super(context);
        initView(context);
    }
    public void setMsg(String message){
        if (msg!=null)
        msg.setText(message);
    }
    public void setMsg(Spanned message){
        if (msg!=null)
            msg.setText(message);
    }
    public String getMsg() {
        if (msg != null) {
            return msg.getText().toString().trim();
        }
        return "";
    }

    public LinearLayoutMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LinearLayoutMenu);
        
        //得到右边图案
        image.setImageDrawable(a.getDrawable(R.styleable.LinearLayoutMenu_nextPageSrc));
        
        //得到标题各种属性
        title.setText(a.getString(R.styleable.LinearLayoutMenu_menuTitle));
        title.setTextColor(a.getColor(R.styleable.LinearLayoutMenu_titleColor, Color.BLACK));
        float titlesize = a.getDimension(R.styleable.LinearLayoutMenu_titleSize, DensityUtils.sp2px(getContext(),14));
        titlesize = DensityUtils.px2sp(getContext(),titlesize);
        title.setTextSize(titlesize);
        
        //得到消息各种属性
        msg.setText(a.getString(R.styleable.LinearLayoutMenu_menuMsg));
        msg.setTextColor(a.getColor(R.styleable.LinearLayoutMenu_msgColor, Color.BLACK));
        float msgsize = a.getDimension(R.styleable.LinearLayoutMenu_msgSize, DensityUtils.sp2px(getContext(),14));
        msgsize = DensityUtils.px2sp(getContext(),msgsize);
        msg.setTextSize(msgsize);
        
        //得到toggle按钮
        Drawable drawable = a.getDrawable(R.styleable.LinearLayoutMenu_menuMsgIcon);
        if(drawable!=null){
            toggleButton.setVisibility(VISIBLE);
            toggleButton.setBackgroundDrawable(drawable);
            
        }

        int down = a.getInt(R.styleable.LinearLayoutMenu_lineDown,DOWN_FILL);
        int up = a.getInt(R.styleable.LinearLayoutMenu_lineUp,UP_FILL);
        if(down==DOWN_FILL){
            line_down_fill.setVisibility(VISIBLE);
            line_down_gap.setVisibility(GONE);
        }else if (down==DOWN_GAP) {
            line_down_fill.setVisibility(GONE);
            line_down_gap.setVisibility(VISIBLE);
        }else if(down==DOWN_GONE){
            line_down_fill.setVisibility(GONE);
            line_down_gap.setVisibility(GONE);
        }
        if(up==UP_FILL){
            line_up_fill.setVisibility(VISIBLE);
            line_up_gap.setVisibility(GONE);
        }else if (up==UP_GAP) {
            line_up_fill.setVisibility(GONE);
            line_up_gap.setVisibility(VISIBLE);
        }else if (up == UP_GONE) {
            line_up_fill.setVisibility(GONE);
            line_up_gap.setVisibility(GONE);
        }
        showArrow=a.getBoolean(R.styleable.LinearLayoutMenu_showArrow,true);
        if(showArrow){
            image.setVisibility(VISIBLE);
        }else{
            image.setVisibility(INVISIBLE);
        }


        Drawable drawable1 = a.getDrawable(R.styleable.LinearLayoutMenu_img);
        if(drawable1!=null){
            img.setVisibility(VISIBLE);
            img.setBackgroundDrawable(drawable1);

        }
    }

    public void setNotReadMsgCount(String count){
        if (count.equals("0")) {
            not_read_msg.setVisibility(GONE);
        } else {
            not_read_msg.setVisibility(VISIBLE);
            not_read_msg.setText(count);
        } 
    }
    public LinearLayoutMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    
    public void setToggleOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener){
        toggleButton.setOnCheckedChangeListener(listener);
    }
    public void setToggleChecked(boolean checked){
        toggleButton.setChecked(checked);
    }
    public void setTitle(String titles){
        title.setText(titles);
    }
    public void setArrowVisiable(boolean showArrow){
        if(showArrow){
            image.setVisibility(VISIBLE);
        }else{
            image.setVisibility(INVISIBLE);
        }
    }
    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.linearlayout_menu_item,this);
        title = (TextView) view.findViewById(R.id.title);
        msg = (TextView) view.findViewById(R.id.msg);
        image = (ImageView) view.findViewById(R.id.image);
        line_up_fill =  view.findViewById(R.id.line_up_fill);
        line_up_gap =  view.findViewById(R.id.line_up_gap);
        line_down_fill =  view.findViewById(R.id.line_down_fill);
        line_down_gap =  view.findViewById(R.id.line_down_gap);
        toggleButton = (ToggleButton) view.findViewById(R.id.tog_bt);
        img = (ImageView) view.findViewById(R.id.img);
        not_read_msg = (TextView) view.findViewById(R.id.not_read_msg);
    }
}
