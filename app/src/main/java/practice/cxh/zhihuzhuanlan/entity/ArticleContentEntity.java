package practice.cxh.zhihuzhuanlan.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import practice.cxh.zhihuzhuanlan.bean.ArticleContent;
import practice.cxh.zhihuzhuanlan.util.StringUtil;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ArticleContentEntity {
    @Id
    private String slug;
    private String author;
    private String avatar;
    private String content;

    @Generated(hash = 278465378)
    public ArticleContentEntity(String slug, String author, String avatar, String content) {
        this.slug = slug;
        this.author = author;
        this.avatar = avatar;
        this.content = content;
    }

    @Generated(hash = 220209226)
    public ArticleContentEntity() {
    }

    public static ArticleContentEntity convertFromArticleContent(ArticleContent articleContent) {
        ArticleContentEntity ariticleContentEntity = new ArticleContentEntity();
        ariticleContentEntity.slug = articleContent.getSlug();
        ariticleContentEntity.author = articleContent.getAuthor() == null?
                "": articleContent.getAuthor().getName();
        ariticleContentEntity.avatar = StringUtil.getAvatarUrl(articleContent.getAuthor(), "m");
        ariticleContentEntity.content = articleContent.getContent();
        return ariticleContentEntity;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
