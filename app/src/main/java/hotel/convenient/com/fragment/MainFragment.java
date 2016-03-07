package hotel.convenient.com.fragment;

import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSONObject;

import org.xutils.http.RequestParams;

import java.util.List;

import hotel.convenient.com.adapter.CommonRecyclerViewAdapter;
import hotel.convenient.com.adapter.DealerRecyclerAdapter;
import hotel.convenient.com.domain.Publish;
import hotel.convenient.com.http.HostUrl;
import hotel.convenient.com.http.HttpUtils;
import hotel.convenient.com.http.SimplePageCallback;

/**
 * Created by Gyb on 2015/11/20.
 */
public class MainFragment extends RecyclerViewFragment<Publish> implements RecyclerViewFragment.RecyclerRefreshListener{

    private SimplePageCallback simplePageCallback;
    @Override
    public CommonRecyclerViewAdapter createAdapter(List<Publish> list) {
        return new DealerRecyclerAdapter(list);
    }

    @Override
    public void setData(View view, Bundle savedInstanceState) {
        setRecyclerRefreshListener(this);
        getPublishInfoByInfo(initPage());
    }
    
    public void getPublishInfoByInfo(int page){
        if(simplePageCallback==null){
            simplePageCallback = new SimplePageCallback(this) {
                @Override
                public void firstPage(String result, int currentPage, int countPage, List<JSONObject> list) {
                    
                }

                @Override
                public void otherPages(String result, int currentPage, int countPage, List<JSONObject> list) {

                }
            };
        }
        RequestParams params = new RequestParams(HostUrl.HOST+HostUrl.URL_GET_PUBLISH_INFO);
        params.addQueryStringParameter("page",page+"");
        HttpUtils.get(params,simplePageCallback);
    }

    @Override
    public void onRefresh(RecyclerViewFragment recyclerViewFragment) {
        getPublishInfoByInfo(initPage());
    }

    @Override
    public void onLoading(RecyclerViewFragment RecyclerViewFragment) {
        getPublishInfoByInfo(addPage());
    }
}
