package practice.cxh.zhihuzhuanlan.util;

import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import practice.cxh.zhihuzhuanlan.bean.Article;
import practice.cxh.zhihuzhuanlan.bean.ArticleContent;
import practice.cxh.zhihuzhuanlan.bean.Column;
import practice.cxh.zhihuzhuanlan.bean.RawPostListJson;

/**
 * Created by Administrator on 2018/9/1 0001.
 */

public class JsonUtil {

    public static Column decodeColumn(String json) {
        Gson gson = new Gson();
        Column column = gson.fromJson(json, Column.class);
        return column;
    }

    public static List<Article> decodeArticleList(String json) {
        json = "{articleList:" + json + "}";
        Log.d("cxh", json);
        Gson gson = new Gson();
        RawPostListJson rawPostListJson = gson.fromJson(json, RawPostListJson.class);
        List<Article> articleList = rawPostListJson.getArticleList();
        return articleList;
    }

    public static ArticleContent decodeArticleContent(String json) {
        Gson gson = new Gson();
        ArticleContent articleContent = gson.fromJson(json, ArticleContent.class);
        return articleContent;
    }

}
