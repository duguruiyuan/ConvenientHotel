package hotel.convenient.com.activity;

import android.os.Bundle;


import butterknife.Bind;
import hotel.convenient.com.R;
import hotel.convenient.com.base.BaseActivity;
import hotel.convenient.com.utils.VersionUtils;
import hotel.convenient.com.view.LinearLayoutMenu;

public class AboutActivity extends BaseActivity {
    @Bind(R.id.about_version)
    LinearLayoutMenu about_version;
    
    @Override
    public void initData(Bundle savedInstanceState) {
         setTitle("关于");
        showBackPressed();
        String versionName = VersionUtils.getVersionName(this);
        about_version.setMsg(versionName);
    }

    @Override
    public int setLayoutView() {
        return R.layout.about_activity;
    }
}
