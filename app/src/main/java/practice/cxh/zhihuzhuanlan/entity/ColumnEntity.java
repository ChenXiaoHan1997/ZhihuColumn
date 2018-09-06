package practice.cxh.zhihuzhuanlan.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

import practice.cxh.zhihuzhuanlan.bean.Column;
import practice.cxh.zhihuzhuanlan.util.StringUtil;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ColumnEntity implements Serializable {

    @Id
    private String slug;
    private String name;
    private String avatar;
    private String description;
    private int followersCount;
    private int postsCount;




    @Generated(hash = 1305861907)
    public ColumnEntity(String slug, String name, String avatar,
            String description, int followersCount, int postsCount) {
        this.slug = slug;
        this.name = name;
        this.avatar = avatar;
        this.description = description;
        this.followersCount = followersCount;
        this.postsCount = postsCount;
    }

    @Generated(hash = 119934664)
    public ColumnEntity() {
    }




    public static ColumnEntity convertFromColumn(Column column) {
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.slug = column.getSlug();
        columnEntity.name = column.getName();
        columnEntity.avatar = StringUtil.getAvatarUrl(column.getAvatar(), "m");
        columnEntity.description = column.getDescription();
        columnEntity.followersCount = column.getFollowersCount();
        columnEntity.postsCount = column.getPostsCount();
        return columnEntity;
    }
    
    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getPostsCount() {
        return postsCount;
    }

    public void setPostsCount(int postsCount) {
        this.postsCount = postsCount;
    }
}
