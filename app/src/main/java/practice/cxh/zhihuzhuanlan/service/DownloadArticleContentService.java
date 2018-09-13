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
import practice.cxh.zhihuzhuanlan.util.HtmlUtil;
import practice.cxh.zhihuzhuanlan.util.HttpUtil;
import practice.cxh.zhihuzhuanlan.util.JsonUtil;

public class DownloadArticleContentService extends IntentService {

    private static final String NAME = "downloadArticleContentService";
    private static final String ARTICLE_SLUG = "article_slug";
    private static final String HTML = "html";
    private static final String CMD = "cmd";
    private static final int CMD_DOWNLOAD_ARTICLE = 0;
    private static final int CMD_DOWNLOAD_PICS = 1;

    public DownloadArticleContentService(String name) {
        super(name);
    }

    public DownloadArticleContentService() {
        super(NAME);
    }

    public static void downloadArticleContent(Activity activity, String articleSlug) {
        Intent intent = new Intent(activity, DownloadArticleContentService.class);
        intent.putExtra(CMD, CMD_DOWNLOAD_ARTICLE);
        intent.putExtra(ARTICLE_SLUG, articleSlug);
        activity.startService(intent);
    }

    public static void downloadWebImages(Activity activity, String html) {
        Intent intent = new Intent(activity, DownloadArticleContentService.class);
        intent.putExtra(CMD, CMD_DOWNLOAD_ARTICLE);
        intent.putExtra(HTML, html);
        activity.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }
        int cmd = intent.getIntExtra(CMD, -1);
        switch (cmd) {
            case CMD_DOWNLOAD_ARTICLE:
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
                                downloadWebImages(articleContent.getContent());
                            }

                            @Override
                            public void onFail(String statusCode) {
                                notifyForeground(articleSlug, false);
                            }
                        });
                break;
            case CMD_DOWNLOAD_PICS:
                String html = intent.getStringExtra(HTML);
                downloadWebImages(html);
                break;
        }

    }

    private void downloadWebImages(String html) {
        List<String> imageUrls = HtmlUtil.getWebImages(html);
        for (final String url : imageUrls) {
            Log.d("tag1", "try download image: " + url);
            HttpUtil.getBytes(url, new HttpUtil.HttpListener<byte[]>() {
                @Override
                public void onSuccess(byte[] data) {
                    Log.d("tag1", "-----succeed in downloading: " + url);
                    String imageFileName = FileUtil.getWebImageFilename(url);
                    FileUtil.saveDataToFile(FileUtil.WEB_IMGAGES_DIR
                                    + File.separator + imageFileName, data);
                }

                @Override
                public void onFail(String statusCode) {

                }
            });
        }
    }

    private void saveArticleContent(final ArticleContent articleContent) {
        AsyncUtil.getThreadPool().execute(new Runnable() {
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

    private void notifyForeground(String articleSlug, boolean success) {
        Intent intent = new Intent(Constants.BC_DOWNLOAD_FINISH);
        intent.putExtra(Constants.BC_SLUG, articleSlug);
        intent.putExtra(Constants.BC_SUCCESS, success);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
