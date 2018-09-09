package practice.cxh.zhihuzhuanlan.article_page;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import java.io.File;
import java.util.List;

import practice.cxh.zhihuzhuanlan.Constants;
import practice.cxh.zhihuzhuanlan.bean.ArticleContent;
import practice.cxh.zhihuzhuanlan.db.ArticleEntityDao;
import practice.cxh.zhihuzhuanlan.entity.ArticleEntity;
import practice.cxh.zhihuzhuanlan.util.AsyncUtil;
import practice.cxh.zhihuzhuanlan.util.DbUtil;
import practice.cxh.zhihuzhuanlan.util.FileUtil;
import practice.cxh.zhihuzhuanlan.util.HttpUtil;
import practice.cxh.zhihuzhuanlan.util.JsonUtil;

public class ArticleContentPagePresenter {

    private boolean mLoadedFromLocal;
    private boolean mLoadedFromNet;

    private ArticleContentActivity mActivity;
    private Handler mUiHandler;

    public ArticleContentPagePresenter(ArticleContentActivity activity) {
        this.mActivity = activity;
        mUiHandler = new Handler(mActivity.getMainLooper());
    }

    public void loadArticleContent(final String articleSlug) {
        // 首先加载本地的数据
        loadArticleContentLocal(articleSlug);
        HttpUtil.get(HttpUtil.API_BASE + HttpUtil.POSTS + "/" + articleSlug,
                new HttpUtil.HttpListener<String>() {
            @Override
            public void onSuccess(String response) {
                mLoadedFromNet = true;
                ArticleContent articleContent = JsonUtil.decodeArticleContent(response);
                ArticleEntity articleEntity = ArticleEntity.convertFromArticleContent(articleContent);
                notifyArticleListPage(articleSlug, true);
                mActivity.onArticleContentLoaded(articleEntity);
                saveArticleContent(articleContent);
            }

            @Override
            public void onFail() {
                if (!mLoadedFromLocal && !mLoadedFromNet) {
                    mActivity.onArticleContentLoadFail();
                }
            }
        });
    }

    private void saveArticleContent(final ArticleContent articleContent) {
        AsyncUtil.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                // 将文章的作者、头像等信息保存到数据库
                List<ArticleEntity> tmp = DbUtil.getArticleEntityDao()
                        .queryBuilder()
                        .where(ArticleEntityDao.Properties.Slug.eq(articleContent.getSlug()))
                        .list();
                // 此处先查询出同slug的对象，主要是为了使文章列表中的对象同步更新
                ArticleEntity articleEntity = tmp.size() > 0 ?
                        tmp.get(0) : new ArticleEntity();
                articleEntity.copyFromArticleContent(articleContent);
                // 将文章内容html保存到files/htmls/<slug>中
                FileUtil.saveTextToFile(FileUtil.HTMLS_DIR + File.separator + articleContent.getSlug(), articleEntity.getContent());
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
                    String content = FileUtil.readTextFromFile(FileUtil.HTMLS_DIR + File.separator + articleSlug);
                    if (TextUtils.isEmpty(content)) {
                        return;
                    }
                    mLoadedFromLocal = true;
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

    private void notifyArticleListPage(String articleSlug, boolean success) {
        // TODO 发送广播
        Intent intent = new Intent(Constants.BC_DOWNLOAD_FINISH);
        intent.putExtra(Constants.BC_SLUG, articleSlug);
        intent.putExtra(Constants.BC_SUCCESS, success);
        LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
    }
}
