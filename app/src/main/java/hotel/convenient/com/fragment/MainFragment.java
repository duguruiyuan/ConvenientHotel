package hotel.convenient.com.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.xutils.http.RequestParams;
import org.xutils.http.request.UriRequest;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import hotel.convenient.com.R;
import hotel.convenient.com.adapter.CommonRecyclerViewAdapter;

/**
 * Created by Gyb on 2015/11/20.
 */
@ContentView(R.layout.main_fragment)
public class MainFragment extends RecyclerViewFragment{

    @Override
    public CommonRecyclerViewAdapter createAdapter(List list) {
        return new CommonRecyclerViewAdapter(list) {
            @Override
            public int setLayoutItemResId() {
                return R.layout.main_recycler_view_item;
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(View view, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolderItem(RecyclerView.ViewHolder holder, int position) {

            }
        };
    }

    @Override
    public void setData(View view, Bundle savedInstanceState) {

    }
}
