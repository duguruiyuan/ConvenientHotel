package hotel.convenient.com.utils;

import android.app.Activity;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import hotel.convenient.com.R;
import hotel.convenient.com.view.PickDialog;
import hotel.convenient.com.view.SelectDialog;


public class ShowAnimationDialogUtil {
	/**
	 * 显示从下至上弹出对话框
	 * 
	 * @param activity
	 * @param resId
	 *            自定义dialog资源文件id
	 * @return
	 */
	public static PickDialog showDialog(Activity activity, int resId) {
		PickDialog dialog = new PickDialog(activity, R.style.CityPickDialog,
				resId);
		Window window = dialog.getWindow();
		window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
		window.setWindowAnimations(R.style.CityPickDialogAnimation); // 添加动画
		dialog.show();

		WindowManager windowManager = activity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (int) (display.getWidth()); // 设置宽度
		dialog.getWindow().setAttributes(lp);
		return dialog;
	}

	//
	public static PickDialog showDialogFromTop(Activity activity, int resId) {
		PickDialog dialog = new PickDialog(activity, R.style.CityPickDialog,
				resId);
		Window window = dialog.getWindow();
		window.setGravity(Gravity.TOP); // 此处可以设置dialog显示的位置
		window.setWindowAnimations(R.style.CityPickDialogAnimation); // 添加动画
		dialog.show();

		WindowManager windowManager = activity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (int) (display.getWidth()); // 设置宽度
		dialog.getWindow().setAttributes(lp);
		return dialog;
	}

	/**
	 * 显示从下至上弹出对话框
	 * 
	 * @param activity
	 * @return
	 */
	public static SelectDialog showDialog(Activity activity, View view) {
		SelectDialog dialog = new SelectDialog(activity,
				R.style.CityPickDialog, view);
		Window window = dialog.getWindow();
		window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
		window.setWindowAnimations(R.style.CityPickDialogAnimation); // 添加动画
		dialog.show();

		WindowManager windowManager = activity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (int) (display.getWidth()); // 设置宽度
		dialog.getWindow().setAttributes(lp);
		return dialog;
	}
}
