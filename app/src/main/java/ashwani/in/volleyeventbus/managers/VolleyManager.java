package ashwani.in.volleyeventbus.managers;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyManager {

    private static VolleyManager instance;
    private RequestQueue mRequestQueue;

    public synchronized static void initialize(Context context) {
        if (instance == null) {
            instance = new VolleyManager();
            instance.mRequestQueue = Volley.newRequestQueue(context);
        }
    }


    public synchronized static VolleyManager getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }
}