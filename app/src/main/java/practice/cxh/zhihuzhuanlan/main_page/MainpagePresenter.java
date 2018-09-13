package practice.cxh.zhihuzhuanlan.main_page;

import android.os.Handler;
import android.util.Log;

import java.util.List;

import practice.cxh.zhihuzhuanlan.bean.Column;
import practice.cxh.zhihuzhuanlan.db.ColumnEntityDao;
import practice.cxh.zhihuzhuanlan.entity.ColumnEntity;
import practice.cxh.zhihuzhuanlan.util.AsyncUtil;
import practice.cxh.zhihuzhuanlan.util.DbUtil;
import practice.cxh.zhihuzhuanlan.util.HttpUtil;
import practice.cxh.zhihuzhuanlan.util.JsonUtil;

public class MainpagePresenter {
    private MainActivity mActivity;
    private Handler mUiHandler;

    private String[] columnsSlugs = new String[]{"zhaohaoyang", "542b2333", "maqianzu", "diqiuzhishiju", "c_134408063", "h4cj250", "baitouwengkezhan", "stormzhang", "huizi", "kaede", "zhangjiawei", "hehehe"};

    public MainpagePresenter(MainActivity activity) {
        this.mActivity = activity;
        mUiHandler = new Handler(mActivity.getMainLooper());
    }

    /**
     * 加载所有关注的专栏
     */
    public void loadColums() {
        for (final String columnSlug : columnsSlugs) {
            HttpUtil.get(HttpUtil.API_BASE + HttpUtil.COLUMN + "/" + columnSlug,
                    new HttpUtil.HttpListener<String>() {
                        @Override
                        public void onSuccess(final String response) {
                            AsyncUtil.getThreadPool().execute(new Runnable() {
                                @Override
                                public void run() {
                                    Column column = JsonUtil.decodeColumn(response);
                                    final ColumnEntity columnEntity = ColumnEntity.convertFromColumn(column);
                                    mUiHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            // 显示在UI
                                            mActivity.onColumnLoaded(columnEntity);
                                        }
                                    });
                                    // 保存到数据库
                                    saveColumnEntity(columnEntity);
                                }
                            });
                        }

                        @Override
                        public void onFail(String detail) {
                            // 加载失败，读取本地数据
                            loadColumnEntityFromDB(columnSlug);
                        }
                    });
        }
    }

    private void saveColumnEntity(final ColumnEntity columnEntity) {
        AsyncUtil.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                DbUtil.getColumnEntityDao().insertOrReplace(columnEntity);
            }
        });
    }

    private void loadColumnEntityFromDB(final String columnSlug) {
        AsyncUtil.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                List<ColumnEntity> columnEntityList = DbUtil.getColumnEntityDao()
                        .queryBuilder()
                        .where(ColumnEntityDao.Properties.Slug.eq(columnSlug))
                        .list();
                if (columnEntityList.size() > 0) {
                    final ColumnEntity columnEntity = columnEntityList.get(0);
                    mUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mActivity.onColumnLoaded(columnEntity);
                        }
                    });
                }
            }
        });
    }
}
