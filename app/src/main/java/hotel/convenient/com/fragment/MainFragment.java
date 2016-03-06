package hotel.convenient.com.fragment;

import android.os.Bundle;
import android.view.View;

import org.xutils.view.annotation.ContentView;

import java.util.List;

import hotel.convenient.com.R;
import hotel.convenient.com.adapter.CommonRecyclerViewAdapter;
import hotel.convenient.com.adapter.DealerRecyclerAdapter;
import hotel.convenient.com.domain.Publish;

/**
 * Created by Gyb on 2015/11/20.
 */
@ContentView(R.layout.main_fragment)
public class MainFragment extends RecyclerViewFragment<Publish>{

    @Override
    public CommonRecyclerViewAdapter createAdapter(List<Publish> list) {
        return new DealerRecyclerAdapter(list);
    }

    @Override
    public void setData(View view, Bundle savedInstanceState) {

    }
}
