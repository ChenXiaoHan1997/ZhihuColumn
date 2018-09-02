package practice.cxh.zhihuzhuanlan.article_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import practice.cxh.zhihuzhuanlan.R;
import practice.cxh.zhihuzhuanlan.util.HtmlUtil;

public class ArticleContentActivity extends AppCompatActivity {

    private static final String ARTICLE_SLUG = "article_slug";
    private static final String JS_INTERFACE = "js_interface";

    private String mArticleSlug;

    private boolean mIsWifi;

    private ArticleContentPagePresenter mPresenter;

    private WebView wvContent;

    public static void launch(Activity activity, String slug) {
        Intent intent = new Intent(activity, ArticleContentActivity.class);
        intent.putExtra(ARTICLE_SLUG, slug);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        checkWifi();
        initData();
    }

    private void checkWifi() {
        mIsWifi = true;
    }

    private void initView() {
        setContentView(R.layout.activity_article_content);
        wvContent = (WebView) findViewById(R.id.wv_content);
    }

    private void initData() {
        Intent intent = getIntent();
        mArticleSlug = intent.getStringExtra(ARTICLE_SLUG);
        mPresenter = new ArticleContentPagePresenter(this);
        mPresenter.loadArticleContent(mArticleSlug);
    }

    public void onArticleContentLoaded(String content) {
//        wvContent.getSettings().setJavaScriptEnabled(true);
        wvContent.setWebViewClient(mWebViewClient);
        wvContent.addJavascriptInterface(this, JS_INTERFACE);
        wvContent.loadData(HtmlUtil.getHtmlData(content, mIsWifi), "text/html; charset=UTF-8", null);
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            wvContent.loadUrl("javascript:(function(){" +
                    "var objs = document.getElementsByTagName(\"img\"); " +
                    "for(var i=0;i<objs.length;i++)  " +
                    "{"
                    + "    objs[i].onclick=function()  " +
                    "    {  "
                    + "        window." + JS_INTERFACE + ".showBigImage(this.src);  " +
                    "    }  " +
                    "}" +
                    "})()");
        }
    };

    @JavascriptInterface
    private void showBigImage(String url) {
        Log.d("cxh", "show big image: " + url);
    }
}
