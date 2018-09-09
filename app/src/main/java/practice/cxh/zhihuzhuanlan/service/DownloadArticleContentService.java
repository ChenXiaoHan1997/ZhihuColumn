package practice.cxh.zhihuzhuanlan.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

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

public class DownloadArticleContentService extends IntentService {

    private static final String NAME = "downloadArticleContentService";
    private static final String ARTICLE_SLUG = "article_slug";

    public DownloadArticleContentService(String name) {
        super(name);
    }

    public DownloadArticleContentService() {
        super(NAME);
    }

    public static void downloadArticleContent(Activity activity, String articleSlug) {
        Intent intent = new Intent(activity, DownloadArticleContentService.class);
        intent.putExtra(ARTICLE_SLUG, articleSlug);
        activity.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }
        final String articleSlug = intent.getStringExtra(ARTICLE_SLUG);
        Log.d("tag1", "try download " + articleSlug);
        HttpUtil.get(HttpUtil.API_BASE + HttpUtil.POSTS + "/" + articleSlug,
                new HttpUtil.HttpListener<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d("tag1", "--------------下载成功");
                ArticleContent articleContent = JsonUtil.decodeArticleContent(response);
                notifyForeground(articleSlug, true);
                saveArticleContent(articleContent);
            }

            @Override
            public void onFail() {
                notifyForeground(articleSlug, false);
            }
        });
    }

    private void saveArticleContent(final ArticleContent articleContent) {
        AsyncUtil.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                List<ArticleEntity> tmp = DbUtil.getArticleEntityDao()
                        .queryBuilder()
                        .where(ArticleEntityDao.Properties.Slug.eq(articleContent.getSlug()))
                        .list();
                ArticleEntity articleEntity = tmp.size() > 0?
                        tmp.get(0): new ArticleEntity();
                articleEntity.copyFromArticleContent(articleContent);
                FileUtil.saveTextToFile(FileUtil.HTMLS_DIR + File.separator + articleContent.getSlug(), articleEntity.getContent());
                DbUtil.getArticleEntityDao().update(articleEntity);
            }
        });
    }

    private void notifyForeground(String articleSlug, boolean success) {
        Intent intent = new Intent(Constants.BC_DOWNLOAD_FINISH);
        intent.putExtra(Constants.BC_SLUG, articleSlug);
        intent.putExtra(Constants.BC_SUCCESS, success);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
