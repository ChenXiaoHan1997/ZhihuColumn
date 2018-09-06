package practice.cxh.zhihuzhuanlan.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import practice.cxh.zhihuzhuanlan.entity.ArticleEntity;
import practice.cxh.zhihuzhuanlan.entity.ColumnEntity;
import practice.cxh.zhihuzhuanlan.entity.ArticleContentEntity;

import practice.cxh.zhihuzhuanlan.db.ArticleEntityDao;
import practice.cxh.zhihuzhuanlan.db.ColumnEntityDao;
import practice.cxh.zhihuzhuanlan.db.ArticleContentEntityDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig articleEntityDaoConfig;
    private final DaoConfig columnEntityDaoConfig;
    private final DaoConfig articleContentEntityDaoConfig;

    private final ArticleEntityDao articleEntityDao;
    private final ColumnEntityDao columnEntityDao;
    private final ArticleContentEntityDao articleContentEntityDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        articleEntityDaoConfig = daoConfigMap.get(ArticleEntityDao.class).clone();
        articleEntityDaoConfig.initIdentityScope(type);

        columnEntityDaoConfig = daoConfigMap.get(ColumnEntityDao.class).clone();
        columnEntityDaoConfig.initIdentityScope(type);

        articleContentEntityDaoConfig = daoConfigMap.get(ArticleContentEntityDao.class).clone();
        articleContentEntityDaoConfig.initIdentityScope(type);

        articleEntityDao = new ArticleEntityDao(articleEntityDaoConfig, this);
        columnEntityDao = new ColumnEntityDao(columnEntityDaoConfig, this);
        articleContentEntityDao = new ArticleContentEntityDao(articleContentEntityDaoConfig, this);

        registerDao(ArticleEntity.class, articleEntityDao);
        registerDao(ColumnEntity.class, columnEntityDao);
        registerDao(ArticleContentEntity.class, articleContentEntityDao);
    }
    
    public void clear() {
        articleEntityDaoConfig.clearIdentityScope();
        columnEntityDaoConfig.clearIdentityScope();
        articleContentEntityDaoConfig.clearIdentityScope();
    }

    public ArticleEntityDao getArticleEntityDao() {
        return articleEntityDao;
    }

    public ColumnEntityDao getColumnEntityDao() {
        return columnEntityDao;
    }

    public ArticleContentEntityDao getArticleContentEntityDao() {
        return articleContentEntityDao;
    }

}
