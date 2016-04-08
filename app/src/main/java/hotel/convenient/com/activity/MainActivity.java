package hotel.convenient.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import hotel.convenient.com.R;
import hotel.convenient.com.adapter.MainViewPagerAdapter;
import hotel.convenient.com.base.BaseActivity;
import hotel.convenient.com.fragment.DealerFragment;
import hotel.convenient.com.fragment.MainFragment;
import hotel.convenient.com.fragment.MoreFragment;
import hotel.convenient.com.http.HostUrl;
import hotel.convenient.com.http.HttpUtils;
import hotel.convenient.com.http.RequestParams;
import hotel.convenient.com.http.ResultJson;
import hotel.convenient.com.http.SimpleCallback;
import hotel.convenient.com.utils.LogUtils;
import hotel.convenient.com.utils.PreferenceUtils;
import hotel.convenient.com.view.CircleImageView;
import hotel.convenient.com.view.MainViewPager;

/**
 * Created by Gonyb on 2016/2/27.
 */
public class MainActivity extends BaseActivity  implements ViewPager.OnPageChangeListener{
    @Bind(R.id.vp_main_fragment)
    MainViewPager viewPager;
    private List<Fragment> fragments = new ArrayList<>();
    private MainFragment mMainFragment; //主页
    private DealerFragment mDealerFragment; //商家页
    private MoreFragment mMoreFragment; //更多页
    @Bind(R.id.tv_main)
    TextView tv_main;
    @Bind(R.id.tv_dealer)
    TextView tv_dealer;
    @Bind(R.id.tv_more)
    TextView tv_more;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CircleImageView headImage;
    private TextView username;
    private TextView phone;
    private NavigationView navigationView;
    
    public static final String FLAG_SKIP = "flag_skip";

    @Override                                                          
    public void initData(Bundle savedInstanceState) {
        //开启侧滑动画
         openSlideAnimation();
        //设置标题
        setTitle("首页");
        tv_main.setBackgroundResource(R.mipmap.home_p);
        mMainFragment = new MainFragment();
        mDealerFragment = new DealerFragment();
        mMoreFragment = new MoreFragment();
        fragments.add(mMainFragment);
        fragments.add(mDealerFragment);
        fragments.add(mMoreFragment);
        viewPager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager(), fragments));
        //禁止滑动
        viewPager.setIsCanScroll(false);
        viewPager.addOnPageChangeListener(this);
        
        
        //如果在手机中是登录状态  则自动登录    每次应用启动时做的动作 
        if(PreferenceUtils.isLogin(this)){
            LoginActivity.httpLoginByPreference(this);
        }
    }

    @Override
    public int setLayoutView() {
        return R.layout.activity_main;
    }

    public void setFooterIcon(int select){
        switch (select){
            case 0:
                tv_main.setBackgroundResource(R.mipmap.home_p);
                tv_dealer.setBackgroundResource(R.mipmap.my_n);
                tv_more.setBackgroundResource(R.mipmap.more_n);
                break;
            case 1:
                tv_main.setBackgroundResource(R.mipmap.home_n);
                tv_dealer.setBackgroundResource(R.mipmap.my_p);
                tv_more.setBackgroundResource(R.mipmap.more_n);
                break;
            case 2:
                tv_main.setBackgroundResource(R.mipmap.home_n);
                tv_dealer.setBackgroundResource(R.mipmap.my_n);
                tv_more.setBackgroundResource(R.mipmap.more_p);
                break;
            default:
                tv_main.setBackgroundResource(R.mipmap.home_p);
                tv_dealer.setBackgroundResource(R.mipmap.my_n);
                tv_more.setBackgroundResource(R.mipmap.more_n);
                break;
        }
    }
    /**
     * 开启侧滑动画  不设置则不打开
     */
    public void openSlideAnimation() {
        //动态侧滑菜单
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(mDrawerLayout==null||getToolbar()==null){
            LogUtils.d("你没有使用DrawerLayout 或 Toolbar 不能开启侧滑动画");
            return;
        }
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,getToolbar(),R.string.draw_open,R.string.draw_close){
            public void onDrawerClosed(View view){
                invalidateOptionsMenu(); // creates call to                                                    // onPrepareOptionsMenu()            }
                /** Called when a drawer has settled in a completely open state. */
            }
            public void onDrawerOpened(View drawerView){
                invalidateOptionsMenu(); // creates call to                                                    // onPrepareOptionsMenu()            }
            }
        };
        mDrawerToggle.syncState(); //初始化状态
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Snackbar.make(getToolbar(), item.getTitle() + " pressed", Snackbar.LENGTH_LONG).show();
                switch (item.getItemId()){
                    case R.id.logout:
                        logout();
                        break;
                    case R.id.drawer_home:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.action_publish:
                        skipActivity(PublishActivity.class, false);
                        break;
                }
//                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        View headerView = navigationView.getHeaderView(0);
        headImage = (CircleImageView) headerView.findViewById(R.id.head_image);
        username = (TextView) headerView.findViewById(R.id.username);
        phone = (TextView) headerView.findViewById(R.id.phone);

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showAlertDialog("")
                mDrawerLayout.closeDrawers();
            }
        });
        setHeadViewInfo();
    }
    private void logout(){
        RequestParams params = new RequestParams(HostUrl.HOST+HostUrl.URL_LOGOUT);
        HttpUtils.get(params, new SimpleCallback() {
            @Override
            public <T> void simpleSuccess(String url, String result, ResultJson<T> resultJson) {
                if (resultJson.getCode() == CODE_SUCCESS) {
                    PreferenceUtils.removeLoginFlag(MainActivity.this);
                    setHeadViewInfo();
                    setCurrentItem(0);
                } 
                showShortToast(resultJson.getMsg());
            }
        });
    }

    private void setHeadViewInfo() {
        if(PreferenceUtils.isLogin(this)){
            username.setText(PreferenceUtils.getLoginUsername(this));
            phone.setText(PreferenceUtils.getPhone(this));
            navigationView.getMenu().getItem(3).setVisible(true);
            navigationView.getMenu().getItem(1).setVisible(true);
        }else{
            username.setText("游客");
            phone.setText("");
            navigationView.getMenu().getItem(1).setVisible(false);
            navigationView.getMenu().getItem(3).setVisible(false);
        }
    }
    /**
     * 监听菜单点击
     * @param item
     * @return
     *
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //返回按钮
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_publish:
                skipActivity(PublishActivity.class, false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setTitleByCurrentPage(){
        switch (viewPager.getCurrentItem()){
            case 0:
                setTitle("首页");
                break;
            case 1:
                setTitle("商家");
                break;
            case 3:
                setTitle("更多");
                break;
            default:
                setTitle("首页");
                break;
        }
    }
    /**
     * 由于mainActivity 的启动模式为 singleTask  intent创建activity时只会执行onNewIntent方法 而不会执行 oncreate方法
     * 使用在这里处理传递过来的intent
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtils.e("onNewIntent");
        int intExtra = getIntent().getIntExtra(FLAG_SKIP, -1);
        if(intExtra!=-1){
            viewPager.setCurrentItem(intExtra);
        }
        setHeadViewInfo();
    }
    public void setCurrentItem(int item){
        viewPager.setCurrentItem(item);
    }


    /**
     * 注解点击事件
     * 1. 方法必须私有限定,方法必须私有限定,方法必须私有限定 重要的事说三遍
     * 2. 方法以Click或Event结尾, 方便配置混淆编译参数 :
     * -keepattributes *Annotation*
     * -keepclassmembers class * {
     * void *(android.view.View);
     * *** *Click(...);
     * *** *Event(...);
     * }
     * 3. 方法参数形式必须和type对应的Listener接口一致.
     * 4. 注解参数value支持数组: value={id1, id2, id3}
     **/
    @OnClick({R.id.ll_more,R.id.ll_main,R.id.ll_dealer})
     void onFragmentImageClick(View v) {
        switch (v.getId()){
            case R.id.ll_main:
                viewPager.setCurrentItem(0);
                break;
            case R.id.ll_dealer:
                if (PreferenceUtils.isLogin(this)) {
                    viewPager.setCurrentItem(1);
                } else {
                    skipActivity(LoginActivity.class,false);
                }
                break;
            case R.id.ll_more:
                if (PreferenceUtils.isLogin(this)) {
                    viewPager.setCurrentItem(2);
                } else {
                    skipActivity(LoginActivity.class,false);
                }
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setTitleByCurrentPage();
        setFooterIcon(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
