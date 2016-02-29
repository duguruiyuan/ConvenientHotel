package hotel.convenient.com.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import hotel.convenient.com.R;

/**
 * Created by Gyb on 2016/2/29.
 */
public class ImageViewCanDelete extends RelativeLayout {
    private ImageView img;
    private LinearLayout delete;
    public interface OnDeleteClick{
        void onDeleteClick(View view);
    }
    private OnDeleteClick onDeleteClick;

    public void setOnDeleteClick(OnDeleteClick onDeleteClick) {
        this.onDeleteClick = onDeleteClick;
    }

    public ImageViewCanDelete(Context context) {
        super(context);
        initView(context);
    }

    public ImageViewCanDelete(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ImageViewCanDelete(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_can_delete, this);
        img = (ImageView) view.findViewById(R.id.img);
        delete = (LinearLayout) view.findViewById(R.id.delete);
        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteClick.onDeleteClick(ImageViewCanDelete.this);
            }
        });
    }
    public void setImageBitmap(Bitmap bitmap){
        setVisibility(VISIBLE);
        img.setImageBitmap(bitmap);
    }
}
