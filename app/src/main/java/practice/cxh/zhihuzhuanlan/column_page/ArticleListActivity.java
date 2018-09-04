package practice.cxh.zhihuzhuanlan.column_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import practice.cxh.zhihuzhuanlan.R;
import practice.cxh.zhihuzhuanlan.entity.ArticleEntity;

public class ArticleListActivity extends AppCompatActivity {

    private static String COLUMN_SLUG = "column_slug";

    private String mColumnSlug;

    private ArticleListPagePresenter mPresenter;

    private ArticleListFragment mArticleListFragment;

    private RecyclerView rvArticles;

    private ArticleEntityAdapter mAdapter;
    private List<ArticleEntity> mArticleEntityList = new ArrayList<>();

    public static void launch(Activity activity, String slug) {
        Intent intent = new Intent(activity, ArticleListActivity.class);
        intent.putExtra(COLUMN_SLUG, slug);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_article_list_wrap);
        mArticleListFragment = new ArticleListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, mArticleListFragment).commit();
    }

    //    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        initView();
//        initData();
//    }
//
//    private void initView() {
//        setContentView(R.layout.activity_article_list_wrap);
//        rvArticles = (RecyclerView) findViewById(R.id.rv_articles);
//        rvArticles.setLayoutManager(new LinearLayoutManager(this));
//        mAdapter = new ArticleEntityAdapter(this, mArticleEntityList);
//        rvArticles.setAdapter(mAdapter);
//    }
//
//    private void initData() {
//        Intent intent = getIntent();
//        mColumnSlug = intent.getStringExtra(COLUMN_SLUG);
//        mPresenter = new ArticleListPagePresenter(this);
//        mPresenter.loadArticleList(mColumnSlug);
//    }
//
//    @Override
//    public void onArticleListLoaded(List<ArticleEntity> articleEntityList) {
//        mArticleEntityList.addAll(articleEntityList);
//        mAdapter.notifyDataSetChanged();
//    }
}
