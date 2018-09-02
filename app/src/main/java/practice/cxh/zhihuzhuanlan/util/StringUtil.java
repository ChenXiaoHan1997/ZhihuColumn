package practice.cxh.zhihuzhuanlan.util;

/**
 * Created by Administrator on 2018/9/1 0001.
 */

public class StringUtil {
    public static String getColumnPicUrl(String picId, String size, String template) {
        String picUrl = template.replace("{id}", picId).replace("{size}", size);
        return picUrl;
    }
}
