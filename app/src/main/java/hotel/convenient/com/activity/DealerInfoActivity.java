package hotel.convenient.com.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import hotel.convenient.com.R;
import hotel.convenient.com.base.BaseActivity;
import hotel.convenient.com.domain.ChooseDealerInfo;
import hotel.convenient.com.domain.Publish;
import hotel.convenient.com.http.HostUrl;
import hotel.convenient.com.utils.ImageUtils;

/**
 * Created by Administrator on 2015/11/16.
 */
public class DealerInfoActivity extends BaseActivity{
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appbar;
    private ChooseDealerInfo dealerInfo;
    @Bind(R.id.tv_info)
    TextView tv_info;
    @Bind(R.id.main_image)
    ImageView main_image;

    @Override
    public void initData(Bundle savedInstanceState) {
        dealerInfo = (ChooseDealerInfo) getIntent().getSerializableExtra("data");
        initView();
        initData();
    }

    @Override
    public int setLayoutView() {
        return R.layout.dealer_info_activity;
    }

    private void initData() {
        Publish publish = dealerInfo.getPublish();
        collapsingToolbarLayout.setTitle(publish.getDealer_name());
        if(publish.getImage_name().indexOf(",")==-1){
            ImageUtils.setImage(main_image, HostUrl.HOST+"/"+publish.getDir_path()+"/"+publish.getImage_name(),R.drawable.mei_zi);
        }else{
            ImageUtils.setImage(main_image,HostUrl.HOST+"/"+publish.getDir_path()+"/"+publish.getImage_name().split(",")[0],R.drawable.mei_zi);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.GREEN);//设置收缩后Toolbar上字体的颜色
    }
    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.ctl_collapsing);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
