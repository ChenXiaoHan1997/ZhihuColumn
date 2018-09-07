package practice.cxh.zhihuzhuanlan.article_page;

import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.util.List;

import javax.crypto.AEADBadTagException;

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
                Log.d("cxh", "------------success-----------");
                ArticleContent articleContent = JsonUtil.decodeArticleContent(response);
                ArticleEntity articleEntity = ArticleEntity.convertFromArticleContent(articleContent);
                mActivity.onArticleContentLoaded(articleEntity);
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
                ArticleEntity articleEntity = ArticleEntity.convertFromArticleContent(articleContent);
                articleEntity.setDownloadState(ArticleEntity.DOWNLOAD_SUCCESS);
                DbUtil.getArticleEntityDao().update(articleEntity);
            }
        });
    }

    private void loadArticleContentLocal(final String articleSlug) {
        AsyncUtil.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                List<ArticleEntity> articleEntityList = DbUtil.getArticleEntityDao()
                        .queryBuilder()
                        .where(ArticleEntityDao.Properties.Slug.eq(articleSlug))
                        .list();
                if (articleEntityList.size() > 0) {
                    final ArticleEntity articleEntity = articleEntityList.get(0);
                    String content = FileUtil.readText(FileUtil.HTMLS_DIR + File.separator + articleSlug);
                    articleEntity.setContent(content);
                    mUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mActivity.onArticleContentLoaded(articleEntity);
                        }
                    });
                }
            }
        });
    }
}
