package practice.cxh.zhihuzhuanlan.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

import practice.cxh.zhihuzhuanlan.bean.Article;
import practice.cxh.zhihuzhuanlan.bean.ArticleContent;
import practice.cxh.zhihuzhuanlan.util.StringUtil;

@Entity
public class ArticleEntity implements Serializable {

    public static final int NO_CACHE = 0;
    public static final int DOWNLOADING = 1;
    public static final int DOWNLOAD_SUCCESS = 2;

    @Id
    private String slug;
    private String columnSlug;
    private String title;
    private String publishedTime;
    private String titleImage;
    private String author;
    private String avatar;
    private String summary;
    private int likesCount;
    private int downloadState;
    @Transient
    private String content;

    @Generated(hash = 262149160)
    public ArticleEntity(String slug, String columnSlug, String title, String publishedTime,
            String titleImage, String author, String avatar, String summary, int likesCount,
            int downloadState) {
        this.slug = slug;
        this.columnSlug = columnSlug;
        this.title = title;
        this.publishedTime = publishedTime;
        this.titleImage = titleImage;
        this.author = author;
        this.avatar = avatar;
        this.summary = summary;
        this.likesCount = likesCount;
        this.downloadState = downloadState;
    }

    @Generated(hash = 1301498493)
    public ArticleEntity() {
    }

    public static ArticleEntity convertFromArticle(Article article, String columnSlug) {
        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.slug = article.getSlug();
        articleEntity.columnSlug = columnSlug;
        articleEntity.title = article.getTitle();
        articleEntity.publishedTime = article.getPublishedTime();
        articleEntity.titleImage = article.getTitleImage();
        articleEntity.summary = article.getSummary();
        articleEntity.likesCount = article.getLikesCount();
        articleEntity.author = "";
        articleEntity.avatar = "";
        articleEntity.content = "";
        return articleEntity;
    }

    public static ArticleEntity convertFromArticleContent(ArticleContent articleContent) {
        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.slug = articleContent.getSlug();
        articleEntity.columnSlug = articleContent.getColumn() == null?
                "": articleContent.getColumn().getSlug();
        articleEntity.title = articleContent.getTitle();
        articleEntity.publishedTime = articleContent.getPublishedTime();
        articleEntity.titleImage = articleContent.getTitleImage();
        articleEntity.author = articleContent.getAuthor() == null?
                "": articleContent.getAuthor().getName();
        articleEntity.avatar = StringUtil.getAvatarUrl(articleContent.getAuthor(), "m");
        articleEntity.summary = articleContent.getSummary();
        articleEntity.likesCount = articleContent.getLikesCount();
        articleEntity.content = articleContent.getContent();
        return articleEntity;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
