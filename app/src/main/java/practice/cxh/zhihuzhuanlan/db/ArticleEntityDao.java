package practice.cxh.zhihuzhuanlan.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import practice.cxh.zhihuzhuanlan.entity.ArticleEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ARTICLE_ENTITY".
*/
public class ArticleEntityDao extends AbstractDao<ArticleEntity, String> {

    public static final String TABLENAME = "ARTICLE_ENTITY";

    /**
     * Properties of entity ArticleEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Slug = new Property(0, String.class, "slug", true, "SLUG");
        public final static Property ColumnSlug = new Property(1, String.class, "columnSlug", false, "COLUMN_SLUG");
        public final static Property Title = new Property(2, String.class, "title", false, "TITLE");
        public final static Property PublishedTime = new Property(3, String.class, "publishedTime", false, "PUBLISHED_TIME");
        public final static Property TitleImage = new Property(4, String.class, "titleImage", false, "TITLE_IMAGE");
        public final static Property Author = new Property(5, String.class, "author", false, "AUTHOR");
        public final static Property Avatar = new Property(6, String.class, "avatar", false, "AVATAR");
        public final static Property Summary = new Property(7, String.class, "summary", false, "SUMMARY");
        public final static Property LikesCount = new Property(8, int.class, "likesCount", false, "LIKES_COUNT");
        public final static Property DownloadState = new Property(9, int.class, "downloadState", false, "DOWNLOAD_STATE");
    }


    public ArticleEntityDao(DaoConfig config) {
        super(config);
    }
    
    public ArticleEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ARTICLE_ENTITY\" (" + //
                "\"SLUG\" TEXT PRIMARY KEY NOT NULL ," + // 0: slug
                "\"COLUMN_SLUG\" TEXT," + // 1: columnSlug
                "\"TITLE\" TEXT," + // 2: title
                "\"PUBLISHED_TIME\" TEXT," + // 3: publishedTime
                "\"TITLE_IMAGE\" TEXT," + // 4: titleImage
                "\"AUTHOR\" TEXT," + // 5: author
                "\"AVATAR\" TEXT," + // 6: avatar
                "\"SUMMARY\" TEXT," + // 7: summary
                "\"LIKES_COUNT\" INTEGER NOT NULL ," + // 8: likesCount
                "\"DOWNLOAD_STATE\" INTEGER NOT NULL );"); // 9: downloadState
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ARTICLE_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ArticleEntity entity) {
        stmt.clearBindings();
 
        String slug = entity.getSlug();
        if (slug != null) {
            stmt.bindString(1, slug);
        }
 
        String columnSlug = entity.getColumnSlug();
        if (columnSlug != null) {
            stmt.bindString(2, columnSlug);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(3, title);
        }
 
        String publishedTime = entity.getPublishedTime();
        if (publishedTime != null) {
            stmt.bindString(4, publishedTime);
        }
 
        String titleImage = entity.getTitleImage();
        if (titleImage != null) {
            stmt.bindString(5, titleImage);
        }
 
        String author = entity.getAuthor();
        if (author != null) {
            stmt.bindString(6, author);
        }
 
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(7, avatar);
        }
 
        String summary = entity.getSummary();
        if (summary != null) {
            stmt.bindString(8, summary);
        }
        stmt.bindLong(9, entity.getLikesCount());
        stmt.bindLong(10, entity.getDownloadState());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ArticleEntity entity) {
        stmt.clearBindings();
 
        String slug = entity.getSlug();
        if (slug != null) {
            stmt.bindString(1, slug);
        }
 
        String columnSlug = entity.getColumnSlug();
        if (columnSlug != null) {
            stmt.bindString(2, columnSlug);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(3, title);
        }
 
        String publishedTime = entity.getPublishedTime();
        if (publishedTime != null) {
            stmt.bindString(4, publishedTime);
        }
 
        String titleImage = entity.getTitleImage();
        if (titleImage != null) {
            stmt.bindString(5, titleImage);
        }
 
        String author = entity.getAuthor();
        if (author != null) {
            stmt.bindString(6, author);
        }
 
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(7, avatar);
        }
 
        String summary = entity.getSummary();
        if (summary != null) {
            stmt.bindString(8, summary);
        }
        stmt.bindLong(9, entity.getLikesCount());
        stmt.bindLong(10, entity.getDownloadState());
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public ArticleEntity readEntity(Cursor cursor, int offset) {
        ArticleEntity entity = new ArticleEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // slug
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // columnSlug
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // title
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // publishedTime
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // titleImage
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // author
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // avatar
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // summary
            cursor.getInt(offset + 8), // likesCount
            cursor.getInt(offset + 9) // downloadState
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ArticleEntity entity, int offset) {
        entity.setSlug(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setColumnSlug(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setTitle(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPublishedTime(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setTitleImage(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setAuthor(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setAvatar(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setSummary(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setLikesCount(cursor.getInt(offset + 8));
        entity.setDownloadState(cursor.getInt(offset + 9));
     }
    
    @Override
    protected final String updateKeyAfterInsert(ArticleEntity entity, long rowId) {
        return entity.getSlug();
    }
    
    @Override
    public String getKey(ArticleEntity entity) {
        if(entity != null) {
            return entity.getSlug();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ArticleEntity entity) {
        return entity.getSlug() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
