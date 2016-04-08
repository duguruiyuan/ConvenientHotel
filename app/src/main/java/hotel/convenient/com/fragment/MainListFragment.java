package hotel.convenient.com.fragment;

import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSONObject;

import java.util.GregorianCalendar;
import java.util.List;

import hotel.convenient.com.activity.DealerInfoActivity;
import hotel.convenient.com.adapter.CommonRecyclerViewAdapter;
import hotel.convenient.com.adapter.MainRecyclerAdapter;
import hotel.convenient.com.domain.ChooseDealerInfo;
import hotel.convenient.com.domain.Publish;
import hotel.convenient.com.http.HostUrl;
import hotel.convenient.com.http.HttpUtils;
import hotel.convenient.com.http.RequestParams;
import hotel.convenient.com.http.SimplePageCallback;

/**
 * 首页列表
 * Created by Gyb on 2015/11/20.
 */
public class MainListFragment extends RecyclerViewFragment<Publish> implements RecyclerViewFragment.RecyclerRefreshListener,CommonRecyclerViewAdapter.OnItemClickListener{

    private SimplePageCallback simplePageCallback;
    private double latitude;
    private double longitude;
    private GregorianCalendar endCalendar;
    private GregorianCalendar startCalendar;

    public void setEndCalendar(GregorianCalendar endCalendar) {
        this.endCalendar = endCalendar;
    }

    public void setStartCalendar(GregorianCalendar startCalendar) {
        this.startCalendar = startCalendar;
    }

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
        if(this.latitude == latitude&&this.longitude == longitude){
            return;
        }
        this.latitude = latitude;
        this.longitude = longitude;
        sendLocalByHttp(initPage());
    }

    @Override
    public void onLoading(RecyclerViewFragment RecyclerViewFragment) {
        sendLocalByHttp(addPage());
    }

    @Override
    public void onItemClick(View view, final int position) {
        Publish publish = getList().get(position);
        ChooseDealerInfo paramValue = new ChooseDealerInfo(publish);
        paramValue.setStartCalendar(startCalendar);
        paramValue.setEndCalendar(endCalendar);
        mBaseActivity.skipActivity(DealerInfoActivity.class,false,"data", paramValue);
    }
}
