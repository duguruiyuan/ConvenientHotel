package hotel.convenient.com.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSONObject;

import org.xutils.http.RequestParams;

import java.util.List;

import hotel.convenient.com.R;
import hotel.convenient.com.adapter.CommonRecyclerViewAdapter;
import hotel.convenient.com.adapter.DealerRecyclerAdapter;
import hotel.convenient.com.domain.Publish;
import hotel.convenient.com.http.HostUrl;
import hotel.convenient.com.http.HttpUtils;
import hotel.convenient.com.http.ResultJson;
import hotel.convenient.com.http.SimpleCallback;
import hotel.convenient.com.http.SimplePageCallback;

/**
 * Created by Gyb on 2015/11/20.
 */
public class MainFragment extends RecyclerViewFragment<Publish> implements RecyclerViewFragment.RecyclerRefreshListener,CommonRecyclerViewAdapter.OnItemClickListener{

    private SimplePageCallback simplePageCallback;
    private SimpleCallback deleteCallback;
    @Override
    public CommonRecyclerViewAdapter createAdapter(List<Publish> list) {
        DealerRecyclerAdapter dealerRecyclerAdapter = new DealerRecyclerAdapter(list);
        dealerRecyclerAdapter.setOnItemClickListener(this);
        return dealerRecyclerAdapter;
    }

    @Override
    public void setData(View view, Bundle savedInstanceState) {
        getPublishInfoByInfo(initPage());
        setRecyclerRefreshListener(this);
    }
    
    public void getPublishInfoByInfo(int page){
        if(simplePageCallback==null){
            simplePageCallback = new SimplePageCallback(this) {
                @Override
                public void firstPage(String result, int currentPage, int countPage, List<JSONObject> list) {
                    setList(JSONObject.parseArray(list.toString(),Publish.class));
                }

                @Override
                public void otherPages(String result, int currentPage, int countPage, List<JSONObject> list) {
                    addList(JSONObject.parseArray(list.toString(),Publish.class));
                }
            };
        }
        RequestParams params = new RequestParams(HostUrl.HOST+HostUrl.URL_GET_PUBLISH_INFO);
        params.addQueryStringParameter("page",page+"");
        HttpUtils.get(params,simplePageCallback);
    }
    public void removePublish(final Publish publish){
        if(deleteCallback==null){
            deleteCallback = new SimpleCallback(mBaseActivity) {
                @Override
                public <T> void simpleSuccess(String url, String result, ResultJson<T> resultJson) {
                    if(resultJson.getCode()==CODE_SUCCESS){
                       removeItem(publish);
                    }else{
                       mBaseActivity.showShortToast(resultJson.getMsg());
                    }
                }
            };
        }
        RequestParams params = new RequestParams(HostUrl.HOST+HostUrl.URL_REMOVE_PUBLISH);
        params.addBodyParameter("id",publish.getId()+"");
        HttpUtils.post(params,deleteCallback);
    }

    @Override
    public void onRefresh(RecyclerViewFragment recyclerViewFragment) {
        getPublishInfoByInfo(initPage());
    }

    @Override
    public void onLoading(RecyclerViewFragment RecyclerViewFragment) {
        getPublishInfoByInfo(addPage());
    }

    @Override
    public void onItemClick(View view, final int position) {
        switch (view.getId()){
            case R.id.ll_delete:
                mBaseActivity.showAlertDialog("确认要删除此条发布信息?"+position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removePublish(getList().get(position));
                    }
                });
                break;
        }
    }
}
