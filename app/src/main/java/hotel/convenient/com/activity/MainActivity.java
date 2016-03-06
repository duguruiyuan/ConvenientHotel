package hotel.convenient.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import hotel.convenient.com.R;
import hotel.convenient.com.adapter.MainViewPagerAdapter;
import hotel.convenient.com.base.BaseActivity;
import hotel.convenient.com.fragment.AccountFragment;
import hotel.convenient.com.fragment.MainFragment;
import hotel.convenient.com.fragment.MoreFragment;
import hotel.convenient.com.http.HostUrl;
import hotel.convenient.com.http.HttpUtils;
import hotel.convenient.com.http.ResultJson;
import hotel.convenient.com.http.SimpleCallback;
import hotel.convenient.com.utils.LogUtils;
import hotel.convenient.com.utils.PreferenceUtils;
import hotel.convenient.com.view.MainViewPager;

/**
 * Created by zhouyi on 2016/2/27.
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity  implements ViewPager.OnPageChangeListener{
    @ViewInject(R.id.vp_main_fragment)
    private MainViewPager viewPager;
    private List<Fragment> fragments = new ArrayList<>();
    private MainFragment mMainFragment; //主页
    public AccountFragment mAccountFragment; //账户页
    private MoreFragment mMoreFragment; //更多页
    @ViewInject(R.id.tv_main)
    private TextView tv_main;
    @ViewInject(R.id.tv_account)
    private TextView tv_account;
    @ViewInject(R.id.tv_more)
    private TextView tv_more;

    @Override
    public void initData(Bundle savedInstanceState) {
        //开启侧滑动画
        // openSlideAnimation();
        //设置标题
        setTitle("民宿商家版");
        tv_main.setBackgroundResource(R.mipmap.home_p);
        mMainFragment = new MainFragment();
        mAccountFragment = new AccountFragment();
        mMoreFragment = new MoreFragment();
        fragments.add(mMainFragment);
        fragments.add(mAccountFragment);
        fragments.add(mMoreFragment);
        viewPager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager(), fragments));
        //禁止滑动
        viewPager.setIsCanScroll(false);
        viewPager.addOnPageChangeListener(this);
        httpLogin();

    }
    /**
     * 访问网络以及处理返回结果
     */
    public void httpLogin(){
        String username = PreferenceUtils.getLoginUsername(this);
        String password = PreferenceUtils.getLoginPassword(this);

        RequestParams params = new RequestParams(HostUrl.HOST+ HostUrl.URL_LOGIN);
        params.addBodyParameter("phone",username);
        params.addBodyParameter("password",password);
        HttpUtils.post(params, new SimpleCallback(this,false) {
            @Override
            public <T> void simpleSuccess(String url, String result, ResultJson<T> resultJson) {
                if (resultJson.getCode() == CODE_SUCCESS) {

                } else {
                    baseActivity.showShortToast("登录超时");
                    PreferenceUtils.removeLoginFlag(baseActivity);
                    baseActivity.skipActivity(LoginActivity.class,false, LoginActivity.STATE_LOGOUT, LoginActivity.STATE_LOGOUT);
                }
            }
        });
    }
    public void setFooterIcon(int select){
        switch (select){
            case 0:
                tv_main.setBackgroundResource(R.mipmap.home_p);
                tv_account.setBackgroundResource(R.mipmap.my_n);
                tv_more.setBackgroundResource(R.mipmap.more_n);
                break;
            case 1:
                tv_main.setBackgroundResource(R.mipmap.home_n);
                tv_account.setBackgroundResource(R.mipmap.my_p);
                tv_more.setBackgroundResource(R.mipmap.more_n);
                break;
            case 2:
                tv_main.setBackgroundResource(R.mipmap.home_n);
                tv_account.setBackgroundResource(R.mipmap.my_n);
                tv_more.setBackgroundResource(R.mipmap.more_p);
                break;
            default:
                tv_main.setBackgroundResource(R.mipmap.home_p);
                tv_account.setBackgroundResource(R.mipmap.my_n);
                tv_more.setBackgroundResource(R.mipmap.more_n);
                break;
        }
    }

    /**
     * 显示菜单
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(PreferenceUtils.isLogin(this)){
            if(viewPager.getCurrentItem()==0)
                getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return true;
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
                onBackPressed();
                break;
            case R.id.action_publish:
                skipActivity(PublishActivity.class, false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setTitleByCurrentPage(){
        switch (viewPager.getCurrentItem()){
            case 0:
                setTitle("民宿商家版");
                break;
            case 1:
                setTitle("账户详情");
                break;
            case 2:
                setTitle("更多");
                break;
            default:
                setTitle("民宿商家版");
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
        //重新创建菜单
        supportInvalidateOptionsMenu();
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
    @Event({R.id.ll_more,R.id.ll_main,R.id.ll_account})
    private void onFragmentImageClick(View v) {
        switch (v.getId()){
            case R.id.ll_main:
                viewPager.setCurrentItem(0);
                break;
            case R.id.ll_account:
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
        //重新创建菜单
        supportInvalidateOptionsMenu();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
