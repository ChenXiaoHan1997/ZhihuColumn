package practice.cxh.zhihuzhuanlan.column_page;

import java.util.List;

import practice.cxh.zhihuzhuanlan.entity.ArticleEntity;

public interface ArticleListV {
    void onArticlesLoaded(List<ArticleEntity> articleEntityList, int offset, int limit);
    void onArticleLoaded(ArticleEntity articleEntity, int index);
}
