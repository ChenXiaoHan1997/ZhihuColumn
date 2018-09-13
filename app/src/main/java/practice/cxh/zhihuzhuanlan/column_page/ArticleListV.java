package practice.cxh.zhihuzhuanlan.column_page;

import java.util.List;

import practice.cxh.zhihuzhuanlan.entity.ArticleEntity;

public interface ArticleListV {
    void onArticleListLoaded(List<ArticleEntity> articleEntityList, boolean clearOld);
    void onArticleListLoaded(List<ArticleEntity> articleEntityList, int offset);
    void showLoading(boolean loading);
}
