package hotel.convenient.com.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import hotel.convenient.com.utils.LogUtils;

/**
 * Created by Gyb on 2016/4/13 15:45
 */
public class ZoomImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener, ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {

    private boolean mOnce = false;
    private Matrix matrix;
    
    private float minScale;
    private float mediumScale;
    private float maxScale;
    
    //图片移动参数
    private int lastTouchPointCount = 0;
    private float lastX = 0;
    private float lastY = 0;
    private float scaledTouchSlop; //允许移动的最小距离
    private boolean isMove = false;

    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    public ZoomImageView(Context context) {
        this(context,null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScaleType(ScaleType.MATRIX);
        matrix = new Matrix();
        scaleGestureDetector = new ScaleGestureDetector(getContext(),this);
        setOnTouchListener(this);//onTouchEvent 方法会被屏蔽

        scaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                float x = e.getX();
                float y = e.getY();

                if (getScale() < mediumScale) {
                    postDelayed(new AutoScaleRunnable(mediumScale, x, y),16);//动画效果
                } else {
                    postDelayed(new AutoScaleRunnable(minScale, x, y),16);//动画效果
                } 
                return super.onDoubleTap(e);
            }
        });
    }

    /**
     * 我们销毁View的时候。我们写的这个View不再显示的时候调用
     * 取消监听
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    /**
     * onAttachedToWindow是在第一次onDraw前调用的。也就是我们写的View在没有绘制出来时调用的，但只会调用一次。
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    /**
     * 监听视图改变   可获得空间的真实高度    多次调用 
     * 设置监听
     * getViewTreeObserver().addOnGlobalLayoutListener(this);
     */
    @Override
    public void onGlobalLayout() {
        Drawable drawable = getDrawable();
        if(drawable==null){
            LogUtils.e("还未加载图片或图片加载失败");
            return;
        }
        if(!mOnce){ //设置只调用一次  初始化值  调整图片位置
            int width = getWidth();
            int height = getHeight();

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
            checkRectAndCenter();
            setImageMatrix(matrix);
        }
        return true;
    }

    /**
     * 检查并保持居中 ,不过界 
     */
    private void checkRectAndCenter() {
        Drawable drawable = getDrawable();
        if(drawable==null){
            return;
        }
        RectF rect = getMatrixRectF();
        float width = getWidth();
        float height = getHeight();
        float dx = 0;
        float dy = 0;
        dx = getDx(rect, width);
        dy = getDy(rect, height);
        //如果图片宽度或高度小于空间大小  则居中
        if(rect.width()<width){
            dx = width/2f - (rect.left+rect.width()/2f);
        }
        if(rect.height()<height){
            dy = height/2f - (rect.top+rect.height()/2f);
        }
        matrix.postTranslate(dx,dy);
    }

    /**
     * 得到垂直方向的边界判断的偏移量    保证垂直方向不会越界
     * @param rect
     * @param height
     * @return
     */
    private float getDy(RectF rect, float height) {
        float dy = 0;
        if(rect.height()>=height){
            if(rect.top>0){
                dy = -rect.top;
            }
            if(rect.bottom<height){
                dy = height-rect.bottom;
            }
        }
        return dy;
    }

    /**
     * 得到水平方向的边界判断的偏移量    保证水平方向不会越界
     * @param rect
     * @param width
     * @return
     */
    private float getDx(RectF rect, float width) {
        float dx = 0;
        if(rect.width()>=width){
            if(rect.right<width){
                dx = width- rect.right;
            }
            if(rect.left>0){
                dx = -rect.left;
            }
        }
        return dx;
    }

    @NonNull
    private RectF getMatrixRectF() {
        Drawable drawable = getDrawable();
        float intrinsicWidth = drawable.getIntrinsicWidth();
        float intrinsicHeight = drawable.getIntrinsicHeight();
        RectF rect = new RectF(0, 0, intrinsicWidth, intrinsicHeight);
        matrix.mapRect(rect);
        return rect;
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
        if(gestureDetector.onTouchEvent(event)){
            return true;
        }
        int pointerCount = event.getPointerCount();
        //找到触摸的终点
        float x = 0;
        float y = 0;
        for(int i=0;i<pointerCount;i++){
            x += event.getX(i);
            y += event.getY(i);
        }
        x = x/pointerCount;
        y = y/pointerCount;
        if(lastTouchPointCount!=pointerCount){
            isMove = false;
            lastX = x;
            lastY = y;
        }
        lastTouchPointCount = pointerCount;
        RectF matrixRectF = getMatrixRectF();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(matrixRectF.width()>getWidth()||matrixRectF.height()>getHeight()){
                    getParent().requestDisallowInterceptTouchEvent(true);//通知父控件不要拦截此点击事件
                }
            case MotionEvent.ACTION_MOVE:
                if(matrixRectF.width()>getWidth()||matrixRectF.height()>getHeight()){
                    getParent().requestDisallowInterceptTouchEvent(true);//通知父控件不要拦截此点击事件
                }
                if(getDrawable()!=null){
                    float dx = x - lastX;
                    float dy = y - lastY;
                    if(!isMove){
                        isMove = isMove(dx,dy);
                    }
                    if(isMove){
                        
                        if (matrixRectF.width() <= getWidth()) {
                            dx = 0;
                        }
                        if (matrixRectF.height() <= getHeight()) {
                            dy = 0;
                        }
                        matrix.postTranslate(dx,dy);
                        checkBorderWhenTranslate();
                        setImageMatrix(matrix);
                    }
                }
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                lastTouchPointCount = 0;
                break;
        }
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    /**
     * 判断移动时是否过界
     */
    private void checkBorderWhenTranslate() {
        RectF matrixRectF = getMatrixRectF();
        matrix.postTranslate(getDx(matrixRectF,getWidth()),getDy(matrixRectF,getHeight()));
    }

    private boolean isMove(float dx, float dy) {
        return Math.sqrt(dx * dx + dy * dy) > scaledTouchSlop;
    }
    
    
    private class AutoScaleRunnable implements Runnable{

        private float targetScale;
        private float pointX;
        private float pointY;

        public static final float BIGGER = 1.07F;
        public static final float SMALL = 0.93F;
        
        private float runScale;

        /**
         * @param targetScale  目标scale
         * @param pointX  中心点 X
         * @param pointY  中心点 Y
         */
        public AutoScaleRunnable(float targetScale, float pointX, float pointY) {
            this.targetScale = targetScale;
            this.pointX = pointX;
            this.pointY = pointY;
            if(targetScale>getScale()){
                runScale = BIGGER;
            }else if(targetScale<getScale()){
                runScale = SMALL;
            }
        }

        @Override
        public void run() {
            
            matrix.postScale(runScale,runScale,pointX,pointY);
            checkRectAndCenter();
            setImageMatrix(matrix);

            float currentScale = getScale();
            if (targetScale > currentScale && runScale == BIGGER || targetScale < currentScale && runScale == SMALL) {
                //如果还没达到目标大小
                postDelayed(this, 16);  //重复执行run方法
            } else {//达到或超过目标大小
                //一步到位
                matrix.postScale(targetScale/ currentScale,targetScale/ currentScale,pointX,pointY);
                checkRectAndCenter();
                setImageMatrix(matrix);
            } 
        }
    }
}
