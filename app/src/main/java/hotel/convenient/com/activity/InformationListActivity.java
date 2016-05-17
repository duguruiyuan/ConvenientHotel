package hotel.convenient.com.activity;

import android.content.Intent;
import android.os.Bundle;

import hotel.convenient.com.R;
import hotel.convenient.com.base.BaseActivity;
import hotel.convenient.com.domain.Information;
import hotel.convenient.com.fragment.InformationListFragment;
import hotel.convenient.com.utils.LogUtils;


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
    
    public void startActivityByType(int type){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra(MainActivity.FLAG_SKIP,1);
        if (type == Information.TYPE_ORDER_ROOM) {
            intent.putExtra("tab", 1);
            LogUtils.e("tab"+1);
        } else if (type == Information.TYPE_PUBLISH){
            intent.putExtra("tab", 0);
            LogUtils.e("tab"+0);
        }
        startActivity(intent);
    }
}
