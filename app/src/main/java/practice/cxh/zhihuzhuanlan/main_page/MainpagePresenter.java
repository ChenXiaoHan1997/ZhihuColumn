package practice.cxh.zhihuzhuanlan.main_page;

import android.os.Handler;
import android.util.Log;

import java.util.List;

import practice.cxh.zhihuzhuanlan.ZhihuZhuanlanApplication;
import practice.cxh.zhihuzhuanlan.bean.Column;
import practice.cxh.zhihuzhuanlan.db.ColumnEntityDao;
import practice.cxh.zhihuzhuanlan.entity.ColumnEntity;
import practice.cxh.zhihuzhuanlan.util.AsyncUtil;
import practice.cxh.zhihuzhuanlan.util.DbUtil;
import practice.cxh.zhihuzhuanlan.util.HttpUtil;
import practice.cxh.zhihuzhuanlan.util.JsonUtil;

public class MainpagePresenter {
    private MainActivity mActivity;
    private ColumnEntityDao mColumnEntityDao;
    private Handler mUiHandler;

    private String[] columnsSlugs = new String[] {"zhaohaoyang", "542b2333", "qiechihe", "c_134408063", "h4cj250", "huizi", "kaede", "zhangjiawei"};

    public MainpagePresenter(MainActivity activity) {
        this.mActivity = activity;
        mColumnEntityDao = DbUtil.getDaoSession().getColumnEntityDao();
        mUiHandler = new Handler(mActivity.getMainLooper());
    }

    public void loadColums() {
        for (final String columnSlug : columnsSlugs) {
            HttpUtil.get(HttpUtil.API_BASE + HttpUtil.COLUMN + "/" + columnSlug, new HttpUtil.HttpListener() {
                @Override
                public void onSuccess(String response) {
                    Column column = JsonUtil.decodeColumn(response);
                    ColumnEntity columnEntity = ColumnEntity.convertFromColumn(column);
                    mActivity.onColumnLoaded(columnEntity);
                    saveColumnEntity(columnEntity);
                }

                @Override
                public void onFail() {
                    loadColumnEntityFromDB(columnSlug);
                }
            });
        }
    }

    private void saveColumnEntity(final ColumnEntity columnEntity) {
        AsyncUtil.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                mColumnEntityDao.insertOrReplace(columnEntity);
            }
        });
    }

    private void loadColumnEntityFromDB(final String columnSlug) {
        AsyncUtil.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                List<ColumnEntity> columnEntityList = mColumnEntityDao.queryRaw("where SLUG = ?", columnSlug);
                Log.d("cxh", columnEntityList.toString());
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
