package hotel.convenient.com.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import hotel.convenient.com.R;


public class CustomProgressDialog extends Dialog {
	private Context context;
	private TextView tvMsg;

	public CustomProgressDialog(Context context, String strMessage) {
		this(context, R.style.CustomProgressDialog, strMessage);
	}

	public CustomProgressDialog(Context context, int theme, String strMessage) {
		super(context, theme);
		this.setContentView(R.layout.customprogressdialog);
		this.getWindow().getAttributes().gravity = Gravity.CENTER;
		tvMsg = (TextView) this.findViewById(R.id.id_tv_loadingmsg);
		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}
	}

	public CustomProgressDialog(Context context) {
		this(context, null);
	}

	public void setMessage(String message) {
		tvMsg.setText(message);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (!hasFocus) {
			dismiss();
		}
	}
}
