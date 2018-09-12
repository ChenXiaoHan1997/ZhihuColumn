package practice.cxh.zhihuzhuanlan.column_page;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import practice.cxh.zhihuzhuanlan.Constants;
import practice.cxh.zhihuzhuanlan.EndlessRecyclerOnScrollListener;
import practice.cxh.zhihuzhuanlan.R;
import practice.cxh.zhihuzhuanlan.entity.ArticleEntity;
import practice.cxh.zhihuzhuanlan.entity.ColumnEntity;
import practice.cxh.zhihuzhuanlan.service.DownloadArticleContentService;
import practice.cxh.zhihuzhuanlan.util.AsyncUtil;
import practice.cxh.zhihuzhuanlan.util.DbUtil;

public class ArticleListActivity extends AppCompatActivity implements ArticleListV {

    public static String COLUMN_ENTITY = "column_entity";

    private ColumnEntity mColumnEntity;

    private ArticleListPagePresenter mPresenter;

    private AppBarLayout mAppBar;
    private LinearLayout mHeader;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private TextView tvName;
    private CircleImageView ivAvatar;
    private TextView tvDescription;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvArticles;
    private Toolbar toolbar;

    private ArticleEntityAdapter mAdapter;
    private List<ArticleEntity> mArticleEntityList = new ArrayList<>();

    public static void launch(Activity activity, ColumnEntity columnEntity) {
        Intent intent = new Intent(activity, ArticleListActivity.class);
        intent.putExtra(COLUMN_ENTITY, columnEntity);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initToolbar();
        registerListenerAndReceiver();
        initData();
    }

    private void initView() {
        setContentView(R.layout.activity_article_list);
        mAppBar = findViewById(R.id.app_bar);
        mHeader = findViewById(R.id.ll_header);
        mCollapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        tvName = findViewById(R.id.tv_name);
        ivAvatar = findViewById(R.id.iv_avatar);
        tvDescription = findViewById(R.id.tv_description);
        swipeRefresh = findViewById(R.id.swipe_refresh);
        rvArticles = findViewById(R.id.rv_articles);
        rvArticles.setLayoutManager(new LinearLayoutManager(this));
        // 取消动画避免列表项闪烁
        ((SimpleItemAnimator)rvArticles.getItemAnimator()).setSupportsChangeAnimations(false);
        mAdapter = new ArticleEntityAdapter(this, mArticleEntityList);
        rvArticles.setAdapter(mAdapter);
    }

    private void registerListenerAndReceiver() {
        // 折叠式标题栏在折叠时显示标题
        mAppBar.addOnOffsetChangedListener(mOnOffsetChangedListener);
        // 文章列表在滑动到最后一项时开始加载
        rvArticles.addOnScrollListener(mEndlessRecyclerOnScrollListener);
        swipeRefresh.setOnRefreshListener(mRefreshListener);
        // 注册监听后台下载广播
        IntentFilter intentFilter = new IntentFilter(Constants.BC_DOWNLOAD_FINISH);
        LocalBroadcastManager.getInstance(this).registerReceiver(mDownloadFinishReicever, intentFilter);
    }

    private void initData() {
        mColumnEntity = (ColumnEntity) getIntent().getSerializableExtra(COLUMN_ENTITY);
        tvName.setText(mColumnEntity.getName());
        Glide.with(this)
                .load(mColumnEntity.getAvatar())
                .apply(new RequestOptions().placeholder(R.drawable.liukanshan))
                .into(ivAvatar);
        tvDescription.setText(mColumnEntity.getDescription());
        mPresenter = new ArticleListPagePresenter(this);
        mPresenter.loadArticleList(mColumnEntity.getSlug(), 0);
    }

    @Override
    public void onStart() {
        super.onStart();
//        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mDownloadFinishReicever);
        DbUtil.getArticleEntityDao().detachAll();
        super.onDestroy();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.tb_article_list);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back_ios_white);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.article_list_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.download:
                ArticleDownloadActivity.launch(this, mArticleEntityList);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onArticleListLoaded(List<ArticleEntity> articleEntityList, boolean clearOld) {
        if (clearOld) {
            mArticleEntityList.clear();
        }
        int oldSize = mArticleEntityList.size();
        mArticleEntityList.addAll(articleEntityList);
        mAdapter.setLoadState(ArticleEntityAdapter.LOADING_COMPLETE);
        mAdapter.notifyItemRangeChanged(oldSize, mArticleEntityList.size() - oldSize);
    }

    private void refreshArticleList() {
        // TODO 重新加载
        AsyncUtil.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
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

        @Override
        protected void onStartScrolling() {
//            Glide.with(ArticleListActivity.this).pauseRequests();
        }

        @Override
        protected void onStopScrolling() {
//            Glide.with(ArticleListActivity.this).resumeRequests();
        }
    };

    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshArticleList();
        }
    };

    private BroadcastReceiver mDownloadFinishReicever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String slug = intent.getStringExtra(Constants.BC_SLUG);
            for (int i = mArticleEntityList.size() - 1; i >= 0; i--) {
                if (mArticleEntityList.get(i).getSlug().equals(slug)) {
                    mAdapter.notifyItemChanged(i);
                    break;
                }
            }
//            mAdapter.notifyDataSetChanged();
        }
    };
}
