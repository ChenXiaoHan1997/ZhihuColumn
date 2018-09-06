package practice.cxh.zhihuzhuanlan.column_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.Serializable;
import java.util.List;

import practice.cxh.zhihuzhuanlan.R;
import practice.cxh.zhihuzhuanlan.entity.ArticleEntity;

public class ArticleDownloadActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    public static void launch(Activity activity, List<ArticleEntity> articleEntityList) {
        Intent intent = new Intent(activity, ArticleDownloadActivity.class);
        intent.putExtra("a", (Serializable) articleEntityList);
        activity.startActivity(intent);
    }

    private void initView() {
        setContentView(R.layout.activity_article_download);
    }

    private void initData() {
        List<ArticleEntity> articleEntityList = (List<ArticleEntity>) getIntent().getSerializableExtra("a");
        Log.d("cxh", articleEntityList.toString());
    }
}
