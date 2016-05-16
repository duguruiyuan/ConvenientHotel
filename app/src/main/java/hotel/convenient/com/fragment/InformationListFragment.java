package hotel.convenient.com.fragment;

import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

import hotel.convenient.com.adapter.CommonRecyclerViewAdapter;
import hotel.convenient.com.adapter.InformationListAdapter;
import hotel.convenient.com.domain.Information;
import hotel.convenient.com.http.HostUrl;
import hotel.convenient.com.http.HttpUtils;
import hotel.convenient.com.http.RequestParams;
import hotel.convenient.com.http.SimplePageCallback;

/**
 * Created by cwy on 2016/1/29 14:32
 */
public class InformationListFragment extends RecyclerViewFragment<Information> {
    private SimplePageCallback simpleCallback;
    @Override
    public CommonRecyclerViewAdapter createAdapter(List<Information> list) {
        InformationListAdapter innerMessListAdapter=new InformationListAdapter(list);
        return innerMessListAdapter;
    }

    @Override
    public void setData(View view, Bundle savedInstanceState) {
        httpData(getCurrentPage());
        setRecyclerRefreshListener(new RecyclerRefreshListener() {
            @Override
            public void onRefresh(RecyclerViewFragment recyclerViewFragment) {
                httpData(initPage());
            }
            @Override
            public void onLoading(RecyclerViewFragment RecyclerViewFragment) {
                httpData(addPage());
            }
        });
    }
    private void httpData(int page) {
        RequestParams requestParams=new RequestParams(HostUrl.HOST + HostUrl.URL_GET_MESSAGE);
        requestParams.addQueryStringParameter("page", page + "");
        if (simpleCallback == null) {
            simpleCallback = new SimplePageCallback(this) {
                @Override
                public void firstPage(String result, int currentPage, int countPage, List<JSONObject> list) {
                    setList(JSONObject.parseArray(list.toString(), Information.class));
                }

                @Override
                public void otherPages(String result, int currentPage, int countPage, List<JSONObject> list) {
                    addList(JSONObject.parseArray(list.toString(), Information.class));
                }

            };
        }
        HttpUtils.get(requestParams, simpleCallback);
    }
}
