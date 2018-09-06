package practice.cxh.zhihuzhuanlan;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

import practice.cxh.zhihuzhuanlan.db.DaoMaster;
import practice.cxh.zhihuzhuanlan.db.DaoSession;
import practice.cxh.zhihuzhuanlan.util.DbUtil;
import practice.cxh.zhihuzhuanlan.util.FileUtil;
import practice.cxh.zhihuzhuanlan.util.HttpUtil;

public class ZhihuZhuanlanApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HttpUtil.init(this);
        DbUtil.init(this);
        FileUtil.init(this);
    }
}
