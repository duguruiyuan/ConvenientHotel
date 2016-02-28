package hotel.convenient.com.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public class PickDialog extends Dialog
{

	private Context context;
	private View view;
	private int resource;
	private boolean flag = true;//true:关闭activity

	public PickDialog(Context context)
	{
		super(context);
		this.context = context;
	}

	public PickDialog(Context context, int theme, int resource)
	{
		super(context, theme);
		this.context = context;
		this.resource = resource;
	}
	public void setDismissFinishActivity(boolean flag){
		this.flag = flag;
	}
	public boolean getDismissFinishActivity(){
		return flag;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		view = View.inflate(context, resource, null);
		this.setContentView(view);
	}

	public View getView()
	{
		return view;
	}
}
