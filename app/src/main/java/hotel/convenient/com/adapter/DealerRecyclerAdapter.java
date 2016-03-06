package hotel.convenient.com.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import hotel.convenient.com.R;
import hotel.convenient.com.domain.Publish;
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
        ImageUtils.setImage(publishHolder.imageHeader,publish.getUrl_head());
        publishHolder.llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v,position);
            }
        });
        publishHolder.roomName.setText(publish.getName());
        publishHolder.publishTime.setText(publish.getPublish_end_time());
    }

    class PublishHolder extends RecyclerView.ViewHolder{
        private CircleImageView imageHeader;
        private TextView roomName;
        private TextView publishTime;
        private LinearLayout llDelete;

        public PublishHolder(View itemView) {
            super(itemView);
            imageHeader = (CircleImageView) itemView.findViewById(R.id.image_header);
            roomName = (TextView) itemView.findViewById(R.id.room_name);
            publishTime = (TextView) itemView.findViewById(R.id.publish_time);
            llDelete = (LinearLayout) itemView.findViewById(R.id.ll_delete);
        }
    }
}
