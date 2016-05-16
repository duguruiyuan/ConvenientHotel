package hotel.convenient.com.fragment;

import hotel.convenient.com.app.App;

/**
 * Created by cwy on 2016/5/13 10:34
 */

public class MyTabFragment extends GeneralTabsFragment {
    private DealerFragment mDealerFragment; //商家页
    private UserRecordFragment mUserFragment; //用户页
    private NotDealerFragment notDealerFragment;//未成为商家

    public MyTabFragment() {
        super();
        mDealerFragment = new DealerFragment();
        mUserFragment = new UserRecordFragment();
        notDealerFragment = new NotDealerFragment();
        notDealerFragment.setiBecomeDealer(new NotDealerFragment.IBecomeDealer() {
            @Override
            public void becomeDealer() {
                App.getInstanceApp().getDealer().setType(1);
                fragments.remove(1);
                fragments.add(mDealerFragment);
                initAdapter();
            }
        });
        titles.add("我租到的房间");
        titles.add("我发布的房间");
        setTabsFragments();
    }
    void setTabsFragments(){
        fragments.clear();
        fragments.add(mUserFragment);
        if (App.getInstanceApp().getDealer() != null && App.getInstanceApp().getDealer().getType()==1 ) {
            fragments.add(mDealerFragment);
        } else {
            fragments.add(notDealerFragment);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setTabsFragments();
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
