package hotel.convenient.com.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.Bind;
import hotel.convenient.com.R;
import hotel.convenient.com.adapter.PhotoAdapter;
import hotel.convenient.com.base.BaseActivity;
import hotel.convenient.com.domain.Publish;
import hotel.convenient.com.http.HostUrl;
import hotel.convenient.com.utils.LogUtils;
import hotel.convenient.com.view.DividerGridItemDecoration;

/**
 * Created by cwy on 2016/4/12 14:37
 */
public class PreviewRoomPhotoActivity extends BaseActivity {
    @Bind(R.id.recycler)
    RecyclerView recyclerView;
    private String[] imageUrls;
    @Override
    public void initData(Bundle savedInstanceState) {
        Publish publish = (Publish) getIntent().getSerializableExtra("data");
        showBackPressed();
        setTitle(publish.getDealer_name());
        
        imageUrls = new String[publish.getImage_name().split(",").length];
        int i = 0;
        for(String name:publish.getImage_name().split(",")){
            String url = HostUrl.HOST + publish.getDir_path() + "/" + name;
            LogUtils.e("url:"+url);
            imageUrls[i] = url;
            i++;
        }
        recyclerView.setAdapter(new PhotoAdapter(imageUrls));
//        StaggeredGridLayoutManager layout = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));//设置分割线
    }

    @Override
    public int setLayoutView() {
        return R.layout.preview_photo_activity;
    }
}
