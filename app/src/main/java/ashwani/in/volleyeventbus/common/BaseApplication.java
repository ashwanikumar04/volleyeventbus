package ashwani.in.volleyeventbus.common;


import android.app.Application;

import ashwani.in.volleyeventbus.managers.VolleyManager;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VolleyManager.initialize(this);
    }
}
