package practice.cxh.zhihuzhuanlan.util;

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

//    public static void get(String url, final HttpListener httpListener) {
//        OkHttpClient okHttpClient = new OkHttpClient();
//        final okhttp3.Request request = new okhttp3.Request.Builder()
//                .url(url)
//                .get()
//                .build();
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                httpListener.onFail();
//            }
//
//            @Override
//            public void onResponse(Call call, okhttp3.Response response) throws IOException {
//                Log.d("cxh", response.body().toString());
//                httpListener.onSuccess(response.body().string());
//            }
//        });
//    }

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
