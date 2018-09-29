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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import de.hdodenhof.circleimageview.CircleImageView;
import practice.cxh.zhihuzhuanlan.Constants;
import practice.cxh.zhihuzhuanlan.entity.SubscribeEntity;
import practice.cxh.zhihuzhuanlan.event_pool.EventPool;
import practice.cxh.zhihuzhuanlan.event_pool.IEvent;
import practice.cxh.zhihuzhuanlan.event_pool.IEventListener;
import practice.cxh.zhihuzhuanlan.event_pool.event.SubscribeEvent;
import practice.cxh.zhihuzhuanlan.util.EndlessRecyclerOnScrollListener;
import practice.cxh.zhihuzhuanlan.datasource.DataSource;
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
    private Button btnFollow;
    private CircleImageView ivAvatar;
    private TextView tvDescription;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvArticles;
    private Toolbar toolbar;

    private ArticleEntityAdapter mAdapter;
    private DataSource<ArticleEntity> mDataSource;

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
        btnFollow = findViewById(R.id.btn_follow);
        ivAvatar = findViewById(R.id.iv_avatar);
        tvDescription = findViewById(R.id.tv_description);
        swipeRefresh = findViewById(R.id.swipe_refresh);
        rvArticles = findViewById(R.id.rv_articles);
        rvArticles.setLayoutManager(new LinearLayoutManager(this));
        // 取消动画避免列表项闪烁
        ((SimpleItemAnimator) rvArticles.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    private void registerListenerAndReceiver() {
        // 折叠式标题栏在折叠时显示标题
        mAppBar.addOnOffsetChangedListener(mOnOffsetChangedListener);

        swipeRefresh.setOnRefreshListener(mRefreshListener);
        // 注册监听后台下载广播
        IntentFilter intentFilter = new IntentFilter(Constants.BC_DOWNLOAD_FINISH);
        LocalBroadcastManager.getInstance(this).registerReceiver(mDownloadFinishReicever, intentFilter);
        btnFollow.setOnClickListener(mBtnClickListener);
        EventPool.getInstace().addListener(SubscribeEvent.TYPE, mSubscribeListener);
    }

    private void initData() {
        mColumnEntity = (ColumnEntity) getIntent().getSerializableExtra(COLUMN_ENTITY);
        tvName.setText(mColumnEntity.getName());
        setBtnSelected(mColumnEntity.isSubscribed());
        Glide.with(this)
                .load(mColumnEntity.getAvatar())
                .apply(new RequestOptions().placeholder(R.drawable.liukanshan))
                .into(ivAvatar);
        tvDescription.setText(mColumnEntity.getDescription());
//        swipeRefresh.setRefreshing(true);
        mAdapter = new ArticleEntityAdapter(this);
        mDataSource = new ArticleEntityDataSource(this,
                mColumnEntity.getSlug(),
                mColumnEntity.getPostsCount(),
                mAdapter);
        mAdapter.setDataSource(mDataSource);
        rvArticles.setAdapter(mAdapter);
        mPresenter = new ArticleListPagePresenter(this);
    }

    @Override
    public void onStart() {
        super.onStart();
//        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        EventPool.getInstace().removeListener(SubscribeEvent.TYPE, mSubscribeListener);
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
                ArticleDownloadActivity.launch(this, mDataSource.getDataWithRange(0, mDataSource.getItemCount()));
                break;
            default:
                break;
        }
        return true;
    }

    private void refreshArticleList() {
        // TODO 重新加载
        AsyncUtil.executeAsync(new Runnable() {
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


    private void setBtnSelected(boolean isSubscribe) {
        btnFollow.setSelected(isSubscribe);
        btnFollow.setText(isSubscribe ?
                R.string.unsubscribe : R.string.subscribe);
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

    private View.OnClickListener mBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPresenter.setSubscribe(mColumnEntity.getSlug(), !mColumnEntity.isSubscribed());
        }
    };

    private IEventListener mSubscribeListener = new IEventListener() {
        @Override
        public void onEvent(IEvent event) {
            switch (event.getType()) {
                case SubscribeEvent.TYPE:
                    final SubscribeEvent subscribeEvent = (SubscribeEvent) event;
                    if (mColumnEntity.getSlug().equals(subscribeEvent.getColumnSlug())) {
                        mColumnEntity.setSubscribed(subscribeEvent.isSubscribe());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setBtnSelected(mColumnEntity.isSubscribed());
                            }
                        });
                        break;
                    }
            }
        }
    };
}
