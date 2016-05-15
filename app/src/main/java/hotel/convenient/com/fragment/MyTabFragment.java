package hotel.convenient.com.fragment;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import hotel.convenient.com.utils.PreferenceUtils;

/**
 * Created by Gonyb on 2016/5/13 10:34
 */

public class MyTabFragment extends GeneralTabsFragment {
    private DealerFragment mDealerFragment; //商家页
    private UserRecordFragment mUserFragment; //用户页
    List<Fragment> tabsFragments = new ArrayList<>();
    private NotDealerFragment notDealerFragment;//未成为商家

    private List<String> titles = new ArrayList<>();
    public MyTabFragment() {
        super();
        mDealerFragment = new DealerFragment();
        mUserFragment = new UserRecordFragment();
        titles.add("我租到的房间");
        titles.add("我发布的房间");
        tabsFragments.add(mUserFragment);
        if (mBaseActivity == null) {
            tabsFragments.add(notDealerFragment);
        } else {
            if (PreferenceUtils.isLogin(getContext())) {
                tabsFragments.add(mDealerFragment);
            } else {
                tabsFragments.add(notDealerFragment);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initAdapter();
    }

    @Override
    public void initAdapter() {
        super.initAdapter();
    }

    public void setmDealerFragment(DealerFragment mDealerFragment) {
        this.mDealerFragment = mDealerFragment;
    }

    public void setmUserFragment(UserRecordFragment mUserFragment) {
        this.mUserFragment = mUserFragment;
    }
}
