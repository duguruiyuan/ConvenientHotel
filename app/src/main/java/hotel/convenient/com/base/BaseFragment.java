package hotel.convenient.com.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import hotel.convenient.com.R;
import hotel.convenient.com.utils.LogUtils;

/**
 * Created by cwy on 2015/11/23.
 */
public abstract class BaseFragment extends Fragment {
    private boolean injected = false;
    public BaseActivity mBaseActivity;
    protected SwipeRefreshLayout mSwipe_main;
    //通用的分页
    public static final int INIT_PAGE = 0;
    private int currentPage = INIT_PAGE;

    /**
     * 初始化page 一般在下拉刷新的时候调用
     * @return
     */
    public int initPage(){
        return currentPage = INIT_PAGE;
    }
    /**
     * 获取下一页
     * @return
     */
    public int addPage(){
        return ++currentPage;
    }
    public int getCurrentPage() {
        return currentPage;
    }
    /**
     * 设置activity标题的接口  一般在activity中实现该接口
     */
    public interface ActivityTitleListener{
        /**
         * @param title  标题
         */
        void setActivityTitle(Spanned title);
    }

    protected ActivityTitleListener titleListener;

    public void setTitleListener(ActivityTitleListener titleListener) {
        this.titleListener = titleListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mBaseActivity = (BaseActivity) context;
    }
    public abstract int setLayoutView();
    /**
     * 初始数据
     * @param view
     * @param savedInstanceState
     */
    public abstract void initData(View view, Bundle savedInstanceState);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(setLayoutView(), container, false);
        ButterKnife.bind(this,view);
        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //填充数据
        initData(view, savedInstanceState);
    }

    /**
     * 开启下拉刷新功能
     * @param listener
     */
    public void openSwipeRefreshLayout(SwipeRefreshLayout.OnRefreshListener listener,View view){
        //下拉刷新控件
        mSwipe_main = (SwipeRefreshLayout) view.findViewById(R.id.swipe_main);
        if(mSwipe_main==null){
            LogUtils.e("未添加SwipeRefreshLayout控件 不能使用下拉刷新功能");
        }else {
            mSwipe_main.setOnRefreshListener(listener);
            mSwipe_main.setColorSchemeResources(R.color.progressColor, android.R.color.holo_red_light);
            mSwipe_main.setProgressBackgroundColorSchemeResource(R.color.ProgressBackgroundColor);
            mSwipe_main.startLayoutAnimation();
        }
    }
    public void setRefreshing(boolean refreshing){
        if(mSwipe_main!=null)
            mSwipe_main.setRefreshing(refreshing);
    }

}
