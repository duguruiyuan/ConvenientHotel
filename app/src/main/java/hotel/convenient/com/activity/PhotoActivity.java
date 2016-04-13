package hotel.convenient.com.activity;

import android.os.Bundle;
import android.widget.ImageView;

import butterknife.Bind;
import hotel.convenient.com.R;
import hotel.convenient.com.base.BaseActivity;
import hotel.convenient.com.utils.ImageUtils;

/**
 * Created by Gyb on 2016/4/13 17:13
 */
public class PhotoActivity extends BaseActivity {
    private String[] imageUrls;
    @Bind(R.id.image)
    ImageView imageView;
    public static final String PICK_IMAGE_POSITION = "PICK_IMAGE_POSITION";
    @Override
    public void initData(Bundle savedInstanceState) {
        imageUrls = (String[]) getIntent().getSerializableExtra("data");
        int current = getIntent().getIntExtra("PICK_IMAGE_POSITION",0);
        ImageUtils.setImage(imageView,imageUrls[current]);
    }

    @Override
    public int setLayoutView() {
        return R.layout.photo;
    }
}
