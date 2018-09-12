package practice.cxh.zhihuzhuanlan.util;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import practice.cxh.zhihuzhuanlan.bean.Author;
import practice.cxh.zhihuzhuanlan.bean.Avatar;

/**
 * Created by Administrator on 2018/9/1 0001.
 */

public class StringUtil {

    public static String getAuthorAndTime(String author, String publishedTime) {
        if (!TextUtils.isEmpty(author) && !TextUtils.isEmpty(publishedTime)) {
            return author + "，" + publishedTime;
        } else if (!TextUtils.isEmpty(author)) {
            return author;
        } else if (!TextUtils.isEmpty(publishedTime)) {
            return publishedTime;
        } else {
            return "";
        }
    }

    /**
     * 获取头像的url
     * @param author
     * @param size
     * @return
     */
    public static String getAvatarUrl(Author author, String size) {
        if (author == null
                || author.getAvatar() == null
                || TextUtils.isEmpty(author.getAvatar().getId())
                || TextUtils.isEmpty(author.getAvatar().getTemplate())) {
            return "";
        }
        return getAvatarUrl(author.getAvatar(), size);
    }

    /**
     * 获取头像的url
     * @param avatar
     * @param size
     * @return
     */
    public static String getAvatarUrl(Avatar avatar, String size) {
        if (avatar == null
                || TextUtils.isEmpty(avatar.getId())
                || TextUtils.isEmpty(avatar.getTemplate())) {
            return "";
        }
        return getAvatarUrl(avatar.getId(), size, avatar.getTemplate());
    }

    /**
     * 获取头像的url
     * @param picId
     * @param size
     * @param template
     * @return
     */
    public static String getAvatarUrl(String picId, String size, String template) {
        if (TextUtils.isEmpty(picId) || TextUtils.isEmpty(template)) {
            return "";
        }
        String picUrl = template.replace("{id}", picId).replace("{size}", size);
        return picUrl;
    }

    public static List<String> findAllRegexMatch(String source, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }
}
