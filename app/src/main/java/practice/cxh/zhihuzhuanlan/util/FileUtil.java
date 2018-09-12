package practice.cxh.zhihuzhuanlan.util;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;

/**
 * Created by Administrator on 2018/9/1 0001.
 */

public class FileUtil {

    public static final String HTMLS_DIR = "htmls";
    public static final String HTMLS_LOCAL_PIC_DIR = "htmls_local_pic";
    public static final String WEB_IMGAGES_DIR = "web_images";

    private static Context sContext;
    private static File sFilesDir;
    private static File sHtmlsDir;
    private static File sHtmlLocalPicDir;
    private static File sWebImgDir;

    public static void init(Application application) {
        sContext = application;
        sFilesDir = sContext.getFilesDir();
        sHtmlsDir = new File(sFilesDir, HTMLS_DIR);
        sHtmlLocalPicDir = new File(sFilesDir, HTMLS_LOCAL_PIC_DIR);
        sWebImgDir = new File(sFilesDir, WEB_IMGAGES_DIR);
        makeDir(sHtmlsDir);
        makeDir(sHtmlLocalPicDir);
        makeDir(sWebImgDir);
    }

    public static String readTextFromAssets(String relativePath) {
        AssetManager manager = sContext.getAssets();
        try {
            Scanner scanner = new Scanner(manager.open(relativePath));
            StringBuilder builder = new StringBuilder();
            while (scanner.hasNext()) {
                builder.append(scanner.nextLine());
            }
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean saveTextToFile(String relativePath, String txt) {
        String absolutePath = sFilesDir.getAbsolutePath() + File.separator + relativePath;
        FileOutputStream outputStream = null;
        BufferedWriter writer = null;
        try {
            outputStream = new FileOutputStream(absolutePath);
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

    public static String readTextFromFile(String relativePath) {
        String absolutePath = sFilesDir.getAbsolutePath() + File.separator + relativePath;
        FileInputStream inputStream = null;
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            inputStream = new FileInputStream(absolutePath);
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

    public static boolean saveDataToFile(String relativePath, byte[] data) {
        String absolutePath = sFilesDir.getAbsolutePath() + File.separator + relativePath;
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(absolutePath);
            outputStream.write(data);
            outputStream.flush();
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                Log.e("cxh", "", e);
            }
        }
    }

    public static String getWebImageFilename(String url) {
        return url.replaceAll("https://", "")
                .replace("/", "_");
    }

    private static void makeDir(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
}
