package practice.cxh.zhihuzhuanlan.column_page;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

import practice.cxh.zhihuzhuanlan.bean.Article;
import practice.cxh.zhihuzhuanlan.datasource.AsyncDataSource;
import practice.cxh.zhihuzhuanlan.datasource.DataSource;
import practice.cxh.zhihuzhuanlan.datasource.LruAsyncDataSourceBase;
import practice.cxh.zhihuzhuanlan.datasource.LrudataSourceBase;
import practice.cxh.zhihuzhuanlan.db.ArticleEntityDao;
import practice.cxh.zhihuzhuanlan.entity.ArticleEntity;
import practice.cxh.zhihuzhuanlan.util.AsyncUtil;
import practice.cxh.zhihuzhuanlan.util.DbUtil;
import practice.cxh.zhihuzhuanlan.util.HttpUtil;
import practice.cxh.zhihuzhuanlan.util.JsonUtil;

public class ArticleEntityDataSource extends LruAsyncDataSourceBase<ArticleEntity> implements AsyncDataSource<ArticleEntity> {

    private static final int MAX_COUNT = Integer.MAX_VALUE - 10086;
    private static final int CACHE_SIZE = 50;
    private static final int ARTICLE_LOAD_ONCE = 10;

    private String columnSlug;
    private int postsCount;

    private Handler mUiHandler;

    public ArticleEntityDataSource(String columnSlug, int postsCount) {
        super(CACHE_SIZE);
        this.columnSlug = columnSlug;
        this.postsCount = postsCount;
        this.mUiHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    protected void getDataNoCached(final int start, final int count, final AsyncDataListener<ArticleEntity> listener) {
        AsyncUtil.executeAsync(new Runnable() {
            @Override
            public void run() {
                final List<ArticleEntity> articleEntityList = DbUtil.getArticleEntityDao()
                        .queryBuilder()
                        .where(ArticleEntityDao.Properties.ColumnSlug.eq(columnSlug))
                        .limit(count)
                        .offset(start)
                        .list();
                if (articleEntityList != null && articleEntityList.size() > 0) {
                    mUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onDataLoaded(articleEntityList);
                        }
                    });
                }
            }
        });

        // 从网络中获取数据
        HttpUtil.get(HttpUtil.API_BASE + HttpUtil.COLUMN + "/" + columnSlug + "/" + HttpUtil.POSTS + "?offset=" + start + "&limit=" + count,
                new HttpUtil.HttpListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        List<Article> articlesList = JsonUtil.decodeArticleList(response);
                        List<ArticleEntity> articleEntityList = new ArrayList<ArticleEntity>();
                        for (Article article : articlesList) {
                            // articleEntity是新获得的
                            ArticleEntity articleEntity = ArticleEntity.convertFromArticle(article, columnSlug);
                            // 若数据库中已有此文章，设置articleEntity的下载状态
                            List<ArticleEntity> tmp = DbUtil.getArticleEntityDao()
                                    .queryBuilder()
                                    .where(ArticleEntityDao.Properties.Slug.eq(articleEntity.getSlug()))
                                    .list();
                            if (tmp.size() > 0) {
                                articleEntity.setDownloadState(tmp.get(0).getDownloadState());
                            }
                            articleEntityList.add(articleEntity);
                        }
                        if (articleEntityList.size() == 0) {
                            return;
                        }
                        if (count == 1) {
                            listener.onDataLoaded(articleEntityList.get(0));
                        } else {
                            listener.onDataLoaded(articleEntityList);
                        }
                        saveArticleList(articleEntityList);
                    }

                    @Override
                    public void onFail(String detail) {
                        // TODO 出错提示
                    }
                });
    }

    private void saveArticleList(final List<ArticleEntity> articleEntityList) {
        AsyncUtil.executeAsync(new Runnable() {
            @Override
            public void run() {
                for (ArticleEntity articleEntity : articleEntityList) {
                    DbUtil.getArticleEntityDao().insertOrReplace(articleEntity);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return postsCount;
    }
}
