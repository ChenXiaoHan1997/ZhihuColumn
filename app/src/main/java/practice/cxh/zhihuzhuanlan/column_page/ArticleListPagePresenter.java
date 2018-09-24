package practice.cxh.zhihuzhuanlan.column_page;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import practice.cxh.zhihuzhuanlan.base.BasePresenter;
import practice.cxh.zhihuzhuanlan.bean.Article;
import practice.cxh.zhihuzhuanlan.db.ArticleEntityDao;
import practice.cxh.zhihuzhuanlan.entity.ArticleEntity;
import practice.cxh.zhihuzhuanlan.entity.SubscribeEntity;
import practice.cxh.zhihuzhuanlan.event_pool.EventPool;
import practice.cxh.zhihuzhuanlan.event_pool.IEvent;
import practice.cxh.zhihuzhuanlan.event_pool.event.SubscribeEvent;
import practice.cxh.zhihuzhuanlan.util.AsyncUtil;
import practice.cxh.zhihuzhuanlan.util.DbUtil;
import practice.cxh.zhihuzhuanlan.util.HttpUtil;
import practice.cxh.zhihuzhuanlan.util.JsonUtil;

public class ArticleListPagePresenter extends BasePresenter<ArticleListV> {

    private Context mContext;
    private Handler mUiHandler;
    private HttpUtil mHttpUtil;

    public ArticleListPagePresenter(Context context) {
        this.mContext = context;
        this.mHttpUtil = new HttpUtil(context);
        this.mUiHandler = new Handler(Looper.getMainLooper());
    }

    public void loadArticles(final String columnSlug, final int offset, final int limit) {
        loadArticlesFromDB(columnSlug, offset, limit);
        mHttpUtil.get(HttpUtil.API_BASE + HttpUtil.COLUMN + "/" + columnSlug
                        + "/" + HttpUtil.POSTS + "?offset=" + offset
                        + "&limit=" + limit,
                new HttpUtil.HttpListener<String>() {
                    @Override
                    public void onSuccess(final String response) {
                        AsyncUtil.executeAsync(new Runnable() {
                            @Override
                            public void run() {
                                List<Article> articlesList = JsonUtil.decodeArticleList(response);
                                final List<ArticleEntity> articleEntityList = new ArrayList<ArticleEntity>();
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
                                mUiHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (getView() != null) {
                                            getView().onArticlesLoaded(articleEntityList, offset, limit);
                                        }
                                    }
                                });
                                saveArticleList(articleEntityList);
                            }
                        });
                    }

                    @Override
                    public void onFail(String statusCode) {

                    }
                });
    }

    private void loadArticlesFromDB(final String columnSlug, final int offset, final int limit) {
        AsyncUtil.executeAsync(new Runnable() {
            @Override
            public void run() {
                QueryBuilder queryBuilder = DbUtil.getArticleEntityDao()
                        .queryBuilder()
                        .where(ArticleEntityDao.Properties.ColumnSlug.eq(columnSlug))
                        .orderDesc(ArticleEntityDao.Properties.Slug);
                if (limit > 0) {
                    queryBuilder.limit(limit)
                            .offset(offset);
                }
                final List<ArticleEntity> articleEntityList = queryBuilder.list();
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (getView() != null) {
                            getView().onArticlesLoaded(articleEntityList, offset, limit);
                        }
                    }
                });
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

    public void setSubscribe(final String columnSlug, final boolean subscribe) {
        AsyncUtil.executeAsync(new Runnable() {
            @Override
            public void run() {
                SubscribeEntity subscribeEntity = new SubscribeEntity(columnSlug, subscribe);
                DbUtil.getSubscribeEntityDao()
                        .insertOrReplace(subscribeEntity);
                IEvent event = new SubscribeEvent(columnSlug, subscribe);
                EventPool.getInstace().publishEvent(event);
            }
        });
    }
}
