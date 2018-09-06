package practice.cxh.zhihuzhuanlan.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import practice.cxh.zhihuzhuanlan.entity.ArticleContentEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ARTICLE_CONTENT_ENTITY".
*/
public class ArticleContentEntityDao extends AbstractDao<ArticleContentEntity, String> {

    public static final String TABLENAME = "ARTICLE_CONTENT_ENTITY";

    /**
     * Properties of entity ArticleContentEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Slug = new Property(0, String.class, "slug", true, "SLUG");
        public final static Property Author = new Property(1, String.class, "author", false, "AUTHOR");
        public final static Property Avatar = new Property(2, String.class, "avatar", false, "AVATAR");
        public final static Property Content = new Property(3, String.class, "content", false, "CONTENT");
    }


    public ArticleContentEntityDao(DaoConfig config) {
        super(config);
    }
    
    public ArticleContentEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ARTICLE_CONTENT_ENTITY\" (" + //
                "\"SLUG\" TEXT PRIMARY KEY NOT NULL ," + // 0: slug
                "\"AUTHOR\" TEXT," + // 1: author
                "\"AVATAR\" TEXT," + // 2: avatar
                "\"CONTENT\" TEXT);"); // 3: content
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ARTICLE_CONTENT_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ArticleContentEntity entity) {
        stmt.clearBindings();
 
        String slug = entity.getSlug();
        if (slug != null) {
            stmt.bindString(1, slug);
        }
 
        String author = entity.getAuthor();
        if (author != null) {
            stmt.bindString(2, author);
        }
 
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(3, avatar);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(4, content);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ArticleContentEntity entity) {
        stmt.clearBindings();
 
        String slug = entity.getSlug();
        if (slug != null) {
            stmt.bindString(1, slug);
        }
 
        String author = entity.getAuthor();
        if (author != null) {
            stmt.bindString(2, author);
        }
 
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(3, avatar);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(4, content);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public ArticleContentEntity readEntity(Cursor cursor, int offset) {
        ArticleContentEntity entity = new ArticleContentEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // slug
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // author
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // avatar
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // content
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ArticleContentEntity entity, int offset) {
        entity.setSlug(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setAuthor(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setAvatar(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setContent(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final String updateKeyAfterInsert(ArticleContentEntity entity, long rowId) {
        return entity.getSlug();
    }
    
    @Override
    public String getKey(ArticleContentEntity entity) {
        if(entity != null) {
            return entity.getSlug();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ArticleContentEntity entity) {
        return entity.getSlug() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
