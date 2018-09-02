package practice.cxh.zhihuzhuanlan;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

import practice.cxh.zhihuzhuanlan.db.DaoMaster;
import practice.cxh.zhihuzhuanlan.db.DaoSession;

public class ZhihuZhuanlanApplication extends Application {

    private static final String DB_NAME = "zhihuzhuanlan.db";

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        initGreenDao();
    }

    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, DB_NAME);
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
