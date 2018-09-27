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

import de.hdodenhof.circleimageview.CircleImageView;
import practice.cxh.zhihuzhuanlan.Constants;
import practice.cxh.zhihuzhuanlan.datasource.DataSource;
import practice.cxh.zhihuzhuanlan.EndlessRecyclerOnScrollListener;
import practice.cxh.zhihuzhuanlan.R;
import practice.cxh.zhihuzhuanlan.entity.ArticleEntity;
import practice.cxh.zhihuzhuanlan.entity.ColumnEntity;
import practice.cxh.zhihuzhuanlan.util.AsyncUtil;

public class ArticleListActivity extends AppCompatActivity {
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
    private DataSource<ArticleEntity> mDataSource;

//    private ArticleEntityAdapter mAdapter;
//    private List<ArticleEntity> mArticleEntityList = new ArrayList<>();

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
    }

    private void registerListenerAndReceiver() {
        // 折叠式标题栏在折叠时显示标题
        mAppBar.addOnOffsetChangedListener(mOnOffsetChangedListener);

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
        mAdapter = new ArticleEntityAdapter(this, rvArticles);
        mDataSource = new ArticleEntityDataSource(mColumnEntity.getSlug(),
                mColumnEntity.getPostsCount(),
                mAdapter);
        mAdapter.setDataSource(mDataSource);
        rvArticles.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
//        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mDownloadFinishReicever);
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
                // TODO 启动下载页
//                ArticleDownloadActivity.launch(this, mDataSource);
                break;
            default:
                break;
        }
        return true;
    }

    // TODO 请求成功回调

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
//            mAdapter.setLoadState(ArticleEntityAdapter.LOADING);
//            if (mAdapter.getLastPosition() < mColumnEntity.getPostsCount()) {
//                mPresenter.loadArticleList(mColumnEntity.getSlug(), mArticleEntityList.size());
//            } else {
//                mAdapter.setLoadState(ArticleEntityAdapter.LOADING_END);
//            }
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
            // TODO 接受到广播回调
        }
    };
}
