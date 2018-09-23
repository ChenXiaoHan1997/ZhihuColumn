package practice.cxh.zhihuzhuanlan.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class SubscribeEntity {

    @Id
    private String columnSlug;
    private boolean subscribed;

    @Generated(hash = 1388489979)
    public SubscribeEntity(String columnSlug, boolean subscribed) {
        this.columnSlug = columnSlug;
        this.subscribed = subscribed;
    }

    @Generated(hash = 767992259)
    public SubscribeEntity() {
    }

    public String getColumnSlug() {
        return columnSlug;
    }

    public void setColumnSlug(String columnSlug) {
        this.columnSlug = columnSlug;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public boolean getSubscribed() {
        return this.subscribed;
    }
}
