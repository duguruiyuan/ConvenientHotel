package hotel.convenient.com.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import hotel.convenient.com.R;
import hotel.convenient.com.adapter.CommonRecyclerViewAdapter;
import hotel.convenient.com.base.BaseFragment;

/**
 * 通用的recyclerView fragment 可以下拉刷新 上拉加载  需要自定义adapter
 * Created by Gyb on 2015/12/10 16:53
 */
public abstract class RecyclerViewFragment<T> extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.swipe_refresh_widget)
     SwipeRefreshLayout mSwipeRefreshWidget;
    @Bind(R.id.refresh_recycler)
     RecyclerView mRecyclerView;
    private int lastVisibleItem;
    private LinearLayoutManager mLayoutManager;

    private List<T> list = new ArrayList<>();
    private CommonRecyclerViewAdapter adapter;
    private boolean isDown = false;
    private boolean isOpenLoad = true;
    private boolean isOpenRefresh = true;
    private boolean isFull = false;

    @Override
    public int setLayoutView() {
        return R.layout.recyclerview_fragment;
    }
    /**
     * 打开上拉加载
     * @param isOpenLoad
     */
    public void setIsOpenLoad(boolean isOpenLoad) {
        this.isOpenLoad = isOpenLoad;
    }

    /**
     * 打开下拉刷新
     * @param isOpenRefresh
     */
    public void setIsOpenRefresh(boolean isOpenRefresh) {
        this.isOpenRefresh = isOpenRefresh;
    }

    public abstract CommonRecyclerViewAdapter createAdapter(List<T> list);
    /**
     * 关于recyclerView 上拉加载 下拉刷新的接口
     */
    public interface RecyclerRefreshListener{
        /**下拉刷新*/
        void onRefresh(RecyclerViewFragment recyclerViewFragment);
        /**上拉加载*/
        void onLoading(RecyclerViewFragment RecyclerViewFragment);
    }
    private RecyclerRefreshListener recyclerRefreshListener;

    /**
     * 监听滑动事件
     * @param recyclerRefreshListener
     */
    public void setRecyclerRefreshListener(RecyclerRefreshListener recyclerRefreshListener) {
        this.recyclerRefreshListener = recyclerRefreshListener;
        setIsFull(false);
    }

    /**
     * 用来初始化数据
     * @param view
     * @param savedInstanceState
     */
    public abstract void setData(View view, Bundle savedInstanceState);
    @Override
    public void initData(View view, Bundle savedInstanceState) {
        mSwipeRefreshWidget.setColorSchemeResources(R.color.progressColor, android.R.color.holo_red_light, android.R.color.holo_green_light);
        mSwipeRefreshWidget.setOnRefreshListener(this);
        
        //监听拉上
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    if (isDown) {
                        if (recyclerRefreshListener != null)
                            if (isOpenLoad) {
                                if (!isFull) {
                                    setRefreshing(true);
                                    adapter.setFooterName("加载中...");
                                    recyclerRefreshListener.onLoading(RecyclerViewFragment.this);
                                    isDown = false;
                                    adapter.setFooterName("点击加载更多...");
                                }
                            }
                    } else {
                        isDown = true;
                    }
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                isDown = false;
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = createAdapter(list);
        mRecyclerView.setAdapter(adapter);
        setIsFull(true);
        setData(view,savedInstanceState);
    }


    /**
     * 设置 最后一个item显示的内容  
     * @param name   null则隐藏
     * @View.OnClickListener 点击事件接口
     */
    public void setFoot(String name,View.OnClickListener listener){
        if(adapter!=null){
            if(TextUtils.isEmpty(name)){
                adapter.setFooterGone();
            }else {
                adapter.setFooterName(name);
            }
            adapter.setFooterOnClickListener(listener);
        }
    }

    public void addItem(T t){
        if(list==null) list = new ArrayList<>();
        list.add(t);
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }
    public void notifyItemChanged(int position) {
        if(adapter!=null){
            adapter.notifyItemChanged(position);
        }
    }

    /**
     * 设置是否已经加载到最后一页
     * @param b
     */
    public void setIsFull(boolean b) {
        if (b){
           setFoot("已经没有更多了",null);
        }else {
            setFoot("点击加载更多",new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerRefreshListener != null)
                        recyclerRefreshListener.onLoading(RecyclerViewFragment.this);
                }
            });
        }
        isFull = b;
    }
    public void removeItem(int pos){
        list.remove(pos);
        notifyDataSetChanged();
    }
    public void removeItem(T t){
        list.remove(t);
        notifyDataSetChanged();
    }
    
    @Override
    public void onRefresh() {
       if(recyclerRefreshListener!=null) recyclerRefreshListener.onRefresh(this);
        setIsFull(false);
        
    }
    public  List<T> getList(){
        return list;
    }

    /**
     * 清空以前的list  设置新的list
     * @param list1
     */
    public void setList(List<T> list1){
        list.clear();
        list.addAll(list1);
        notifyDataSetChanged();
        setRefreshing(false);
    }

    /**
     * 在原有的list后面添加新的list
     * @param list1
     */
    public void addList(List<T> list1){
        list.addAll(list1);
        notifyDataSetChanged();
    }
    /**
     * 设置是否刷新
     * @param isRefreshing  true  刷新  false  停止刷新
     */
    public void setRefreshing(boolean isRefreshing){
        if(mSwipeRefreshWidget!=null)
        mSwipeRefreshWidget.setRefreshing(isRefreshing);
    }
}
