package practice.cxh.zhihuzhuanlan.article_page;

import android.os.Handler;

import java.io.File;
import java.util.List;

import practice.cxh.zhihuzhuanlan.bean.ArticleContent;
import practice.cxh.zhihuzhuanlan.db.ArticleEntityDao;
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
        // 首先加载本地的数据
        loadArticleContentLocal(articleSlug);
        HttpUtil.get(HttpUtil.API_BASE + HttpUtil.POSTS + "/" + articleSlug, new HttpUtil.HttpListener() {
            @Override
            public void onSuccess(String response) {
                ArticleContent articleContent = JsonUtil.decodeArticleContent(response);
                ArticleEntity articleEntity = ArticleEntity.convertFromArticleContent(articleContent);
                mActivity.onArticleContentLoaded(articleEntity);
                saveArticleContent(articleContent);
            }

            @Override
            public void onFail() {
//                loadArticleContentLocal(articleSlug);
            }
        });
    }

    private void saveArticleContent(final ArticleContent articleContent) {
        AsyncUtil.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                // 将文章内容html保存到files/htmls/<slug>中
                FileUtil.saveText(FileUtil.HTMLS_DIR + File.separator + articleContent.getSlug(), articleContent.getContent());
                // 将文章的作者、头像等信息保存到数据库
                List<ArticleEntity> tmp = DbUtil.getArticleEntityDao()
                        .queryBuilder()
                        .where(ArticleEntityDao.Properties.Slug.eq(articleContent.getSlug()))
                        .list();
                // 此处先查询出同slug的对象，主要是为了使文章列表中的对象同步更新
                ArticleEntity articleEntity = tmp.size() > 0?
                        tmp.get(0): new ArticleEntity();
                articleEntity.copyFromArticleContent(articleContent);
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
