package practice.cxh.zhihuzhuanlan.main_page;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.List;

import practice.cxh.zhihuzhuanlan.DataCore;
import practice.cxh.zhihuzhuanlan.base.BasePresenter;
import practice.cxh.zhihuzhuanlan.bean.Column;
import practice.cxh.zhihuzhuanlan.db.ColumnEntityDao;
import practice.cxh.zhihuzhuanlan.db.SubscribeEntityDao;
import practice.cxh.zhihuzhuanlan.entity.ColumnEntity;
import practice.cxh.zhihuzhuanlan.entity.SubscribeEntity;
import practice.cxh.zhihuzhuanlan.util.AsyncUtil;
import practice.cxh.zhihuzhuanlan.util.DbUtil;
import practice.cxh.zhihuzhuanlan.util.FileUtil;
import practice.cxh.zhihuzhuanlan.util.HttpUtil;
import practice.cxh.zhihuzhuanlan.util.JsonUtil;

public class MainpagePresenter extends BasePresenter<MainpageV> {
    private Context mContext;
    private Handler mUiHandler;
    private HttpUtil mHttpUtil;

    private String[] columnsSlugs;

    public MainpagePresenter(Context context) {
        this.mContext = context;
        this.mHttpUtil = new HttpUtil(context);
        this.mUiHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 查询已订阅的专栏的slug，再加载专栏
     */
    public void loadSubscribedColumns() {
        AsyncUtil.executeAsync(new Runnable() {
            @Override
            public void run() {
                // 查询哪些是已订阅的专栏
                findSubscribedColumns();
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // 开始加载专栏列表
                        loadColums();
                    }
                });
            }
        });

    }

    public void findSubscribedColumns() {
        if (DataCore.getInstance().isFirstRun()) {
            // 首次启动时读取文件中默认数据
            String followings = FileUtil.readTextFromAssets("subscribe.txt");
            columnsSlugs = followings.split("\n");
            for (String columnSlug : columnsSlugs) {
                SubscribeEntity subscribeEntity = new SubscribeEntity(columnSlug, true);
                DbUtil.getSubscribeEntityDao()
                        .insertOrReplace(subscribeEntity);
            }
        } else {
            // 非首次启动时读取数据库的订阅记录
            List<SubscribeEntity> subscribeEntityList = DbUtil.getSubscribeEntityDao()
                    .queryBuilder()
                    .where(SubscribeEntityDao.Properties.Subscribed.eq(true))
                    .list();
            columnsSlugs = new String[subscribeEntityList.size()];
            int i = 0;
            for (SubscribeEntity subscribeEntity: subscribeEntityList) {
                columnsSlugs[i++] = subscribeEntity.getColumnSlug();
            }
        }
    }

    public void loadColumnBySlug(final String columnSlug) {
        mHttpUtil.get(HttpUtil.API_BASE + HttpUtil.COLUMN + "/" + columnSlug,
                new HttpUtil.HttpListener<String>() {
                    @Override
                    public void onSuccess(final String response) {
                        AsyncUtil.executeAsync(new Runnable() {
                            @Override
                            public void run() {
                                Column column = JsonUtil.decodeColumn(response);
                                final ColumnEntity columnEntity = ColumnEntity.convertFromColumn(column);
                                SubscribeEntity tmp = DbUtil.getSubscribeEntityDao()
                                        .queryBuilder()
                                        .where(SubscribeEntityDao.Properties.ColumnSlug.eq(columnSlug))
                                        .unique();
                                if (tmp != null && tmp.isSubscribed()) {
                                    columnEntity.setSubscribed(true);
                                }
                                mUiHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 显示在UI
                                        if (getView() != null) {
                                            getView().onColumnLoaded(columnEntity);
                                        }
                                    }
                                });
                                // 保存到数据库
                                saveColumnEntity(columnEntity);
                            }
                        });
                    }

                    @Override
                    public void onFail(String statusCode) {
                        // 加载失败，读取本地数据
                        loadColumnEntityFromDB(columnSlug);
                    }
                });
    }

    /**
     * 加载所有订阅的专栏
     */
    private void loadColums() {
        if (columnsSlugs == null) {
            return;
        }
        for (final String columnSlug : columnsSlugs) {
            loadColumnBySlug(columnSlug);
        }
    }

    private void saveColumnEntity(final ColumnEntity columnEntity) {
        AsyncUtil.executeAsync(new Runnable() {
            @Override
            public void run() {
                DbUtil.getColumnEntityDao().insertOrReplace(columnEntity);
            }
        });
    }

    private void loadColumnEntityFromDB(final String columnSlug) {
        AsyncUtil.executeAsync(new Runnable() {
            @Override
            public void run() {
                List<ColumnEntity> columnEntityList = DbUtil.getColumnEntityDao()
                        .queryBuilder()
                        .where(ColumnEntityDao.Properties.Slug.eq(columnSlug))
                        .list();
                if (columnEntityList.size() > 0) {
                    final ColumnEntity columnEntity = columnEntityList.get(0);
                    SubscribeEntity tmp = DbUtil.getSubscribeEntityDao()
                            .queryBuilder()
                            .where(SubscribeEntityDao.Properties.ColumnSlug.eq(columnSlug))
                            .unique();
                    if (tmp != null && tmp.isSubscribed()) {
                        columnEntity.setSubscribed(true);
                    }
                    mUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (getView() != null) {
                                getView().onColumnLoaded(columnEntity);
                            }
                        }
                    });
                }
            }
        });
    }
}
