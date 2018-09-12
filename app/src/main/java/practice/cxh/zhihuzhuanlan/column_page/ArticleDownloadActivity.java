package practice.cxh.zhihuzhuanlan.column_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;
import java.util.List;

import practice.cxh.zhihuzhuanlan.R;
import practice.cxh.zhihuzhuanlan.entity.ArticleEntity;
import practice.cxh.zhihuzhuanlan.service.DownloadArticleContentService;

public class ArticleDownloadActivity extends AppCompatActivity {

    // TODO RecyclerView 上拉加载更多

    private static final String ARTICLE_ENTITY_LIST = "article_entity_list";

    private Button btnDownloadAll;

    private List<ArticleEntity> mArticleEntityList;

    public static void launch(Activity activity, List<ArticleEntity> articleEntityList) {
        Intent intent = new Intent(activity, ArticleDownloadActivity.class);
        intent.putExtra(ARTICLE_ENTITY_LIST, (Serializable) articleEntityList);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        registerListenerAndReceiver();
        initData();
    }

    private void initView() {
        setContentView(R.layout.activity_article_download);
        btnDownloadAll = findViewById(R.id.btn_download_all);
    }

    private void registerListenerAndReceiver() {
        btnDownloadAll.setOnClickListener(mBtnClickListener);
    }

    private void initData() {
        mArticleEntityList = (List<ArticleEntity>) getIntent().getSerializableExtra(ARTICLE_ENTITY_LIST);
        btnDownloadAll.setText(String.format(getString(R.string.download_all), mArticleEntityList.size()));
    }

    private View.OnClickListener mBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_download_all:
                    for (ArticleEntity articleEntity : mArticleEntityList) {
                        DownloadArticleContentService.downloadArticleContent(ArticleDownloadActivity.this, articleEntity.getSlug());
                    }
                    break;
            }
        }
    };
}
