package hotel.convenient.com.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Toast;

import org.xutils.x;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import hotel.convenient.com.app.App;
import hotel.convenient.com.R;
import hotel.convenient.com.utils.DensityUtils;
import hotel.convenient.com.utils.LogUtils;
import hotel.convenient.com.view.CustomProgressDialog;

/**
 * Created by Gyb on 2015/11/23.
 */
public abstract  class BaseActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CustomProgressDialog progressDialog;
    private static Toast toast; //保证不重复使用toast
    protected SwipeRefreshLayout mSwipe_main;
    private AlertDialog dialog;
    //通用的分页
    public static final int INIT_PAGE = 0;
    private int currentPage = INIT_PAGE;
    
    

    /**
     * 初始化page 一般在下拉刷新的时候调用
     * @return
     */
    public int initPage(){
        return currentPage = INIT_PAGE;
    }
    /**
     * 获取下一页 
     * @return
     */
    public int addPage(){
        return ++currentPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public abstract void initData(Bundle savedInstanceState);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        InjectUtils.inject(this);
        x.view().inject(this);
        ((App) getApplication()).addActivity(this);//方便一键退出
        setToolbar();
        initData(savedInstanceState);
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar==null){
            LogUtils.e("请在布局中使用Toolbar");
        }else{
            setSupportActionBar(toolbar);
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }
    
    /**
     * 显示回退按钮 
     */
    public void showBackPressed(){
        if(getSupportActionBar()==null){
            LogUtils.e("请在布局中添加toolbar 否则不能使用自带的回退功能");
            return;
        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    /**
     * 隐藏回退按钮 
     */
    public void hideBackpressed(){
        if(getSupportActionBar()==null){
            LogUtils.e("请在布局中添加toolbar 否则不能使用自带的回退功能");
            return;
        }
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    /**
     * 设置toolbar的标题
     * @param title
     */
    public void setTitle(String title){
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle(title);
        }
    }
    /**
     * 设置toolbar的标题
     * @param title
     */
    public void setTitle(Spanned title){
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle(title);
        }
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

    public AlertDialog showAlertDialog(String msg, String title, String ButtonName1, DialogInterface.OnClickListener listener1, String buttonName2, DialogInterface.OnClickListener listener2, View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        clearViewParent(view);
        if(view!=null) builder.setView(view, DensityUtils.dip2px(this, 26), DensityUtils.dip2px(this,12), DensityUtils.dip2px(this,26), DensityUtils.dip2px(this,12));
//        if(view!=null) builder.setView(view);
        builder.setMessage(msg);
        builder.setTitle(title);
        if(!TextUtils.isEmpty(ButtonName1)){
            builder.setPositiveButton(ButtonName1, listener1);
        }
        if(!TextUtils.isEmpty(buttonName2)){
            builder.setNegativeButton(buttonName2, listener2);
        }
        dialog = builder.show();
        return dialog;
    }
    public AlertDialog showAlertDialog(String msg,DialogInterface.OnClickListener listener1){
        return showAlertDialog(msg, "提示", "确定", listener1, "取消", null, null);
    }
    public AlertDialog showAlertDialog(String msg,DialogInterface.OnClickListener listener1,View view){
        return showAlertDialog(msg, "提示", "确定", listener1, "取消", null, view);
    }
    public AlertDialog showAlertDialog(String msg,String buttonName,View view){
        return showAlertDialog(msg, "提示", buttonName, null, null, null, view);
    }
    public void clearViewParent(View view) {
        if(view!=null){
            ViewParent parent = view.getParent();
            if(parent !=null){
                ((ViewGroup)parent).removeView(view);
            }
        }else {
            LogUtils.e("view不能为空");
        }
    }
    public void closeDialog(){
        if(dialog!=null){
            if(dialog.isShowing()){
                dialog.dismiss();
            }
        }
    }
    //关闭进度条
    public void stopProgress(){
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
    //显示进度条
    public void showProgress(){
        progressDialog = showProgressDialog("",false,true);
    }
    /**
     * 显示自定义进度条
     * @param message  需要显示的消息
     * @param canCancel 点击其他屏幕能取消
     * @param canBack 能被后退
     */
    public CustomProgressDialog showProgressDialog(String message, boolean canCancel, boolean canBack) {
        if (!TextUtils.isEmpty(message)) {
            progressDialog = new CustomProgressDialog(this,message);
        }else{
            progressDialog = new CustomProgressDialog(this,"正在努力加载...");
        }
        progressDialog.setCanceledOnTouchOutside(canCancel);
        progressDialog.setCancelable(canBack);
        progressDialog.show();
        return progressDialog;
    }
    
    public void showLongToast(String msg){
        if(TextUtils.isEmpty(msg)){
            return;
        }
        if (toast==null){
            toast = Toast.makeText(this,msg,Toast.LENGTH_LONG);
        }else{
            toast.setText(msg);
        }
        toast.show();
    }
    public void showShortToast(String msg){
        if(TextUtils.isEmpty(msg)){
            return;
        }
        if (toast==null){
            toast = Toast.makeText(this,msg,Toast.LENGTH_SHORT);
        }else{
            toast.setText(msg);
        }
        toast.show();
    }

    /**
     * 判断字符串是否为空
     * @param c 
     * @return true表示为空
     */
    public boolean isEmpty(String c){
        if(TextUtils.isEmpty(c)){
            return true;
        }
        return false;
    }

    /**
     * activity跳转  会默认finish  不想finish掉的同学请使用  {@link #skipActivity(Class, boolean)}
     * @param clazz 需要跳转的activity 的class
     */
    public void skipActivity(Class<?> clazz) {
        skipActivity(clazz, true);
    }

    /**
     * activity跳转
     * @param clazz 需要跳转的activity 的class
     * @param isFinish 是否销毁当前activity
     */
    public void skipActivity(Class<?> clazz,boolean isFinish) {
       skipActivity(clazz,isFinish,null);
    }
    /**
     * activity跳转
     * @param clazz 需要跳转的activity 的class
     * @param isFinish 是否销毁当前activity
     * @param paramKey 需要传递的参数 参数名
     * @param paramValue  参数值
     */
    public void skipActivity(Class<?> clazz,boolean isFinish,String paramKey,Serializable paramValue) {
        Intent intent = new Intent(BaseActivity.this,clazz);
        intent.putExtra(paramKey, paramValue);
        startActivity(intent);
        if(isFinish){
            finish();
        }
    }
    /**
     * activity跳转
     * @param clazz 需要跳转的activity 的class
     * @param isFinish 是否销毁当前activity
     * @param map 需要传递的参数
     */
    public void skipActivity(Class<?> clazz,boolean isFinish,Map<String,Object> map) {
        Intent intent = new Intent(BaseActivity.this,clazz);
        if(map!=null){
            Set<Map.Entry<String, Object>> entries = map.entrySet();
            for(Map.Entry<String, Object> e:entries){
                intent.putExtra(e.getKey(),e.getValue().toString());
            }
        }
        startActivity(intent);
        if(isFinish){
            finish();
        }
    }


    public void setRefreshing(boolean refreshing){
        if(mSwipe_main!=null)
            mSwipe_main.setRefreshing(refreshing);
    }
@Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
}
