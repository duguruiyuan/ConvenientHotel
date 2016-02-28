package hotel.convenient.com.app;

import android.app.Activity;
import android.app.Application;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import hotel.convenient.com.BuildConfig;


/**
 * Created by Administrator on 2015/11/23.
 */
public class App extends Application{
    
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);

    }
    private List<Activity> activities = new ArrayList<Activity>();

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        
        for (Activity activity : activities) {
            if(activity!=null)
            activity.finish();
        }
        System.exit(0);
    }
    
}
