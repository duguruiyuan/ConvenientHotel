package hotel.convenient.com.app;

import android.app.Application;


/**
 * Created by Administrator on 2015/11/23.
 */
public class App extends Application{
    private static App app;
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }
    public static App getInstanceApp(){
        return app;
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        System.exit(0);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}
