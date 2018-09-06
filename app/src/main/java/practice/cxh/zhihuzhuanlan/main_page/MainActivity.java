package practice.cxh.zhihuzhuanlan.main_page;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import practice.cxh.zhihuzhuanlan.R;
import practice.cxh.zhihuzhuanlan.entity.ColumnEntity;

public class MainActivity extends AppCompatActivity {

    private MainpagePresenter mPresenter;

    private RecyclerView rvColumns;
    private Toolbar tbMain;

    private ColumnEntityAdapter mAdapter;
    private List<ColumnEntity> mColumnEntityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        tbMain = (Toolbar) findViewById(R.id.tb_main);
        setSupportActionBar(tbMain);
        rvColumns = (RecyclerView) findViewById(R.id.rv_columns);
        rvColumns.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ColumnEntityAdapter(this, mColumnEntityList);
        rvColumns.setAdapter(mAdapter);
    }

    private void initData() {
        mPresenter = new MainpagePresenter(this);
        mPresenter.loadColums();
    }

    public void onColumnLoaded(ColumnEntity columnEntity) {
        Log.d("cxh", columnEntity.getName());
        mColumnEntityList.add(columnEntity);
        mAdapter.notifyDataSetChanged();
    }

}
