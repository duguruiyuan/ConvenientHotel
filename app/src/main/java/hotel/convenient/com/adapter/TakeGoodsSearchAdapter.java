package hotel.convenient.com.adapter;

import java.util.List;
import java.util.zip.Inflater;

import android.content.Context;
import android.text.Html;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import hotel.convenient.com.R;
import hotel.convenient.com.domain.SearchInfo;

public class TakeGoodsSearchAdapter extends BaseAdapter {

	Context context;
	List<SearchInfo> list;
	public TakeGoodsSearchAdapter(Context context, List<SearchInfo> datas) {
		this.context = context;
		list = datas;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.takegoods_address_search_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SearchInfo item = list.get(position);
		holder.tv_search_area.setText(item.getDetailAddress());
		String key = item.getSearch_key();
		String name =  item.getResult();
		if(name!=null){
			name = name.replaceAll(key, "<font color='#33aadd'>"+key+"</font>");
			holder.tv_search_address_other.setText(Html.fromHtml(name));
		}
		return convertView;
	}
	
	
	public static class ViewHolder {
		public TextView tv_search_area;
		public TextView tv_search_address_other;
		public ViewHolder(View convertView) {
			this.tv_search_area = (TextView) convertView.findViewById(R.id.tv_search_area);
			this.tv_search_address_other = (TextView) convertView.findViewById(R.id.tv_search_address_other);
		}
	}

}
