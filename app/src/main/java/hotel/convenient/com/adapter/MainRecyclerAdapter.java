package hotel.convenient.com.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hotel.convenient.com.R;
import hotel.convenient.com.domain.Publish;
import hotel.convenient.com.http.HostUrl;
import hotel.convenient.com.utils.DistanceUtils;
import hotel.convenient.com.utils.ImageUtils;
import hotel.convenient.com.utils.PreferenceUtils;

/**
 * Created by Gyb on 2016/3/6 23:57
 */
public class MainRecyclerAdapter extends CommonRecyclerViewAdapter<MainRecyclerAdapter.PublishHolder,Publish> {


    public MainRecyclerAdapter(List<Publish> list) {
        super(list);
    }

    @Override
    public int setLayoutItemResId() {
        return R.layout.main_recycler_view_item;
    }

    @Override
    public PublishHolder onCreateViewHolder(View view, int viewType) {
        return new PublishHolder(view);
    }

    @Override
    public void onBindViewHolderItem(RecyclerView.ViewHolder holder, final int position) {
        Publish publish = list.get(position);
        PublishHolder publishHolder = (PublishHolder) holder;
        if(publish.getImage_name().indexOf(",")==-1){
            ImageUtils.setImage(publishHolder.roomImage,HostUrl.HOST+"/"+publish.getDir_path()+"/"+publish.getImage_name(),R.drawable.mei_zi);
        }else{
            ImageUtils.setImage(publishHolder.roomImage,HostUrl.HOST+"/"+publish.getDir_path()+"/"+publish.getImage_name().split(",")[0],R.drawable.mei_zi);
        }
        
        double distance = DistanceUtils.getDistance(publishHolder.lat, publishHolder.lng, publish.getLatitude(), publish.getLongitude());
        if (distance > 0) {
            distance = Math.round(distance*100)/100;
            publishHolder.roomDistance.setText(distance + "km");
        } else {
            distance = Math.round(distance*100*1000)/100;
            publishHolder.roomDistance.setText(distance + "m");
        }
        publishHolder.dealerName.setText(publish.getDealer_name());
        publishHolder.roomPrice.setText(publish.getRoom_price());
        publishHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v,position);
            }
        });
    }

    class PublishHolder extends RecyclerView.ViewHolder{
        private ImageView roomImage;
        private TextView dealerName;
        private TextView roomPrice;
        private TextView roomDistance;
        private double lat;
        private double lng;
        public PublishHolder(View itemView) {
            super(itemView);
            roomImage = (ImageView) itemView.findViewById(R.id.room_image);
            dealerName = (TextView) itemView.findViewById(R.id.dealer_name);
            roomPrice = (TextView) itemView.findViewById(R.id.room_price);
            roomDistance = (TextView) itemView.findViewById(R.id.room_distance);
            lat = Double.parseDouble(PreferenceUtils.getLat(itemView.getContext()));
            lng = Double.parseDouble(PreferenceUtils.getLng(itemView.getContext()));
        }
    }
}
