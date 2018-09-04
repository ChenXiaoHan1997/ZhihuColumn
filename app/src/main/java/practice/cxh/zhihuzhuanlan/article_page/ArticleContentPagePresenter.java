package practice.cxh.zhihuzhuanlan.article_page;

import android.os.Handler;
import android.text.TextUtils;

import java.util.List;

import practice.cxh.zhihuzhuanlan.ZhihuZhuanlanApplication;
import practice.cxh.zhihuzhuanlan.bean.ArticleContent;
import practice.cxh.zhihuzhuanlan.db.ArticleEntityDao;
import practice.cxh.zhihuzhuanlan.entity.ArticleEntity;
import practice.cxh.zhihuzhuanlan.util.AsyncUtil;
import practice.cxh.zhihuzhuanlan.util.FileUtil;
import practice.cxh.zhihuzhuanlan.util.HttpUtil;
import practice.cxh.zhihuzhuanlan.util.JsonUtil;

public class ArticleContentPagePresenter {

    private ArticleContentActivity mActivity;
    private HttpUtil mHttpUtil;
    private FileUtil mFileUtil;
    private ArticleEntityDao mArticleEntityDao;
    private Handler mUiHandler;

    public ArticleContentPagePresenter(ArticleContentActivity activity) {
        this.mActivity = activity;
        mHttpUtil = new HttpUtil(mActivity.getApplicationContext());
        mFileUtil = new FileUtil(mActivity);
        mArticleEntityDao = ((ZhihuZhuanlanApplication) mActivity.getApplication()).getDaoSession().getArticleEntityDao();
        mUiHandler = new Handler(mActivity.getMainLooper());
    }

    public void loadArticleContent(final String articleSlug) {
        mHttpUtil.get(HttpUtil.API_BASE + HttpUtil.POSTS + "/" + articleSlug, new HttpUtil.HttpListener() {
            @Override
            public void onSuccess(String response) {
                ArticleContent articleContent = JsonUtil.decodeArticleContent(response);
                mActivity.onArticleContentLoaded(articleContent.getContent());
                saveArticleContent(articleSlug, articleContent);
            }

            @Override
            public void onFail() {
                loadArticleContentFromFile(articleSlug);
            }
        });
    }

    private void saveArticleContent(final String articleSlug, final ArticleContent articleContent) {
        AsyncUtil.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                mFileUtil.saveText(FileUtil.HTML_PREF + "_" + articleSlug, articleContent.getContent());
                List<ArticleEntity> tmp = mArticleEntityDao.queryRaw("where " + ArticleEntityDao.Properties.Slug.columnName + " = ?", articleSlug);
                if (tmp.size() > 0) {
                    ArticleEntity articleEntity = tmp.get(0);
                    articleEntity.setDownloadState(ArticleEntity.DOWNLOAD_SUCCEED);
                    mArticleEntityDao.update(articleEntity);
                }
            }
        });
    }

    private void loadArticleContentFromFile(final String articleSlug) {
        AsyncUtil.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                final String content = mFileUtil.readText(FileUtil.HTML_PREF + "_" + articleSlug);
                if (!TextUtils.isEmpty(content)) {
                    mUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mActivity.onArticleContentLoaded(content);
                        }
                    });
                }
            }
        });
    }
}
