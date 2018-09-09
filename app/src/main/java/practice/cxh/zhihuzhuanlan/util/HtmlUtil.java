package practice.cxh.zhihuzhuanlan.util;

/**
 * Created by Administrator on 2018/9/1 0001.
 */

public class HtmlUtil {
    public static String getHtmlData(String htmlBody, boolean displayPic) {
        String head = "<head><style>img{max-width: 100%; width:auto; height: auto;}</style></head>";
        htmlBody = htmlBody.replaceAll("<noscript>", "")
                .replaceAll("</noscript>", "");
        if (!displayPic) {
            htmlBody = htmlBody.replaceAll("<img .*?>", "");
        }
        return "<html>" + head + "<body>" + htmlBody + "</body></html>";
    }
}
