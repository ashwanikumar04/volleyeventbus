package ashwani.in.volleyeventbus.managers;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import ashwani.in.volleyeventbus.entities.ErrorData;
import ashwani.in.volleyeventbus.events.error.BaseErrorEvent;
import ashwani.in.volleyeventbus.events.success.BaseSuccessEvent;
import de.greenrobot.event.EventBus;

public class HTTPRequestManager<P extends BaseSuccessEvent, Q extends BaseErrorEvent> {

    public static final String TAG = HTTPRequestManager.class.getSimpleName();
    public boolean isBaseActivity;
    ProgressDialog progressDialog;
    int responseCode;
    ErrorData errorData;
    Response.Listener<JSONObject> jsonResponseListener;
    Response.ErrorListener errorListener;
    EventBus eventBus = EventBus.getDefault();
    private HashMap<String, String> params;
    private HashMap<String, String> headers;
    private String url;
    private Context context;
    private Class<P> pClass;
    private Class<Q> qClass;

    public HTTPRequestManager(Context context, String url, Class<P> pClass, Class<Q> qClass) {
        this.context = context;
        this.url = url;
        params = new HashMap<>();
        headers = new HashMap<>();
        addJSONHeaders();
        errorData = new ErrorData();
        isBaseActivity = true;
        this.pClass = pClass;
        this.qClass = qClass;
        setListeners();

    }

    private void addJSONHeaders() {
        AddHeader("Accept", "application/json");
        AddHeader("Content-type", "application/json");
    }

    private void setListeners() {
        jsonResponseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissProgressDialog();
                try {
                    P res = pClass.newInstance();
                    res.setResponse(response.toString());
                    eventBus.post(res);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgressDialog();
                try {
                    Q err = qClass.newInstance();
                    ErrorData errorData = new ErrorData();
                    errorData.setMessage(error.toString());
                    err.setErrorData(errorData);
                    eventBus.post(err);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

    }


    public HashMap<String, String> getParams() {
        return params;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }


    public String getCompleteUrl() {
        return url;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public ErrorData getErrorData() {
        return errorData;
    }

    public void execute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        dismissProgressDialog();
        progressDialog.show();
        addToRequestQueue(getJsonRequest());
    }


    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private <X> void addToRequestQueue(Request<X> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        VolleyManager.getInstance().getRequestQueue().add(req);
    }

    private <X> void addToRequestQueue(Request<X> req) {
        addToRequestQueue(req, "");
    }


    public void AddParam(String name, String value) {
        params.put(name, value);
    }

    public void AddHeader(String name, String value) {
        headers.put(name, value);
    }

    Request<JSONObject> getJsonRequest() {
        try {
            getUrl();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new JsonObjectRequest(Request.Method.GET, url, jsonResponseListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
    }


    void getUrl() throws UnsupportedEncodingException {
        String combinedParams = "";
        if (!params.isEmpty()) {
            combinedParams += "?";
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String paramString = entry.getKey() + "="
                        + URLEncoder.encode(entry.getValue(), "UTF-8");
                if (combinedParams.length() > 1) {
                    combinedParams += "&" + paramString;
                } else {
                    combinedParams += paramString;
                }
            }
        }
        url = url + combinedParams;
    }
}
