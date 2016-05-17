package hotel.convenient.com.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import hotel.convenient.com.R;
import hotel.convenient.com.adapter.GeneralTabViewPagerAdapter;
import hotel.convenient.com.base.BaseFragment;
import hotel.convenient.com.utils.LogUtils;

/**
 * 通用的tab  fragment
 * Created by cwy on 2015/12/11 11:31
 */
public class GeneralTabsFragment extends BaseFragment {
    @Bind(R.id.tabs)
    TabLayout mTab;
    @Bind(R.id.fragment_viewpager)
    ViewPager viewPager;
    
    protected List<String> titles = new ArrayList<>();
    protected List<Fragment> fragments = new ArrayList<>();
    private GeneralTabViewPagerAdapter adapter;

    @Override
    public int setLayoutView() {
        return R.layout.general_tab_fragment;
    }

    public void setCurrentTab(int item){
        mTab.setScrollPosition(0,item,true);
        viewPager.setCurrentItem(item);
    }
    @Override
    public void initData(View view, Bundle savedInstanceState) {
        if(titles==null||titles.size()==0){
            LogUtils.e("tabFragment未填充正确数据 需要自己添加");
            return;
        }
        initAdapter(titles,fragments);
    }

    /**
     * 若要请求网络后才能得到tab内容  可调用次方法初始化adapter
     * @param titles
     * @param fragments
     */
    public void initAdapter(List<String> titles, List<Fragment> fragments) {
        adapter = new GeneralTabViewPagerAdapter(getChildFragmentManager(), titles, fragments);
        viewPager.setAdapter(adapter);
        mTab.setTabsFromPagerAdapter(adapter);
        mTab.setupWithViewPager(viewPager);
    }

    /**
     * 更新数据
     */
    public void initAdapter() {
        adapter = new GeneralTabViewPagerAdapter(getChildFragmentManager(), titles, fragments);
        viewPager.setAdapter(adapter);
        mTab.setTabsFromPagerAdapter(adapter);
        mTab.setupWithViewPager(viewPager);
    }
}
