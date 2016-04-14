package hotel.convenient.com.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import hotel.convenient.com.R;
import hotel.convenient.com.activity.PhotoActivity;
import hotel.convenient.com.utils.ImageUtils;

/**
 * Created by Gyb on 2016/4/13 13:51
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private String[] imageUrls;

    private int height ;
    public PhotoAdapter(String[] imageUrls) {
        this.imageUrls = imageUrls;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent, false);
        return new PhotoViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {
        ImageUtils.setImage(holder.imageView,imageUrls[position]);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), PhotoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("data",imageUrls);
                intent.putExtra(PhotoActivity.PICK_IMAGE_POSITION,position);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUrls.length;
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public PhotoViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                    layoutParams.height = imageView.getWidth();
                    imageView.setLayoutParams(layoutParams);
                }
            });
        }
    }
}
