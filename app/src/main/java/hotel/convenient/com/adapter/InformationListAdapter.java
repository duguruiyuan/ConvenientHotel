package hotel.convenient.com.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import hotel.convenient.com.R;
import hotel.convenient.com.domain.Information;

/**
 * Created by jayl on 2015/11/26.
 */
public class InformationListAdapter extends CommonRecyclerViewAdapter<InformationListAdapter.MyViewHolder,Information>{
   
    public InformationListAdapter(List<Information> list) {
        super(list);
    }

    @Override
    public int setLayoutItemResId() {
        return R.layout.item_inner_mess_list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(View view,int type) {
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolderItem(RecyclerView.ViewHolder holder1, final int position) {
        MyViewHolder holder = (MyViewHolder) holder1;
        holder.tv_title.setText(list.get(position).getTitle());
        holder.tv_time.setText(list.get(position).getCreate_time());
        holder.tv_article.setText(list.get(position).getMessage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener !=null){
                    mOnItemClickListener.onItemClick(v, position);
                }
            }
        });
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_title;
        public TextView tv_type;
        public TextView tv_time;
        public TextView tv_article;
//        public ImageView iv_redpot;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_title= (TextView) itemView.findViewById(R.id.tv_title);
            tv_type= (TextView) itemView.findViewById(R.id.tv_type);
            tv_time= (TextView) itemView.findViewById(R.id.tv_time);
            tv_article= (TextView) itemView.findViewById(R.id.tv_article);

        }
    }
}
