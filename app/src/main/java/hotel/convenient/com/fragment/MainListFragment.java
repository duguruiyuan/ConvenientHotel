package hotel.convenient.com.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

import hotel.convenient.com.R;
import hotel.convenient.com.adapter.CommonRecyclerViewAdapter;
import hotel.convenient.com.adapter.MainRecyclerAdapter;
import hotel.convenient.com.domain.Publish;
import hotel.convenient.com.http.HostUrl;
import hotel.convenient.com.http.HttpUtils;
import hotel.convenient.com.http.RequestParams;
import hotel.convenient.com.http.SimplePageCallback;

/**
 * 首页
 * Created by Gyb on 2015/11/20.
 */
public class MainListFragment extends RecyclerViewFragment<Publish> implements RecyclerViewFragment.RecyclerRefreshListener,CommonRecyclerViewAdapter.OnItemClickListener{

    private SimplePageCallback simplePageCallback;
    private double latitude;
    private double longitude;
    
    public interface GetLocation{
        void getLocation();
    }
    private GetLocation getLocation;

    public void setGetLocation(GetLocation getLocation) {
        this.getLocation = getLocation;
    }

    @Override
    public CommonRecyclerViewAdapter createAdapter(List<Publish> list) {
        MainRecyclerAdapter mainRecyclerAdapter = new MainRecyclerAdapter(list);
        mainRecyclerAdapter.setOnItemClickListener(this);
        return mainRecyclerAdapter;
    }

    @Override
    public void setData(View view, Bundle savedInstanceState) {
        setRecyclerRefreshListener(this);
    }


    public void getPublishInfoByInfo(int page,double latitude,double longitude){
        if(simplePageCallback==null){
            simplePageCallback = new SimplePageCallback(this) {
                @Override
                public void firstPage(String result, int currentPage, int countPage, List<JSONObject> list) {
                    List<Publish> list1 = JSONObject.parseArray(list.toString(), Publish.class);
                    setList(list1);
                }

                @Override
                public void otherPages(String result, int currentPage, int countPage, List<JSONObject> list) {
                    List<Publish> list1 = JSONObject.parseArray(list.toString(), Publish.class);
                    addList(list1);
                }
            };
        }
        RequestParams params = new RequestParams(HostUrl.HOST+HostUrl.URL_GET_PUBLISH_INFO_LAT_LNG);
        params.addQueryStringParameter("page",page+"");
        params.addQueryStringParameter("longitude",longitude+"");
        params.addQueryStringParameter("latitude",latitude+"");
        HttpUtils.get(params,simplePageCallback);
    }

    @Override
    public void onRefresh(RecyclerViewFragment recyclerViewFragment) {
        sendLocalByHttp(initPage());
    }
    private void sendLocalByHttp(int page) {
        if (latitude != 0) {
            getPublishInfoByInfo(page, latitude, longitude);
        } else {
            setRefreshing(false);
            if(getLocation!=null)
            getLocation.getLocation();
        } 
    }
    
    public void setLatLng(double latitude,double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public void onLoading(RecyclerViewFragment RecyclerViewFragment) {
        sendLocalByHttp(addPage());
    }

    @Override
    public void onItemClick(View view, final int position) {
        switch (view.getId()){
            case R.id.ll_delete:
                mBaseActivity.showAlertDialog("确认要删除此条发布信息?"+position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                break;
        }
    }

    
}
