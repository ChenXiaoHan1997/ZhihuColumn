package practice.cxh.zhihuzhuanlan;

import android.provider.ContactsContract;

import practice.cxh.zhihuzhuanlan.util.SharedPrefUtil;

public class DataCore {

    private static final String KEY_FIRST_RUN = "first_run";

    private static class InstanceHolder {
        private static DataCore sInstance = new DataCore();
    }

    private DataCore() {

    }

    public static DataCore getInstance() {
        return InstanceHolder.sInstance;
    }

    public boolean isFirstRun() {
        return SharedPrefUtil.getBooleanOrDefault(KEY_FIRST_RUN, true);
    }

    public void setFirstRun(boolean isFirstRun) {
        SharedPrefUtil.setBoolean(KEY_FIRST_RUN, isFirstRun);
    }
}
