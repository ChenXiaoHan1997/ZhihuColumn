package practice.cxh.zhihuzhuanlan.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Administrator on 2018/9/1 0001.
 */

public class FileUtil {

    public static String HTML_PREF = "html";

    static {
        File file = new File(HTML_PREF);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private Context mContext;

    public FileUtil(Context context) {
        this.mContext = context;

    }
    public boolean saveText(String path, String txt) {
        FileOutputStream outputStream = null;
        BufferedWriter writer = null;
        try {
            outputStream = mContext.openFileOutput(path, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(txt);
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                Log.e("cxh", "", e);
            }
        }
    }

    public String readText(String path) {
        FileInputStream inputStream = null;
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            inputStream = mContext.openFileInput(path);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (IOException e) {
            return "";
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("cxh", "", e);
                }
            }
        }
    }
}
