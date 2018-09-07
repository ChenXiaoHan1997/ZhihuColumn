package practice.cxh.zhihuzhuanlan.article_page;

import android.os.Handler;

import java.io.File;
import java.util.List;

import practice.cxh.zhihuzhuanlan.bean.ArticleContent;
import practice.cxh.zhihuzhuanlan.db.ArticleContentEntityDao;
import practice.cxh.zhihuzhuanlan.db.ArticleEntityDao;
import practice.cxh.zhihuzhuanlan.entity.ArticleContentEntity;
import practice.cxh.zhihuzhuanlan.entity.ArticleEntity;
import practice.cxh.zhihuzhuanlan.util.AsyncUtil;
import practice.cxh.zhihuzhuanlan.util.DbUtil;
import practice.cxh.zhihuzhuanlan.util.FileUtil;
import practice.cxh.zhihuzhuanlan.util.HttpUtil;
import practice.cxh.zhihuzhuanlan.util.JsonUtil;

public class ArticleContentPagePresenter {

    private ArticleContentActivity mActivity;
    private Handler mUiHandler;

    public ArticleContentPagePresenter(ArticleContentActivity activity) {
        this.mActivity = activity;
        mUiHandler = new Handler(mActivity.getMainLooper());
    }

    public void loadArticleContent(final String articleSlug) {
        HttpUtil.get(HttpUtil.API_BASE + HttpUtil.POSTS + "/" + articleSlug, new HttpUtil.HttpListener() {
            @Override
            public void onSuccess(String response) {
                ArticleContent articleContent = JsonUtil.decodeArticleContent(response);
                ArticleContentEntity articleContentEntity = ArticleContentEntity.convertFromArticleContent(articleContent);
                mActivity.onArticleContentLoaded(articleContentEntity);
                saveArticleContent(articleContent);
            }

            @Override
            public void onFail() {
                loadArticleContentLocal(articleSlug);
            }
        });
    }

    private void saveArticleContent(final ArticleContent articleContent) {
        AsyncUtil.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                FileUtil.saveText(FileUtil.HTMLS_DIR + File.separator + articleContent.getSlug(), articleContent.getContent());
                List<ArticleEntity> tmp = DbUtil.getArticleEntityDao().queryBuilder()
                        .where(ArticleEntityDao.Properties.Slug.eq(articleContent.getSlug()))
                        .list();
                if (tmp.size() > 0) {
                    ArticleEntity articleEntity = tmp.get(0);
                    articleEntity.setDownloadState(ArticleEntity.DOWNLOAD_SUCCESS);
                    DbUtil.getArticleEntityDao().update(articleEntity);
                }
            }
        });
    }

    private void loadArticleContentLocal(final String articleSlug) {
        AsyncUtil.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                List<ArticleContentEntity> articleContentEntityList = DbUtil.getArticleContentEntityDao()
                        .queryBuilder()
                        .where(ArticleContentEntityDao.Properties.Slug.eq(articleSlug))
                        .list();
                if (articleContentEntityList.size() > 0) {
                    final ArticleContentEntity articleContentEntity = articleContentEntityList.get(0);
                    String content = FileUtil.readText(FileUtil.HTMLS_DIR + File.separator + articleSlug);
                    articleContentEntity.setContent(content);
                    mUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mActivity.onArticleContentLoaded(articleContentEntity);
                        }
                    });
                }
            }
        });
    }
}
