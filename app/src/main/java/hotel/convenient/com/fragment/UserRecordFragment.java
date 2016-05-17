package hotel.convenient.com.fragment;

import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

import hotel.convenient.com.adapter.CommonRecyclerViewAdapter;
import hotel.convenient.com.adapter.UserRecyclerAdapter;
import hotel.convenient.com.domain.UserRecord;
import hotel.convenient.com.http.HostUrl;
import hotel.convenient.com.http.HttpUtils;
import hotel.convenient.com.http.RequestParams;
import hotel.convenient.com.http.SimplePageCallback;
import hotel.convenient.com.utils.PreferenceUtils;
import hotel.convenient.com.utils.ToastUtil;

/**
 * Created by cwy on 2015/11/20.
 */
public class UserRecordFragment extends RecyclerViewFragment<UserRecord> implements RecyclerViewFragment.RecyclerRefreshListener,CommonRecyclerViewAdapter.OnItemClickListener{

    private SimplePageCallback simplePageCallback;
    /** Fragment当前状态是否可见 */
    protected boolean isVisible;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }
    /**
     * 可见 
     */
    protected void onVisible() {
        getPublishInfoByInfo(initPage());
    }

    /**
     * 不可见 
     */
    protected void onInvisible() {}
    @Override
    public CommonRecyclerViewAdapter createAdapter(List<UserRecord> list) {
        UserRecyclerAdapter userRecyclerAdapter = new UserRecyclerAdapter(list);
        userRecyclerAdapter.setOnItemClickListener(this);
        return userRecyclerAdapter;
    }

    @Override
    public void setData(View view, Bundle savedInstanceState) {
        setRecyclerRefreshListener(this);
    }


    public void getPublishInfoByInfo(int page){
        if(!PreferenceUtils.isLogin(getContext())){
            ToastUtil.showShortToast("您还未登录,请登录后重试");
            return;
        }
        if(simplePageCallback==null){
            simplePageCallback = new SimplePageCallback(this) {
                @Override
                public void firstPage(String result, int currentPage, int countPage, List<JSONObject> list) {
                    setList(JSONObject.parseArray(list.toString(),UserRecord.class));
                }

                @Override
                public void otherPages(String result, int currentPage, int countPage, List<JSONObject> list) {
                    addList(JSONObject.parseArray(list.toString(),UserRecord.class));
                }
            };
        }
        RequestParams params = new RequestParams(HostUrl.HOST+HostUrl.URL_GET_USER_RECORD);
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

    @Override
    public void onItemClick(View view, int position) {
        
    }
}
