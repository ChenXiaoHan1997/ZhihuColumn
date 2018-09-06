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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import practice.cxh.zhihuzhuanlan.R;
import practice.cxh.zhihuzhuanlan.bean.Article;
import practice.cxh.zhihuzhuanlan.entity.ArticleContentEntity;
import practice.cxh.zhihuzhuanlan.entity.ArticleEntity;
import practice.cxh.zhihuzhuanlan.util.HtmlUtil;
import practice.cxh.zhihuzhuanlan.util.StringUtil;
import practice.cxh.zhihuzhuanlan.util.TimeUtil;

public class ArticleContentActivity extends AppCompatActivity {

    private static final String ARTICLE_ENTITY = "article_entity";
    private static final String JS_INTERFACE = "js_interface";

    private ArticleEntity mArticleEntity;

    private boolean mIsWifi;

    private ArticleContentPagePresenter mPresenter;

    private ImageView ivTitleImage;
    private TextView tvTitle;
    private CircleImageView ivAvatar;
    private TextView tvAuthorAndTime;
    private WebView wvContent;

    public static void launch(Activity activity, ArticleEntity articleEntity) {
        Intent intent = new Intent(activity, ArticleContentActivity.class);
        intent.putExtra(ARTICLE_ENTITY, articleEntity);
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
        ivTitleImage = findViewById(R.id.iv_title_image);
        tvTitle = findViewById(R.id.tv_title);
        ivAvatar = findViewById(R.id.iv_avatar);
        tvAuthorAndTime = findViewById(R.id.tv_author_time);
        wvContent = findViewById(R.id.wv_content);
    }

    private void initData() {
        mArticleEntity = (ArticleEntity) getIntent().getSerializableExtra(ARTICLE_ENTITY);
        Glide.with(this).load(mArticleEntity.getTitleImage()).into(ivTitleImage);
        tvTitle.setText(mArticleEntity.getTitle());
        mPresenter = new ArticleContentPagePresenter(this);
        mPresenter.loadArticleContent(mArticleEntity.getSlug());
    }

    public void onArticleContentLoaded(ArticleContentEntity articleContentEntity) {
//        wvContent.getSettings().setJavaScriptEnabled(true);
        Glide.with(this).load(articleContentEntity.getAvatar()).into(ivAvatar);
        tvAuthorAndTime.setText(String.format(getString(R.string.author_and_time), articleContentEntity.getAuthor(), TimeUtil.convertPublishTime(mArticleEntity.getPublishedTime())));
        wvContent.setWebViewClient(mWebViewClient);
        wvContent.addJavascriptInterface(this, JS_INTERFACE);
        wvContent.loadData(HtmlUtil.getHtmlData(articleContentEntity.getContent(), mIsWifi), "text/html; charset=UTF-8", null);
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
