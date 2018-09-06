package practice.cxh.zhihuzhuanlan.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

import practice.cxh.zhihuzhuanlan.bean.Article;

@Entity
public class ArticleEntity implements Serializable {

    public static final int NO_CACHE = 0;
    public static final int DOWNLOADING = 1;
    public static final int DOWNLOAD_SUCCEED = 2;

    @Id
    private String slug;
    private String columnSlug;
    private String title;
    private String publishedTime;
    private String titleImage;
    private String summary;
    private int likesCount;
    private int downloadState;



    @Generated(hash = 1479153391)
    public ArticleEntity(String slug, String columnSlug, String title,
            String publishedTime, String titleImage, String summary,
            int likesCount, int downloadState) {
        this.slug = slug;
        this.columnSlug = columnSlug;
        this.title = title;
        this.publishedTime = publishedTime;
        this.titleImage = titleImage;
        this.summary = summary;
        this.likesCount = likesCount;
        this.downloadState = downloadState;
    }

    @Generated(hash = 1301498493)
    public ArticleEntity() {
    }



    public static ArticleEntity convertFromArticle(Article article) {
        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.slug = article.getSlug();
        articleEntity.title = article.getTitle();
        articleEntity.publishedTime = article.getPublishedTime();
        articleEntity.titleImage = article.getTitleImage();
        articleEntity.summary = article.getSummary();
        articleEntity.likesCount = article.getLikesCount();
        return articleEntity;
    }

    public static Article convertToArticle(ArticleEntity articleEntity) {
        Article article = new Article();
        article.setSlug(articleEntity.slug);
        article.setTitle(articleEntity.title);
        article.setTitleImage(articleEntity.titleImage);
        article.setSummary(articleEntity.summary);
        return article;
    }


    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getColumnSlug() {
        return columnSlug;
    }

    public void setColumnSlug(String columnSlug) {
        this.columnSlug = columnSlug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishedTime() {
        return publishedTime;
    }

    public void setPublishedTime(String publishedTime) {
        this.publishedTime = publishedTime;
    }

    public String getTitleImage() {
        return titleImage;
    }

    public void setTitleImage(String titleImage) {
        this.titleImage = titleImage;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(int downloadState) {
        this.downloadState = downloadState;
    }
}
