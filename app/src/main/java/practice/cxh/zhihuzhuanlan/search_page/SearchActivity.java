package practice.cxh.zhihuzhuanlan.search_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import practice.cxh.zhihuzhuanlan.R;
import practice.cxh.zhihuzhuanlan.bean.Column;
import practice.cxh.zhihuzhuanlan.entity.ColumnEntity;
import practice.cxh.zhihuzhuanlan.main_page.ColumnEntityAdapter;

public class SearchActivity extends AppCompatActivity implements SearchV {

    private SearchPagePresenter mPresenter;

    private TextView tvGo;
    private SearchView searchView;
    private RecyclerView rvColumns;
    private Toolbar toolbar;

    private ColumnEntityAdapter mAdapter;
    private List<ColumnEntity> mColumnEntityList = new ArrayList<>();

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, SearchActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initToolbar();
        registerListener();
        initData();
    }

    private void initView() {
        setContentView(R.layout.activity_search);
        tvGo = findViewById(R.id.tv_go);
        rvColumns = findViewById(R.id.rv_columns);
        rvColumns.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ColumnEntityAdapter(this, mColumnEntityList);
        rvColumns.setAdapter(mAdapter);
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.tb_search);
        searchView = findViewById(R.id.action_search);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back_ios_white);
        }
    }
    private void registerListener() {
        tvGo.setOnClickListener(mOnClickListener);
    }

    private void initData() {
        mPresenter = new SearchPagePresenter(this, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_page, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setIconified(false);
        searchView.onActionViewExpanded();
        searchView.setQueryHint(getString(R.string.input_slug));
        searchView.setOnQueryTextListener(mQueryTextListener);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onColumnFound(ColumnEntity columnEntity) {
        mColumnEntityList.clear();
        rvColumns.setVisibility(View.VISIBLE);
        tvGo.setVisibility(View.GONE);
        mColumnEntityList.add(columnEntity);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onColumnNotFound() {
        mColumnEntityList.clear();
        rvColumns.setVisibility(View.VISIBLE);
        tvGo.setVisibility(View.VISIBLE);
        tvGo.setText(R.string.column_not_found);
        mAdapter.notifyDataSetChanged();
    }

    private SearchView.OnQueryTextListener mQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            mPresenter.searchColumn(query);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            rvColumns.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(newText)) {
                tvGo.setVisibility(View.VISIBLE);
                tvGo.setText(String.format(getString(R.string.go_search), newText));
            } else {
                tvGo.setVisibility(View.GONE);
            }
            return true;
        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String columnSlug = searchView.getQuery().toString();
            mPresenter.searchColumn(columnSlug);
        }
    };
}
