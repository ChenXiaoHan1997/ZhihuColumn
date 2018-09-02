package practice.cxh.zhihuzhuanlan.util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class HttpUtil {

    public static final String API_BASE = "https://zhuanlan.zhihu.com/api/";
    public static final String COLUMN = "columns";
    public static final String POSTS = "posts";

    private Context mContext;
    private RequestQueue mRequestQueue;

    public HttpUtil(Context context) {
        this.mContext = context;
        this.mRequestQueue = Volley.newRequestQueue(mContext);
    }

    public void get(String url, final HttpListener httpListener) {
        StringRequest request = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        httpListener.onSuccess(s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        httpListener.onFail();
                    }
                });
        mRequestQueue.add(request);
    }

    public interface HttpListener {
        void onSuccess(String response);
        void onFail();
    }
}
