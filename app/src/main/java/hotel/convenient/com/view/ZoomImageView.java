package hotel.convenient.com.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by Gyb on 2016/4/13 15:45
 */
public class ZoomImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener, ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {

    private boolean mOnce = false;
    private Matrix matrix;
    
    private float minScale;
    private float mediumScale;
    private float maxScale;
    
    
    private ScaleGestureDetector scaleGestureDetector;

    public ZoomImageView(Context context) {
        this(context,null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        matrix = new Matrix();
        scaleGestureDetector = new ScaleGestureDetector(getContext(),this);
        setOnTouchListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    //在window上显示 
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    /**
     * 获取image加载完成的图片
     */
    @Override
    public void onGlobalLayout() {
        if(!mOnce){
            int width = getWidth();
            int height = getHeight();

            Drawable drawable = getDrawable();
            if(drawable==null){
                return;
            }
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
            float scale = 1;
            
            if(width>intrinsicWidth&&height<intrinsicHeight){
                scale = height*1.0f/intrinsicHeight;
            }
            if(width<intrinsicWidth&&height>intrinsicHeight){
                scale = width*1.0f/intrinsicWidth;
            }
            if((width>intrinsicWidth&&height>intrinsicHeight)||(width<intrinsicWidth&&height<intrinsicHeight)){
                scale = Math.min(width*1.0f/intrinsicWidth,height*1.0f/intrinsicHeight);
            }
            minScale = scale;
            mediumScale = scale*2;
            maxScale = scale*4;
            matrix.postTranslate(width/2-intrinsicWidth/2,height/2-intrinsicHeight/2);
            matrix.postScale(minScale,minScale,width/2,height/2);
            setImageMatrix(matrix);
            mOnce = true;
        }
    }

    /**
     * 得到图片当前的缩放比例
     * @return
     */
    public float getScale(){
        float values[] = new float[9];
        matrix.getValues(values);
        return values[matrix.MSCALE_X];
    }
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if(getDrawable()==null) return false;
        float scaleFactor = detector.getScaleFactor();//得到手指的缩放比例
        float scale = getScale();
        if(scale<maxScale&&scaleFactor>1.0f||scale>minScale&&scaleFactor<1.0f){
            if(scale*scaleFactor>maxScale){
                scaleFactor = maxScale/scale;
            }else if(scale*scaleFactor<minScale){
                scaleFactor = minScale/scale;
            }
            matrix.postScale(scaleFactor,scaleFactor,detector.getFocusX(),detector.getFocusY());
            // TODO: 2016/4/13 反正白边  在缩放时要判断 
            setImageMatrix(matrix);
        }
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }
}
