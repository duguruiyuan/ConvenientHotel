package hotel.convenient.com.view;

import android.content.Context;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwy on 2015/12/28 14:15
 */
public class RollViewPager extends ViewPager {
    private List<ImageView> list = new ArrayList<>();
    private MyRollViewPagerAdapter adapter;
    private int currentItem = 0;
    private OnViewPagerItemClickListener onItemClickListener;

    private android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            RollViewPager.this.setCurrentItem(currentItem);
            startRoll(0);
        }
    };

    public RollViewPager(Context context) {
        super(context);
    }

    public RollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setList(List<ImageView> list) {
        this.list.clear();
        this.list.addAll(list);
    }

    public void setOnItemClickListener(OnViewPagerItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacksAndMessages(null);
    }

    private int downX;
    private int downY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 让当前viewpager的父控件不去拦截touch事件
                getParent().requestDisallowInterceptTouchEvent(true);
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();
                if (Math.abs(moveX - downX) >= Math.abs(moveY - downY)) {
                    // 滑动轮播图
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    // 允许父控件处理touch事件
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 滚动viewpager
     * @param count  第几次调用   设定第一次调用时才能启动定时任务    默认0为第一次
     */
    public void startRoll(int count) {
        if (adapter == null) {
            adapter = new MyRollViewPagerAdapter();
            this.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        if(list.size()<1){
            return;
        }
        if(count<1)
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentItem = (currentItem + 1)%list.size();
                handler.obtainMessage().sendToTarget();
            }
        }, 5000);
    }

    class MyRollViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imageView = list.get(position);
            imageView.setOnTouchListener(new OnTouchListener() {
                private int downX;
                private int downY;
                private long downTime;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            handler.removeCallbacksAndMessages(null);
                            downX = (int)event.getX();
                            downY = (int)event.getY();
                            downTime = System.currentTimeMillis();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            break;
                        case MotionEvent.ACTION_UP:
                            if (System.currentTimeMillis() - downTime < 600
                                    && Math.abs(downX - event.getX()) < 10
                                    && Math.abs(downY - event.getY()) < 10) {
                                // 接口回调
                                if (onItemClickListener != null) {
                                    onItemClickListener.onClick(position);
                                }
                            }
                            startRoll(0);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            startRoll(0);
                            break;
                    }
                    return true;
                }
            });
            ViewParent parent = imageView.getParent();
            if(parent!=null){
                ((ViewGroup)parent).removeView(imageView);
            }
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if(container!=null)
            container.removeView((View) object);
        }
    }

    /**
     * viewpager 的 item 被点击时 回调用的接口
     * @author lenovo
     *
     */
    public interface OnViewPagerItemClickListener  {
        void onClick(int i);
    }
}
