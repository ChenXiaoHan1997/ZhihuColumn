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
import java.util.List;

import practice.cxh.zhihuzhuanlan.DataCore;
import practice.cxh.zhihuzhuanlan.R;
import practice.cxh.zhihuzhuanlan.entity.ColumnEntity;
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
        initData();
    }

    @Override
    protected void onDestroy() {
        DbUtil.getColumnEntityDao().detachAll();
        DataCore.getInstance().setFirstRun(false);
        super.onDestroy();
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        // 去掉DecorView背景
        getWindow().setBackgroundDrawable(null);
        toolbar = (Toolbar) findViewById(R.id.tb_main);
        setSupportActionBar(toolbar);
        rvColumns = (RecyclerView) findViewById(R.id.rv_columns);
        rvColumns.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ColumnEntityAdapter(this, mColumnEntityList);
        rvColumns.setAdapter(mAdapter);
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
        Log.d("cxh", columnEntity.getName());
        mColumnEntityList.add(columnEntity);
        mAdapter.notifyDataSetChanged();
    }

}
