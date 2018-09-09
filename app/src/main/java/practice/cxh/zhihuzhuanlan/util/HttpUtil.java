package practice.cxh.zhihuzhuanlan.util;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import practice.cxh.zhihuzhuanlan.article_page.ByteArrayRequest;

public class HttpUtil {

    public static final String API_BASE = "https://zhuanlan.zhihu.com/api/";
    public static final String COLUMN = "columns";
    public static final String POSTS = "posts";

    private Context mContext;
    private RequestQueue mRequestQueue;

    private static Context sContext;
    private static RequestQueue sRequestQueue;

    public static void init(Application application) {
        sContext = application;
        sRequestQueue = Volley.newRequestQueue(sContext);
    }

    public HttpUtil(Context context) {
        this.mContext = context;
        this.mRequestQueue = Volley.newRequestQueue(mContext);
    }

    public static void get(String url, final HttpListener<String> httpListener) {
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
        sRequestQueue.add(request);
    }

    public static void getBytes(String url, final HttpListener<byte[]> httpListener) {
        ByteArrayRequest request = new ByteArrayRequest(Request.Method.GET,
                url,
                new Response.Listener<byte[]>() {
                    @Override
                    public void onResponse(byte[] bytes) {
                        httpListener.onSuccess(bytes);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        httpListener.onFail();
                    }
                });
        sRequestQueue.add(request);
    }

    public interface HttpListener<T> {
        void onSuccess(T response);
        void onFail();
    }
}
