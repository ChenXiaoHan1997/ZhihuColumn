package practice.cxh.zhihuzhuanlan.util;

import android.text.TextUtils;

import practice.cxh.zhihuzhuanlan.bean.Author;
import practice.cxh.zhihuzhuanlan.bean.Avatar;

/**
 * Created by Administrator on 2018/9/1 0001.
 */

public class StringUtil {

    public static String getAvatarUrl(Author author, String size) {
        if (author == null
                || author.getAvatar() == null
                || TextUtils.isEmpty(author.getAvatar().getId())
                || TextUtils.isEmpty(author.getAvatar().getTemplate())) {
            return "";
        }
        return getAvatarUrl(author.getAvatar(), size);
    }

    public static String getAvatarUrl(Avatar avatar, String size) {
        if (avatar == null
                || TextUtils.isEmpty(avatar.getId())
                || TextUtils.isEmpty(avatar.getTemplate())) {
            return "";
        }
        return getAvatarUrl(avatar.getId(), size, avatar.getTemplate());
    }

    public static String getAvatarUrl(String picId, String size, String template) {
        if (TextUtils.isEmpty(picId) || TextUtils.isEmpty(template)) {
            return "";
        }
        String picUrl = template.replace("{id}", picId).replace("{size}", size);
        return picUrl;
    }
}
