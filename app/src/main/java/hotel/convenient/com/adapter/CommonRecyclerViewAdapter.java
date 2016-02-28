package hotel.convenient.com.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import hotel.convenient.com.R;

/**
 * 通用的上拉刷新 下拉加载recyclerView的适配器  抽象类
 * Created by Gyb on 2015/12/17 10:20
 */
public abstract class CommonRecyclerViewAdapter<T extends RecyclerView.ViewHolder,A> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected OnItemClickListener mOnItemClickListener;
    public  interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * 用来监听点击事件的接口
     * @param mOnItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
    protected List<A> list;
    //脚
    public static final int TYPE_ITEM = 0;
    public static final int TYPE_FOOTER = 1;

    private TextView footer;
    private View.OnClickListener listener;

    public TextView getFooter() {
        return footer;
    }

    public void setFooterName(String name){
        if(footer!=null){
//            footer.setVisibility(View.VISIBLE);
            footer.setText(name);
        }
    }
    public void setFooterOnClickListener(View.OnClickListener listener){
        this.listener = listener;
        if(footer!=null){
            footer.setOnClickListener(listener);
        }
    }
    public void setFooterGone(){
        if(footer!=null){
            footer.setVisibility(View.GONE);
        }
    }

    public List<A> getList() {
        return list;
    }

    // RecyclerView的count设置为数据总条数+ 1（footerView）  
    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView  
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public CommonRecyclerViewAdapter(List<A> list) {
        this.list = list;
    }
    public CommonRecyclerViewAdapter(List<A> list, OnItemClickListener listener) {
        this.list = list;
        this.mOnItemClickListener = listener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_FOOTER){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_footer,parent,false);
            return new FooterViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(setLayoutItemResId(),parent,false);
            return onCreateViewHolder(view,viewType);
        }
    }

    public abstract int setLayoutItemResId();
    public abstract T onCreateViewHolder(View view,int viewType);
    
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        if(holder instanceof CommonRecyclerViewAdapter.FooterViewHolder){
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footer = footerViewHolder.footer;
            if(listener!=null)
                footer.setOnClickListener(listener);
        }else {
            if(list.size()>position)
            onBindViewHolderItem(holder,position);
        }
        
    };

    public abstract void onBindViewHolderItem(RecyclerView.ViewHolder holder, int position);

    class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView footer;
        public FooterViewHolder(View view) {
            super(view);
            footer = (TextView) view.findViewById(R.id.footer);
        }
    }
}
