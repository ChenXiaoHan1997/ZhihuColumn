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
import practice.cxh.zhihuzhuanlan.util.HttpUtil;
import practice.cxh.zhihuzhuanlan.util.JsonUtil;

public class ArticleListPagePresenter {

    private ArticleListActivity mActivity;
    private HttpUtil mHttpUtil;
    private ArticleEntityDao mArticleEntityDao;
    private Handler mUiHandler;

    public ArticleListPagePresenter(ArticleListActivity activity) {
        this.mActivity = activity;
        mHttpUtil = new HttpUtil(mActivity);
        mArticleEntityDao = ((ZhihuZhuanlanApplication) mActivity.getApplication()).getDaoSession().getArticleEntityDao();
        mUiHandler = new Handler(mActivity.getMainLooper());
    }

    public void loadArticleList(final String columnSlug) {
        mHttpUtil.get(HttpUtil.API_BASE + HttpUtil.COLUMN + "/" + columnSlug + "/" + HttpUtil.POSTS, new HttpUtil.HttpListener() {
            @Override
            public void onSuccess(String response) {
                List<Article> articlesList = JsonUtil.decodeArticleList(response);
                List<ArticleEntity> articleEntityList = new ArrayList<ArticleEntity>();
                for (Article article : articlesList) {
                    ArticleEntity articleEntity = ArticleEntity.convertFromArticle(article);
                    articleEntity.setColumnSlug(columnSlug);
                    articleEntityList.add(articleEntity);
                }
                mActivity.onArticleListLoaded(articleEntityList);
                saveArticleList(columnSlug, articleEntityList);
            }

            @Override
            public void onFail() {
                loadArticleListFromDB(columnSlug);
            }
        });
    }

    private void saveArticleList(final String columnSlug, final List<ArticleEntity> articleEntityList) {
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
                final List<ArticleEntity> articleEntityList = mArticleEntityDao.queryRaw("where COLUMN_SLUG = ?", columnSlug);
                Log.d("cxh", articleEntityList.toString());
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mActivity.onArticleListLoaded(articleEntityList);
                    }
                });
            }
        });

    }
}
