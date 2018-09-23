package practice.cxh.zhihuzhuanlan.main_page;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import practice.cxh.zhihuzhuanlan.DataCore;
import practice.cxh.zhihuzhuanlan.R;
import practice.cxh.zhihuzhuanlan.entity.ColumnEntity;
import practice.cxh.zhihuzhuanlan.event_pool.EventPool;
import practice.cxh.zhihuzhuanlan.event_pool.IEvent;
import practice.cxh.zhihuzhuanlan.event_pool.IEventListener;
import practice.cxh.zhihuzhuanlan.event_pool.event.SubscribeEvent;
import practice.cxh.zhihuzhuanlan.search_page.SearchActivity;
import practice.cxh.zhihuzhuanlan.util.DbUtil;

public class MainActivity extends AppCompatActivity {

    private MainpagePresenter mPresenter;

    private RecyclerView rvColumns;
    private Toolbar toolbar;

    private ColumnEntityAdapter mAdapter;
    private List<ColumnEntity> mColumnEntityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        registerListenerAndReceiver();
        initData();
    }

    @Override
    protected void onDestroy() {
        DbUtil.getColumnEntityDao().detachAll();
        DataCore.getInstance().setFirstRun(false);
        EventPool.getInstace().removeListener(mSubscribeListener);
        super.onDestroy();
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        // 去掉DecorView背景
        getWindow().setBackgroundDrawable(null);
        toolbar = findViewById(R.id.tb_main);
        setSupportActionBar(toolbar);
        rvColumns = findViewById(R.id.rv_columns);
        rvColumns.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ColumnEntityAdapter(this, mColumnEntityList);
        rvColumns.setAdapter(mAdapter);
    }

    private void registerListenerAndReceiver() {
        EventPool.getInstace().addListener(SubscribeEvent.TYPE, mSubscribeListener);
    }

    private void initData() {
        mPresenter = new MainpagePresenter(this);
        mPresenter.loadSubscribedColumns();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                SearchActivity.launch(this);
                break;
        }
        return true;
    }

    public void onColumnLoaded(ColumnEntity columnEntity) {
        mColumnEntityList.add(columnEntity);
        mAdapter.notifyDataSetChanged();
    }

    private IEventListener mSubscribeListener = new IEventListener() {
        @Override
        public void onEvent(IEvent event) {
            switch (event.getType()) {
                case SubscribeEvent.TYPE:
                    final SubscribeEvent subscribeEvent = (SubscribeEvent) event;
                    if (subscribeEvent.isSubscribe()) {
                        // TODO 使用Presenter请求新订阅的专栏的信息
                        mPresenter.findSubscribedColumns();
                        mPresenter.loadColumnBySlug(subscribeEvent.getColumnSlug());
                    } else {
                        Iterator<ColumnEntity> iterator = mColumnEntityList.iterator();
                        while (iterator.hasNext()) {
                            ColumnEntity columnEntity = iterator.next();
                            if (columnEntity.getSlug().equals(subscribeEvent.getColumnSlug())) {
                                iterator.remove();
                            }
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                    break;
            }
        }
    };
}
