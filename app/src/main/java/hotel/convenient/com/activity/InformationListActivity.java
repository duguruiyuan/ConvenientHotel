package hotel.convenient.com.activity;

import android.os.Bundle;

import hotel.convenient.com.R;
import hotel.convenient.com.base.BaseActivity;
import hotel.convenient.com.fragment.InformationListFragment;


/**
 * 站内信
 * Created by cwy on 2015/12/9 16:14
 */
public class InformationListActivity extends BaseActivity {

    private InformationListFragment informationListFragment;

    @Override
    public void initData(Bundle savedInstanceState) {
        setTitle("站内信息");
        showBackPressed();
        informationListFragment = new InformationListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.recyclerView_fragment,informationListFragment).commit();
    }

    @Override
    public int setLayoutView() {
        return R.layout.recyclerview_activity;
    }
}
