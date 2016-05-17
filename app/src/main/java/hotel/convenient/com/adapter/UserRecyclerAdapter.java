package hotel.convenient.com.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import hotel.convenient.com.R;
import hotel.convenient.com.domain.UserRecord;

/**
 * Created by cwy on 2016/3/6 23:57
 */
public class UserRecyclerAdapter extends CommonRecyclerViewAdapter<UserRecyclerAdapter.UserRecordHolder, UserRecord> {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public UserRecyclerAdapter(List<UserRecord> list) {
        super(list);
    }

    @Override
    public int setLayoutItemResId() {
        return R.layout.item_user_veservation_information;
    }

    @Override
    public UserRecordHolder onCreateViewHolder(View view, int viewType) {
        return new UserRecordHolder(view);
    }

    @Override
    public void onBindViewHolderItem(RecyclerView.ViewHolder holder, final int position) {
        UserRecord userRecord = list.get(position);
        UserRecordHolder userRecordHolder = (UserRecordHolder) holder;
        userRecordHolder.dealerName.setText(userRecord.getDealer_name());
        userRecordHolder.roomAddress.setText(userRecord.getRoom_province()+userRecord.getRoom_city()+userRecord.getRoom_address_detail()+userRecord.getRoom_house_number());
        userRecordHolder.roomArea.setText(userRecord.getRoom_area());
        userRecordHolder.roomPrice.setText("¥"+userRecord.getRoom_price());
        userRecordHolder.time.setText(userRecord.getCreate_order_time());
    }

    static class UserRecordHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.dealer_name)
        TextView dealerName;
        @Bind(R.id.room_area)
        TextView roomArea;
        @Bind(R.id.room_price)
        TextView roomPrice;
        @Bind(R.id.room_address)
        TextView roomAddress;
        @Bind(R.id.time)
        TextView time;

        UserRecordHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
