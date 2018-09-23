package practice.cxh.zhihuzhuanlan;

import android.app.Application;

import practice.cxh.zhihuzhuanlan.util.DbUtil;
import practice.cxh.zhihuzhuanlan.util.FileUtil;
import practice.cxh.zhihuzhuanlan.util.HttpUtil;
import practice.cxh.zhihuzhuanlan.util.SharedPrefUtil;

public class ZhihuZhuanlanApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HttpUtil.init(this);
        DbUtil.init(this);
        FileUtil.init(this);
        SharedPrefUtil.init(this);
    }
}
