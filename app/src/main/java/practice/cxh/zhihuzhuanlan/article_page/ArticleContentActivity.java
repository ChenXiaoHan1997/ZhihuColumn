package practice.cxh.zhihuzhuanlan.article_page;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.util.Scanner;

import de.hdodenhof.circleimageview.CircleImageView;
import practice.cxh.zhihuzhuanlan.R;
import practice.cxh.zhihuzhuanlan.entity.ArticleEntity;
import practice.cxh.zhihuzhuanlan.util.FileUtil;
import practice.cxh.zhihuzhuanlan.util.HtmlUtil;
import practice.cxh.zhihuzhuanlan.util.StringUtil;
import practice.cxh.zhihuzhuanlan.util.TimeUtil;

public class ArticleContentActivity extends AppCompatActivity {

    private static final String ARTICLE_ENTITY = "article_entity";
    private static final String JS_INTERFACE = "imageListener";
    private static final String FAIL_RETRY_HTML = "file:///android_asset/fail_retry.html";

    private ArticleEntity mArticleEntity;

    private boolean mIsWifi;

    private ArticleContentPagePresenter mPresenter;

    private AppBarLayout mAppBar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView ivTitleImage;
    private TextView tvTitle;
    private CircleImageView ivAvatar;
    private TextView tvAuthorAndTime;
    private WebView wvContent;
    private TextView tvFailRetry;
    private Toolbar toolbar;

    public static void launch(Activity activity, ArticleEntity articleEntity) {
        Intent intent = new Intent(activity, ArticleContentActivity.class);
        intent.putExtra(ARTICLE_ENTITY, articleEntity);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initToolbar();
        initWebView();
        registerListenerAndReceiver();
        checkWifi();
        initData();
    }

    private void checkWifi() {
        mIsWifi = true;
    }

    private void initView() {
        setContentView(R.layout.activity_article_content);
        // 去掉DecorView背景
        getWindow().setBackgroundDrawable(null);
        mAppBar = findViewById(R.id.app_bar);
        mCollapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        ivTitleImage = findViewById(R.id.iv_title_image);
        tvTitle = findViewById(R.id.tv_title);
        ivAvatar = findViewById(R.id.iv_avatar);
        tvAuthorAndTime = findViewById(R.id.tv_author_time);
        wvContent = findViewById(R.id.wv_content);
        tvFailRetry = findViewById(R.id.tv_fail_retry);
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.tb_article_content);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back_ios_white);
        }
    }

    private void initWebView() {
        wvContent.addJavascriptInterface(this, JS_INTERFACE);
        WebSettings webSettings = wvContent.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    private void registerListenerAndReceiver() {
        mAppBar.addOnOffsetChangedListener(mOnOffsetChangedListener);
    }

    private void initData() {
        mArticleEntity = (ArticleEntity) getIntent().getSerializableExtra(ARTICLE_ENTITY);
        if (TextUtils.isEmpty(mArticleEntity.getTitleImage())) {
            ivTitleImage.setVisibility(View.GONE);
            mCollapsingToolbarLayout.setTitleEnabled(false);
            getSupportActionBar().setTitle(mArticleEntity.getTitle());
        } else {
            Glide.with(this).load(mArticleEntity.getTitleImage()).into(ivTitleImage);
        }
        tvTitle.setText(mArticleEntity.getTitle());
        mPresenter = new ArticleContentPagePresenter(this);
        mPresenter.loadArticleContent(mArticleEntity.getSlug());
    }

    public void onArticleContentLoaded(ArticleEntity articleEntity) {
        Glide.with(this).load(articleEntity.getTitleImage()).into(ivTitleImage);
        Glide.with(this)
                .load(articleEntity.getAvatar())
                .apply(new RequestOptions().placeholder(R.drawable.liukanshan))
                .into(ivAvatar);
        tvAuthorAndTime.setText(StringUtil.getAuthorAndTime(articleEntity.getAuthor(), TimeUtil.convertPublishTime(articleEntity.getPublishedTime())));
        tvFailRetry.setVisibility(View.GONE);
        tvFailRetry.setOnClickListener(null);
        wvContent.setVisibility(View.VISIBLE);
//        wvContent.loadData(articleEntity.getContent(),"text/html; charset=UTF-8", null);
        wvContent.loadDataWithBaseURL(FileUtil.getHtmlFileBase(), articleEntity.getContent(), "text/html; charset=UTF-8", null, null);
    }

    public void onArticleContentLoadFail() {
        wvContent.setVisibility(View.GONE);
        tvFailRetry.setVisibility(View.VISIBLE);
        tvFailRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.loadArticleContent(mArticleEntity.getSlug());
            }
        });
    }

    private AppBarLayout.OnOffsetChangedListener mOnOffsetChangedListener = new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            if (verticalOffset <= -(ivTitleImage.getHeight() - toolbar.getHeight())) {
                mCollapsingToolbarLayout.setTitle(mArticleEntity.getTitle());
            } else {
                mCollapsingToolbarLayout.setTitle("");
            }
        }
    };

    @JavascriptInterface
    public void showBigImage() {

    }
}
