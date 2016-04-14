package hotel.convenient.com.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import hotel.convenient.com.R;
import hotel.convenient.com.base.BaseActivity;
import hotel.convenient.com.utils.ImageUtils;
import hotel.convenient.com.utils.LogUtils;
import hotel.convenient.com.view.ZoomImageView;

/**
 * Created by Gyb on 2016/4/13 17:13
 */
public class PhotoActivity extends BaseActivity {
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    
    private String[] imageUrls;
    private ZoomImageView[] imageViews;
    public static final String PICK_IMAGE_POSITION = "PICK_IMAGE_POSITION";
    @Override
    public void initData(Bundle savedInstanceState) {
        
        imageUrls = (String[]) getIntent().getSerializableExtra("data");
        imageViews = new ZoomImageView[imageUrls.length];
        int current = getIntent().getIntExtra("PICK_IMAGE_POSITION",0);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imageUrls.length;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                LogUtils.e("position:"+position);
                ZoomImageView imageView = null;
                if (imageViews[position] != null) {
                    imageView = imageViews[position];
                } else {
                    imageView = new ZoomImageView(getApplicationContext());
                    ImageUtils.setImage(imageView,imageUrls[position]);
                    imageViews[position] = imageView;
                }
                container.addView(imageView);
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(imageViews[position]);
            }
            
            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }
        });
        viewPager.setCurrentItem(current);
    }

    @Override
    public int setLayoutView() {
        return R.layout.photo;
    }
}
