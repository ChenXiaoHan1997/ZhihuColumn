package practice.cxh.zhihuzhuanlan.column_page;

import android.os.Handler;
import android.util.Log;

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

    private ArticleListV mArticleListV;
    private ArticleEntityDao mArticleEntityDao;
    private Handler mUiHandler;

    public ArticleListPagePresenter(ArticleListV articleListV) {
        this.mArticleListV = articleListV;
        mArticleEntityDao = DbUtil.getDaoSession().getArticleEntityDao();
        mUiHandler = new Handler();
    }

    public void loadArticleList(final String columnSlug, int offset) {
        HttpUtil.get(HttpUtil.API_BASE + HttpUtil.COLUMN + "/" + columnSlug + "/" + HttpUtil.POSTS + "?offset=" + offset,
                new HttpUtil.HttpListener() {
            @Override
            public void onSuccess(String response) {
                List<Article> articlesList = JsonUtil.decodeArticleList(response);
                List<ArticleEntity> articleEntityList = new ArrayList<ArticleEntity>();
                for (Article article : articlesList) {
                    ArticleEntity articleEntity = ArticleEntity.convertFromArticle(article);
                    articleEntity.setColumnSlug(columnSlug);
                    List<ArticleEntity> tmp = mArticleEntityDao.queryRaw("where " + ArticleEntityDao.Properties.Slug.columnName + " = ?", articleEntity.getSlug());
                    if (tmp.size() > 0) {
                        articleEntity.setDownloadState(tmp.get(0).getDownloadState());
                    }
                    articleEntityList.add(articleEntity);
                }
                mArticleListV.onArticleListLoaded(articleEntityList);
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
                    mArticleEntityDao.insertOrReplace(articleEntity);
                }
            }
        });

    }

    private void loadArticleListFromDB(final String columnSlug) {
        AsyncUtil.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                final List<ArticleEntity> articleEntityList = mArticleEntityDao.queryRaw("where " + ArticleEntityDao.Properties.ColumnSlug.columnName + " = ?", columnSlug);
                Log.d("cxh", articleEntityList.toString());
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mArticleListV.onArticleListLoaded(articleEntityList);
                    }
                });
            }
        });

    }
}
