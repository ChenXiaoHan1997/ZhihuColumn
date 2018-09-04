package practice.cxh.zhihuzhuanlan.column_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import practice.cxh.zhihuzhuanlan.R;
import practice.cxh.zhihuzhuanlan.entity.ColumnEntity;

public class ArticleListActivity extends AppCompatActivity {

    public static String COLUMN_ENTITY = "column_entity";

    private ArticleListFragment mArticleListFragment;
    private Toolbar tbArticleListPage;

    public static void lauch(Activity activity, ColumnEntity columnEntity) {
        Intent intent = new Intent(activity, ArticleListActivity.class);
        intent.putExtra(COLUMN_ENTITY, columnEntity);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_article_list_wrap);
        tbArticleListPage = findViewById(R.id.tb_article_list);
        setSupportActionBar(tbArticleListPage);
//        ColumnEntity columnEntity = (ColumnEntity) getIntent().getSerializableExtra(COLUMN_ENTITY);
        mArticleListFragment = new ArticleListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, mArticleListFragment).commit();
    }

    public void setToolbarTitle(String title) {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }
}
