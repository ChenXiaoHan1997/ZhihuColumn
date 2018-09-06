package practice.cxh.zhihuzhuanlan.column_page;

import android.os.Handler;
import android.util.Log;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import practice.cxh.zhihuzhuanlan.ZhihuZhuanlanApplication;
import practice.cxh.zhihuzhuanlan.bean.Article;
import practice.cxh.zhihuzhuanlan.db.ArticleEntityDao;
import practice.cxh.zhihuzhuanlan.entity.ArticleEntity;
import practice.cxh.zhihuzhuanlan.util.AsyncUtil;
import practice.cxh.zhihuzhuanlan.util.DbUtil;
import practice.cxh.zhihuzhuanlan.util.HttpUtil;
import practice.cxh.zhihuzhuanlan.util.JsonUtil;

public class ArticleListPagePresenter {

    private static final int ARTICLE_LIMIT = 10;

    private ArticleListV mArticleListV;
    private ArticleEntityDao mArticleListVcleEntityDao;
    private Handler mUiHandler;

    public ArticleListPagePresenter(ArticleListV articleListV) {
        this.mArticleListV = articleListV;
        mUiHandler = new Handler();
    }

    public void loadArticleList(final String columnSlug, final int offset) {
        // TODO 先从数据库加载
        HttpUtil.get(HttpUtil.API_BASE + HttpUtil.COLUMN + "/" + columnSlug + "/" + HttpUtil.POSTS + "?offset=" + offset,
                new HttpUtil.HttpListener() {
            @Override
            public void onSuccess(String response) {
                List<Article> articlesList = JsonUtil.decodeArticleList(response);
                List<ArticleEntity> articleEntityList = new ArrayList<ArticleEntity>();
                for (Article article : articlesList) {
                    ArticleEntity articleEntity = ArticleEntity.convertFromArticle(article);
                    articleEntity.setColumnSlug(columnSlug);
                    List<ArticleEntity> tmp = DbUtil.getArticleEntityDao().queryBuilder()
                            .where(ArticleEntityDao.Properties.Slug.eq(articleEntity.getSlug()))
                            .orderDesc(ArticleEntityDao.Properties.PublishedTime)
                            .list();
                    if (tmp.size() > 0) {
                        articleEntity.setDownloadState(tmp.get(0).getDownloadState());
                    }
                    articleEntityList.add(articleEntity);
                }
                mArticleListV.onArticleListLoaded(articleEntityList, false);
                saveArticleList(articleEntityList);
            }

            @Override
            public void onFail() {
                loadArticleListFromDB(columnSlug);
            }
        });
    }

    private void saveArticleList(final List<ArticleEntity> articleEntityList) {
        AsyncUtil.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                for (ArticleEntity articleEntity : articleEntityList) {
                    DbUtil.getArticleEntityDao().insertOrReplace(articleEntity);
                }
            }
        });
    }

    private void loadArticleListFromDB(final String columnSlug) {
        loadArticleListFromDB(columnSlug, 0, -1, true);
    }

    private void loadArticleListFromDB(final String columnSlug, final int offset, final int limit, final boolean clearOld) {
        AsyncUtil.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                QueryBuilder queryBuilder = DbUtil.getArticleEntityDao().queryBuilder()
                        .where(ArticleEntityDao.Properties.ColumnSlug.eq(columnSlug))
                        .orderDesc(ArticleEntityDao.Properties.Slug);
                if (limit > 0) {
                    queryBuilder.limit(limit)
                            .offset(offset);
                }
                final List<ArticleEntity> articleEntityList = queryBuilder.list();
                Log.d("cxh", articleEntityList.toString());
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mArticleListV.onArticleListLoaded(articleEntityList, clearOld);
                    }
                });
            }
        });

    }
}
