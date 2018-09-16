package practice.cxh.zhihuzhuanlan.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrefUtil {

    private static Context sContext;
    private static SharedPreferences sSharedPref;

    public static void init(Application application) {
        sContext = application;
        sSharedPref = PreferenceManager.getDefaultSharedPreferences(sContext);
    }

    public static boolean getBooleanOrDefault(String key, boolean defaultValue) {
        return sSharedPref.getBoolean(key, defaultValue);
    }

    public static boolean setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sSharedPref.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }
}
