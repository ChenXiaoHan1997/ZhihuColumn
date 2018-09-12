package practice.cxh.zhihuzhuanlan.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/9/1 0001.
 */

public class HtmlUtil {

    private static final String IMAGE_CLICK_JS_FILE = "set_image_click_js.txt";

    private static final String REGEX_WEB_IMG = "(?<=data-original=\")http.*?(?=\")|(?<=img src=\")http.*?(?=\")";

    public static String getHtmlDataOld(String htmlBody, boolean displayPic) {
        String head = "<head><style>img{max-width: 100%; width:auto; height: auto;}</style></head>";
        htmlBody = htmlBody.replaceAll("<noscript>", "")
                .replaceAll("</noscript>", "");
        if (!displayPic) {
            htmlBody = htmlBody.replaceAll("<img .*?>", "");
        }
        return "<html>" + head + "<body>" + htmlBody + "</body>" + FileUtil.readTextFromAssets("set_image_click_js.txt") + "</html>";
    }

    public static String getHtmlData0(String htmlBody, boolean displayPic) {
        String head = "<head><style>img{max-width: 100%; width:auto; height: auto;}</style></head>";
        htmlBody = htmlBody.replaceAll("<noscript>", "")
                .replaceAll("</noscript>", "");
        if (!displayPic) {
            htmlBody = htmlBody.replaceAll("<img .*?>", "");
        }
        return "<html>" + head + "<body>" + htmlBody + "</body>" + "</html>";
    }

    public static String getHtmlData(String htmlBody, boolean displayPic) {
        Pattern pattern = Pattern.compile("<(img src=\"http.*?\").*?>");
        Matcher matcher = pattern.matcher(htmlBody);
        while (matcher.find()) {
            if (matcher.groupCount() > 0) {
                htmlBody = htmlBody.replace(matcher.group(),
                        "<" + matcher.group(1) + ">");
            }
        }
        htmlBody = htmlBody.replaceAll("(?<=</noscript>)<img .*?>", "")
                .replaceAll("</*noscript>", "");
        if (!displayPic) {
            htmlBody = htmlBody.replaceAll("<img .*?>", "");
        }
        return String.format(FileUtil.readTextFromAssets("template.html"), htmlBody);
    }

    public static String replaceWebImgSrc(String html) {
        Pattern pattern = Pattern.compile(REGEX_WEB_IMG);
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            String httpUrl = matcher.group();
            String fileUrl = httpUrl.replaceAll("https://", "")
                    .replace("/", "_");
            html = html.replace(httpUrl, fileUrl);
        }
        return html;
    }

    public static List<String> getWebImages(String html) {
        return StringUtil.findAllRegexMatch(html, REGEX_WEB_IMG);
    }
}
