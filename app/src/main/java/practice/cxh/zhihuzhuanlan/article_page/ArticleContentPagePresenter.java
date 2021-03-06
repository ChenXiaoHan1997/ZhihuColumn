package practice.cxh.zhihuzhuanlan.article_page;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import java.io.File;
import java.util.List;

import practice.cxh.zhihuzhuanlan.Constants;
import practice.cxh.zhihuzhuanlan.bean.ArticleContent;
import practice.cxh.zhihuzhuanlan.db.ArticleEntityDao;
import practice.cxh.zhihuzhuanlan.entity.ArticleEntity;
import practice.cxh.zhihuzhuanlan.service.DownloadArticleContentService;
import practice.cxh.zhihuzhuanlan.util.AsyncUtil;
import practice.cxh.zhihuzhuanlan.util.DbUtil;
import practice.cxh.zhihuzhuanlan.util.FileUtil;
import practice.cxh.zhihuzhuanlan.util.HtmlUtil;
import practice.cxh.zhihuzhuanlan.util.HttpUtil;
import practice.cxh.zhihuzhuanlan.util.JsonUtil;

public class ArticleContentPagePresenter {

    private boolean mLoadedFromLocal;
    private boolean mLoadedFromNet;

    private ArticleContentActivity mActivity;
    private HttpUtil mHttpUtil;
    private Handler mUiHandler;

    public ArticleContentPagePresenter(ArticleContentActivity activity) {
        this.mActivity = activity;
        this.mHttpUtil = new HttpUtil(activity);
        this.mUiHandler = new Handler(Looper.getMainLooper());
    }

    public void loadArticleContent(final String articleSlug) {
        // 首先加载本地的数据
        loadArticleContentLocal(articleSlug);
        mHttpUtil.get(HttpUtil.API_BASE + HttpUtil.POSTS + "/" + articleSlug,
                new HttpUtil.HttpListener<String>() {
                    @Override
                    public void onSuccess(final String response) {
                        mLoadedFromNet = true;
                        AsyncUtil.executeAsync(new Runnable() {
                            @Override
                            public void run() {
                                ArticleContent articleContent = JsonUtil.decodeArticleContent(response);
                                final ArticleEntity articleEntity = ArticleEntity.convertFromArticleContent(articleContent);
                                notifyArticleListPage(articleSlug, true);
                                mUiHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mActivity.onArticleContentLoaded(articleEntity);
                                    }
                                });
                                saveArticleContent(articleContent);
                                // 后台下载图片
                                DownloadArticleContentService.downloadWebImages(mActivity, articleContent.getContent());
                            }
                        });

                    }

                    @Override
                    public void onFail(String statusCode) {
                        if (!mLoadedFromLocal && !mLoadedFromNet) {
                            mActivity.onArticleContentLoadFail();
                        }
                    }
                });
    }

    private void saveArticleContent(final ArticleContent articleContent) {
        AsyncUtil.executeAsync(new Runnable() {
            @Override
            public void run() {
                // 将文章的作者、头像等信息保存到数据库
                ArticleEntity tmp = DbUtil.getArticleEntityDao()
                        .queryBuilder()
                        .where(ArticleEntityDao.Properties.Slug.eq(articleContent.getSlug()))
                        .unique();
                // 此处先查询出同slug的对象，主要是为了使文章列表中的对象同步更新
                ArticleEntity articleEntity = tmp == null ?
                        new ArticleEntity() : tmp;
                articleEntity.copyFromArticleContent(articleContent);
                // 将文章内容html保存到files/htmls/<slug>中
                FileUtil.saveTextToFile(FileUtil.HTMLS_DIR + File.separator + articleContent.getSlug(),
                        articleEntity.getContent());
                // 将替换过图片地址的文章内容html保存到files/htmls_local_pic/<slug>中
                FileUtil.saveTextToFile(FileUtil.HTMLS_LOCAL_PIC_DIR + File.separator + articleContent.getSlug(),
                        HtmlUtil.replaceWebImgSrc(articleEntity.getContent()));
                DbUtil.getArticleEntityDao().update(articleEntity);
            }
        });
    }

    private void loadArticleContentLocal(final String articleSlug) {
        AsyncUtil.executeAsync(new Runnable() {
            @Override
            public void run() {
                List<ArticleEntity> articleEntityList = DbUtil.getArticleEntityDao()
                        .queryBuilder()
                        .where(ArticleEntityDao.Properties.Slug.eq(articleSlug))
                        .list();
                if (articleEntityList.size() > 0) {
                    final ArticleEntity articleEntity = articleEntityList.get(0);
                    String content = FileUtil.readTextFromFile(FileUtil.HTMLS_LOCAL_PIC_DIR + File.separator + articleSlug);
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
