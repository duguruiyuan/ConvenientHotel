package hotel.convenient.com.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import hotel.convenient.com.utils.DensityUtils;

/**
 * Created by cwy on 2015/11/24 17:12
 */
public class ImagePickView extends View {
    private int count=-1;
    private int size = 30;
    private int radius = 5;
    private int currentSelect =0;
    //控件的高度
    private int height = 0;

    private int noSelectColor =Color.argb(90, 244, 244, 244);
    private int selectColor = Color.argb(95, 233, 55, 55);

    /**
     * 设置没选中时的小圆点的颜色
     * @param noSelectColor
     */
    public void setNoSelectColor(int noSelectColor) {
        this.noSelectColor = noSelectColor;
    }

    /**
     * 设置当前小圆点的颜色
     * @param selectColor
     */
    public void setSelectColor(int selectColor) {
        this.selectColor = selectColor;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setCurrentSelect(int currentSelect) {
        this.currentSelect = currentSelect;
        postInvalidate();
    }


    public ImagePickView(Context context) {
        super(context);
    }

    public ImagePickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        for(int i=0;i<attrs.getAttributeCount();i++){
            if("layout_height".equals(attrs.getAttributeName(i))){
                String h = attrs.getAttributeValue(i);
                if(h.contains("dip")){
                    h = h.replaceAll("dip","");
                    height = (int) Double.parseDouble(h);
                }
            }
        }
    }

    public ImagePickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    /**
     * 设置半径
     * @param radius dp
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * 设置小圆点所需要占据的空间 默认30
     * @param size  边长
     */
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(noSelectColor);
        Paint selectPaint = new Paint();
        selectPaint.setColor(selectColor);

        //设置抗锯齿
        paint.setAntiAlias(true);

        if(count>0){
            for(int i=0;i<count;i++){
                if(i==currentSelect){
                    //这里的xy指定的是圆心的位置
                    canvas.drawCircle(getStartX()+size*i+size/2,getStartY(), DensityUtils.dip2px(getContext(),radius),selectPaint);
                }else{
                    //这里的xy指定的是圆心的位置
                    canvas.drawCircle(getStartX()+size*i+size/2,getStartY(), DensityUtils.dip2px(getContext(),radius),paint);
                }


            }
        }

    }

    public void setCount(int count) {
        this.count = count;
    }

    /**
     * 得到小圆点开始的X坐标
     * @return
     */
    private int getStartX(){
        int screenWidth = DensityUtils.getScreenWidth(getContext());
        return (screenWidth-size*count)/2;
    }

    /**
     * 得到小圆点的Y坐标
     * @return
     */
    private int getStartY(){
        if(height>=size){
            return height/2;
        }
        return DensityUtils.dip2px(getContext(),size);
    }
}
