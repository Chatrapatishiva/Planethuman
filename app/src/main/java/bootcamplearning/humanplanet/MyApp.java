package bootcamplearning.humanplanet;

import android.app.Application;
import android.content.Context;

/**
 * Created by Shiva on 01-11-2016.
 */
public class MyApp extends Application {
    private static MyApp instance;
    public static MyApp getInstance(){
        return instance;
    }
    public static Context getContext(){
        return instance;
    }
    @Override
    public void onCreate(){
        instance=this;
        super.onCreate();
    }
}
