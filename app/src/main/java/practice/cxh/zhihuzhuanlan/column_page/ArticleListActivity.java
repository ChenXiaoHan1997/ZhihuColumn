package practice.cxh.zhihuzhuanlan.column_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import practice.cxh.zhihuzhuanlan.EndlessRecyclerOnScrollListener;
import practice.cxh.zhihuzhuanlan.R;
import practice.cxh.zhihuzhuanlan.entity.ArticleEntity;
import practice.cxh.zhihuzhuanlan.entity.ColumnEntity;

public class ArticleListActivity extends AppCompatActivity implements ArticleListV {

    public static String COLUMN_ENTITY = "column_entity";

    private ColumnEntity mColumnEntity;

    private ArticleListPagePresenter mPresenter;

    private View mLayoutRoot;
    private AppBarLayout mAppBar;
    private LinearLayout mHeader;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private TextView tvName;
    private CircleImageView ivAvatar;
    private TextView tvDescription;
    private RecyclerView rvArticles;
    private Toolbar toolbar;

    private ArticleEntityAdapter mAdapter;
    private List<ArticleEntity> mArticleEntityList = new ArrayList<>();

    public static void lauch(Activity activity, ColumnEntity columnEntity) {
        Intent intent = new Intent(activity, ArticleListActivity.class);
        intent.putExtra(COLUMN_ENTITY, columnEntity);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        setContentView(R.layout.activity_article_list_wrap);
        mAppBar = findViewById(R.id.app_bar);
        mHeader = findViewById(R.id.ll_header);
        mCollapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        mAppBar.addOnOffsetChangedListener(mOnOffsetChangedListener);
        tvName = findViewById(R.id.tv_name);
        ivAvatar = findViewById(R.id.iv_avatar);
        tvDescription = findViewById(R.id.tv_description);
        rvArticles = findViewById(R.id.rv_articles);
        rvArticles.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ArticleEntityAdapter(this, mArticleEntityList);
        rvArticles.setAdapter(mAdapter);
        toolbar = findViewById(R.id.tb_article_list);
    }

    private void initData() {
        mColumnEntity = (ColumnEntity) getIntent().getSerializableExtra(COLUMN_ENTITY);
        tvName.setText(mColumnEntity.getName());
        Glide.with(this).load(mColumnEntity.getPicUrl()).into(ivAvatar);
        tvDescription.setText(mColumnEntity.getDescription());
        mPresenter = new ArticleListPagePresenter(this);
        mPresenter.loadArticleList(mColumnEntity.getSlug(), 0);
        rvArticles.addOnScrollListener(mEndlessRecyclerOnScrollListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initToolbar();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.tb_article_list);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.article_list_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.download:
                Intent intent = new Intent(this, ArticleDownloadActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onArticleListLoaded(List<ArticleEntity> articleEntityList) {
        mArticleEntityList.addAll(articleEntityList);
        mAdapter.setLoadState(ArticleEntityAdapter.LOADING_COMPLETE);
        mAdapter.notifyDataSetChanged();
    }

    private AppBarLayout.OnOffsetChangedListener mOnOffsetChangedListener = new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            if (verticalOffset <= -mHeader.getHeight() / 2) {
                mCollapsingToolbarLayout.setTitle(mColumnEntity.getName());
            } else {
                mCollapsingToolbarLayout.setTitle("");
            }
        }
    };

    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadMore() {
            mAdapter.setLoadState(ArticleEntityAdapter.LOADING);
            if (mArticleEntityList.size() < mColumnEntity.getPostsCount()) {
                mPresenter.loadArticleList(mColumnEntity.getSlug(), mArticleEntityList.size());
            } else {
                mAdapter.setLoadState(ArticleEntityAdapter.LOADING_END);
            }
        }
    };
}
