package practice.cxh.zhihuzhuanlan.util;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

import practice.cxh.zhihuzhuanlan.db.DaoMaster;
import practice.cxh.zhihuzhuanlan.db.DaoSession;

public class DbUtil {

    private static final String DB_NAME = "zhihuzhuanlan.db";

    private static DaoSession sDaoSession;

    public static void init(Application application) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(application, DB_NAME);
        Database db = helper.getWritableDb();
        sDaoSession = new DaoMaster(db).newSession();
    }

    public static DaoSession getDaoSession() {
        return sDaoSession;
    }
}
