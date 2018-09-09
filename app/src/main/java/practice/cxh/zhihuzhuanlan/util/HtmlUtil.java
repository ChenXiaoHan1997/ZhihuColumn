package practice.cxh.zhihuzhuanlan.util;

/**
 * Created by Administrator on 2018/9/1 0001.
 */

public class HtmlUtil {

    private static final String IMAGE_CLICK_JS_FILE = "set_image_click_js.txt";

    public static String getHtmlData(String htmlBody, boolean displayPic) {
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

    public static String getHtmlData1(String htmlBody, boolean displayPic) {
        htmlBody = htmlBody.replaceAll("<noscript>", "")
                .replaceAll("</noscript>", "");
        if (!displayPic) {
            htmlBody = htmlBody.replaceAll("<img .*?>", "");
        }
        return String.format(FileUtil.readTextFromAssets("template.html"), htmlBody);
    }
}
