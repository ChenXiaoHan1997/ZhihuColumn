package practice.cxh.zhihuzhuanlan.util;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

import practice.cxh.zhihuzhuanlan.db.ArticleEntityDao;
import practice.cxh.zhihuzhuanlan.db.ColumnEntityDao;
import practice.cxh.zhihuzhuanlan.db.DaoMaster;
import practice.cxh.zhihuzhuanlan.db.DaoSession;
import practice.cxh.zhihuzhuanlan.db.SubscribeEntityDao;
import practice.cxh.zhihuzhuanlan.entity.SubscribeEntity;

public class DbUtil {

    private static final String DB_NAME = "zhihuzhuanlan.db";

    private static DaoSession sDaoSession;
    private static ColumnEntityDao sColumnEntityDao;
    private static ArticleEntityDao sArticleEntityDao;
    private static SubscribeEntityDao sSubscribeEntityDao;

    public static void init(Application application) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(application, DB_NAME);
        Database db = helper.getWritableDb();
        sDaoSession = new DaoMaster(db).newSession();
        sColumnEntityDao = sDaoSession.getColumnEntityDao();
        sArticleEntityDao = sDaoSession.getArticleEntityDao();
        sSubscribeEntityDao = sDaoSession.getSubscribeEntityDao();
    }

    public static DaoSession getDaoSession() {
        return sDaoSession;
    }

    public static ColumnEntityDao getColumnEntityDao() {
        return sColumnEntityDao;
    }

    public static ArticleEntityDao getArticleEntityDao() {
        return sArticleEntityDao;
    }

    public static SubscribeEntityDao getSubscribeEntityDao() {
        return sSubscribeEntityDao;
    }
}
