package hotel.convenient.com.view;


import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import hotel.convenient.com.R;


public class SearchLinearLayout extends LinearLayout implements OnClickListener{

	
	public static final int BT_SHOW_FOREVER=1;
	public static final int BT_GONE_FOREVER=2;
	public static final int BT_DEFAULT_FOREVER=0;
	private EditText et_products_search;
	private ImageView img_search_delete;
	private TextView tv_search_bt;
	private String text;
	private boolean search_bt_show = false;
	private String mHint;
	private Context context;
	private ImageView img_search;
	private int search_bt_show_forever = 0;
	private LinearLayout ll_searcher;
	private OnFocusChangeListener l;
	
	public void addTextChangedListener(TextWatcher mTextWatcher) {
		et_products_search.addTextChangedListener(mTextWatcher);
	}
	public String getmHint() {
		return mHint;
	}
	public void setSearchIconSize(int width,int height){
		LayoutParams params =  (LayoutParams) img_search.getLayoutParams();
		params.height = height;
		params.width = width;
		img_search.setLayoutParams(params);
	}
	/**
	 *  如果flag = true  表示需要监听焦点事件  获取焦点时 隐藏hint
	 * @param flag
	 */
	public void setEditOnFocusChangeListener(boolean flag){
		if(flag){
			if(l!=null){
				et_products_search.setOnFocusChangeListener(l);
			}
		}else{
			et_products_search.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					
				}
			});
		}
		
	}
	/**
	 * 是否改变输入法的状态   如果之前是自动弹出  现在就是隐藏
	 * @param flag   是否打开
	 */
	public void setEditFocus(boolean flag){
//		ll_searcher.setFocusableInTouchMode(!flag);
		if(flag){
			new Handler().postDelayed(new Runnable() {  
	            public void run() {  
                    InputMethodManager imm = (InputMethodManager)  
                    et_products_search.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
	            }  
	        }, 800);  
		}
	}
	/**
	 * 设置输入框的搜索文字
	 */
	public void setEditString(String keyword){
		et_products_search.setText(keyword);
	}
	/**
	 * 设置输入框的hint文字   
	 * @param mHint
	 */
	public void setHint(String mHint) {
		this.mHint = mHint;
		et_products_search.setHint(mHint);
	}
	public boolean isSearch_bt_show() {
		return search_bt_show;
	}
	/**
	 * 设置 搜索图案是否显示
	 * @param VISIBLE
	 */
	public void setImgSearchVisible(int VISIBLE){
		img_search.setVisibility(VISIBLE);
	}
	/**
	 * 设置按钮的名字
	 * @param name  按钮的名字
	 */
	public void setBtText(String name){
		tv_search_bt.setText(name);
	}
	/**
	 * 设置搜索按钮是否在输入框没数据的时候隐藏   默认false
	 * 为true时,按钮不隐藏
	 * @param search_bt_show
	 */
	public void setSearch_bt_show(boolean search_bt_show) {
		this.search_bt_show = search_bt_show;
		if(search_bt_show){
			tv_search_bt.setVisibility(View.VISIBLE);
		}else{
			tv_search_bt.setVisibility(View.GONE);
		}
		
	}
	/**
	 * 永久显示/隐藏按钮   
	 * @param search_bt_show_forever  BT_SHOW_FOREVER 表示永久显示  BT_GONE_FOREVER表示永久隐藏   BT_DEFAULT_FOREVER表示
	 */
	public void setSearch_bt_show_forever(int search_bt_show_forever) {
		this.search_bt_show_forever = search_bt_show_forever;
		if(search_bt_show_forever==BT_SHOW_FOREVER){
			tv_search_bt.setVisibility(View.VISIBLE);
			
		}else if(search_bt_show_forever==BT_GONE_FOREVER){
			tv_search_bt.setVisibility(View.GONE);
			
		}else if(search_bt_show_forever==BT_DEFAULT_FOREVER){
		}
	}
	/**
	 * 根据search_bt_show_forever 检测并更改bt显示状态
	 */
	public void checkSearch_bt_show_forever() {
		if(search_bt_show_forever==BT_SHOW_FOREVER){
			tv_search_bt.setVisibility(View.VISIBLE);
			
		}else if(search_bt_show_forever==BT_GONE_FOREVER){
			tv_search_bt.setVisibility(View.GONE);
			
		}else if(search_bt_show_forever==BT_DEFAULT_FOREVER){
		}
	}

	/**
	 * set输入框字体大小
	 * @param size  单位为dp
	 */
	public void setTextSize(int size){
		et_products_search.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
	}
	/**
	 * set整个搜索条目的高度
	 * @param hight
	 */
	public void setLinearLayoutHight(int hight){
		LayoutParams layoutParams = (LayoutParams) ll_searcher.getLayoutParams();
		layoutParams.height = hight;
		ll_searcher.setLayoutParams(layoutParams);
	}
	
	
	private OnSearchClick onSearchClick;

	public void setOnSearchClick(OnSearchClick onSearchClick) {
		this.onSearchClick = onSearchClick;
	}

	public interface OnSearchClick{
		public void click(String keyword);
	}
	
	public SearchLinearLayout(Context context) {
		this(context,null);
	}

	public SearchLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LinearLayout.inflate(context, R.layout.search_linearlayout, this);
		
		et_products_search = (EditText) findViewById(R.id.et_products_search);
		img_search_delete = (ImageView) findViewById(R.id.img_search_delete);
		tv_search_bt = (TextView) findViewById(R.id.tv_search_bt);
		img_search = (ImageView) findViewById(R.id.img_search);
		ll_searcher = (LinearLayout) findViewById(R.id.ll_searcher);
		tv_search_bt.setVisibility(View.GONE);
		init();
	}
	
	
	private void init() {
      	mHint = et_products_search.getHint().toString();
		tv_search_bt.setOnClickListener(this);
		l = new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
		          if (hasFocus) {
		              et_products_search.setHint("");
		          } else {
		        	  text = et_products_search.getText().toString();
		        	  if(!TextUtils.isEmpty(mHint)){
		        		  et_products_search.setHint(mHint);  
		        	  }
		          }
			}
		};
		et_products_search.setOnFocusChangeListener(l);
		et_products_search.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.length()==0){
					img_search_delete.setVisibility(View.INVISIBLE);
					if(!search_bt_show){
						tv_search_bt.setVisibility(View.GONE);
					}
					if(lengthLintener!=null){
						lengthLintener.editTextLengthTo0();
					}
					
				}else{
					img_search_delete.setVisibility(View.VISIBLE);
					tv_search_bt.setVisibility(View.VISIBLE);
				}
				checkSearch_bt_show_forever();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		img_search_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				et_products_search.setText("");
				if(lengthLintener!=null){
					lengthLintener.editTextLengthTo0();
				}
				
			}
		});
	}

	public EditText getEt_products_search() {
		return et_products_search;
	}

	public void setEt_products_search(EditText et_products_search) {
		this.et_products_search = et_products_search;
	}

	public ImageView getImg_search_delete() {
		return img_search_delete;
	}

	public void setImg_search_delete(ImageView img_search_delete) {
		this.img_search_delete = img_search_delete;
	}

	public TextView getTv_search_bt() {
		return tv_search_bt;
	}

	public void setTv_search_bt(TextView tv_search_bt) {
		this.tv_search_bt = tv_search_bt;
	}

	@Override
	public void onClick(View v) {
		if(onSearchClick!=null){
			text = et_products_search.getText().toString();
			onSearchClick.click(text);
		}
	}
	
	
	public interface OnEditLengthTo0Listener{
		public void editTextLengthTo0();
	}
	private OnEditLengthTo0Listener lengthLintener;

	public void setLengthLintener(OnEditLengthTo0Listener lengthLintener) {
		this.lengthLintener = lengthLintener;
	} 
	
}
