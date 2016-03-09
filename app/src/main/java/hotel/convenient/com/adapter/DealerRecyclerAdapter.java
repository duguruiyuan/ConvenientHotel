package hotel.convenient.com.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import hotel.convenient.com.R;
import hotel.convenient.com.domain.Publish;
import hotel.convenient.com.http.HostUrl;
import hotel.convenient.com.utils.ImageUtils;
import hotel.convenient.com.view.CircleImageView;

/**
 * Created by Gyb on 2016/3/6 23:57
 */
public class DealerRecyclerAdapter extends CommonRecyclerViewAdapter<DealerRecyclerAdapter.PublishHolder,Publish> {


    public DealerRecyclerAdapter(List<Publish> list) {
        super(list);
    }

    @Override
    public int setLayoutItemResId() {
        return R.layout.dealer_recycler_view_item;
    }

    @Override
    public PublishHolder onCreateViewHolder(View view, int viewType) {
        return new PublishHolder(view);
    }

    @Override
    public void onBindViewHolderItem(RecyclerView.ViewHolder holder, final int position) {
        Publish publish = list.get(position);
        PublishHolder publishHolder = (PublishHolder) holder;
        ImageUtils.setImage(publishHolder.imageHeader,HostUrl.HOST+publish.getUrl_head(),R.drawable.mei_zi);
        publishHolder.llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(v,position);
                }
            }
        });
        publishHolder.roomName.setText(publish.getName());
        publishHolder.publishTime.setText(publish.getPublish_time());
        if(publish.getImage_name().indexOf(",")==-1){
            ImageUtils.setImage(publishHolder.room_image,HostUrl.HOST+"/"+publish.getDir_path()+"/"+publish.getImage_name(),R.drawable.mei_zi);
        }else{
            ImageUtils.setImage(publishHolder.room_image,HostUrl.HOST+"/"+publish.getDir_path()+"/"+publish.getImage_name().split(",")[0],R.drawable.mei_zi);
        }
        publishHolder.room_description.setText(publish.getDescription());
    }

    class PublishHolder extends RecyclerView.ViewHolder{
        private CircleImageView imageHeader;
        private TextView roomName;
        private TextView publishTime;
        private LinearLayout llDelete;
        private ImageView room_image;
        private TextView room_description;

        public PublishHolder(View itemView) {
            super(itemView);
            imageHeader = (CircleImageView) itemView.findViewById(R.id.image_header);
            roomName = (TextView) itemView.findViewById(R.id.room_name);
            publishTime = (TextView) itemView.findViewById(R.id.publish_time);
            llDelete = (LinearLayout) itemView.findViewById(R.id.ll_delete);
            room_image = (ImageView) itemView.findViewById(R.id.room_image);
            room_description = (TextView) itemView.findViewById(R.id.room_description);
        }
    }
}
