package practice.cxh.zhihuzhuanlan.column_page;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import practice.cxh.zhihuzhuanlan.bean.Article;
import practice.cxh.zhihuzhuanlan.datasource.AsyncDataSource;
import practice.cxh.zhihuzhuanlan.datasource.PreloadDataSourceBase0;
import practice.cxh.zhihuzhuanlan.datasource.PreloadListDataSource;
import practice.cxh.zhihuzhuanlan.db.ArticleEntityDao;
import practice.cxh.zhihuzhuanlan.entity.ArticleEntity;
import practice.cxh.zhihuzhuanlan.util.AsyncUtil;
import practice.cxh.zhihuzhuanlan.util.DbUtil;
import practice.cxh.zhihuzhuanlan.util.HttpUtil;
import practice.cxh.zhihuzhuanlan.util.JsonUtil;

public class ArticleEntityDataSource extends PreloadListDataSource<ArticleEntity> {

    private String columnSlug;
    private Context mContext;
    private HttpUtil mHttpUtil;

    public ArticleEntityDataSource(Context context, String columnSlug, int postsCount, RecyclerView.Adapter adapter) {
        super(postsCount, adapter);
        this.columnSlug = columnSlug;
        this.mContext = context;
        this.mHttpUtil = new HttpUtil(mContext);
        init();
    }

    @Override
    protected void onPreloadData(final int start, final int count) {
        Log.d("tag1", HttpUtil.API_BASE + HttpUtil.COLUMN + "/" + columnSlug
                + "/" + HttpUtil.POSTS + "?offset=" + start + "&limit=" + count);
        mHttpUtil.get(HttpUtil.API_BASE + HttpUtil.COLUMN + "/" + columnSlug
                        + "/" + HttpUtil.POSTS + "?offset=" + start + "&limit=" + count,
                new HttpUtil.HttpListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        List<Article> articlesList = JsonUtil.decodeArticleList(response);
                        int i = start;
                        for (Article article : articlesList) {
                            // articleEntity是新获得的
                            ArticleEntity articleEntity = ArticleEntity.convertFromArticle(article, columnSlug);
                            // 若数据库中已有此文章，设置articleEntity的下载状态
                            ArticleEntity tmp = DbUtil.getArticleEntityDao()
                                    .queryBuilder()
                                    .where(ArticleEntityDao.Properties.Slug.eq(articleEntity.getSlug()))
                                    .unique();
                            if (tmp != null) {
                                articleEntity.setDownloadState(tmp.getDownloadState());
                            }
                            mDataList.add(articleEntity);
                            mAdapter.notifyItemChanged(i++);
                            saveArticle(articleEntity);
                        }
                    }

                    @Override
                    public void onFail(String detail) {
                        loadDataFromDB(start, count);
                    }
                });
    }

    private void loadDataFromDB(final int start, final int count) {
        AsyncUtil.executeAsync(new Runnable() {
            @Override
            public void run() {
                final List<ArticleEntity> articleEntityList = DbUtil.getArticleEntityDao()
                        .queryBuilder()
                        .where(ArticleEntityDao.Properties.ColumnSlug.eq(columnSlug))
                        .orderDesc(ArticleEntityDao.Properties.Slug)
                        .limit(count)
                        .offset(start)
                        .list();
                if (articleEntityList.size() > 0) {
                    mDataList.addAll(articleEntityList);
                    mAdapter.notifyItemRangeChanged(start, count);
                }
                Log.d("cxh", articleEntityList.toString());
            }
        });
    }

    private void saveArticle(ArticleEntity articleEntity) {
        DbUtil.getArticleEntityDao().insertOrReplace(articleEntity);
    }
}
