package practice.cxh.zhihuzhuanlan.column_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import practice.cxh.zhihuzhuanlan.R;
import practice.cxh.zhihuzhuanlan.entity.ColumnEntity;

public class ArticleListActivity extends AppCompatActivity {

    public static String COLUMN_ENTITY = "column_entity";

    private ArticleListFragment mArticleListFragment;
    private ArticleDownloadFragment mArticleDownloadFragment;
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
        mArticleListFragment = new ArticleListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, mArticleListFragment).commit();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initToolbar();
    }

    private void initToolbar() {
        tbArticleListPage = findViewById(R.id.tb_article_list);
        setSupportActionBar(tbArticleListPage);
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
                mArticleDownloadFragment = new ArticleDownloadFragment();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out, R.anim.slide_right_in, R.anim.slide_right_out)
                        .add(R.id.container, mArticleDownloadFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            default:
                break;
        }
        return true;
    }
}
