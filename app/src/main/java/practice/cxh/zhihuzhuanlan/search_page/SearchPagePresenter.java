package practice.cxh.zhihuzhuanlan.search_page;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import practice.cxh.zhihuzhuanlan.bean.Column;
import practice.cxh.zhihuzhuanlan.db.SubscribeEntityDao;
import practice.cxh.zhihuzhuanlan.entity.ColumnEntity;
import practice.cxh.zhihuzhuanlan.entity.SubscribeEntity;
import practice.cxh.zhihuzhuanlan.util.AsyncUtil;
import practice.cxh.zhihuzhuanlan.util.DbUtil;
import practice.cxh.zhihuzhuanlan.util.HttpUtil;
import practice.cxh.zhihuzhuanlan.util.JsonUtil;

public class SearchPagePresenter {

    private SearchV mSearchV;
    private Handler mUiHandler;
    private HttpUtil mHttpUtil;

    public SearchPagePresenter(SearchV searchV, Context context) {
        this.mSearchV = searchV;
        this.mHttpUtil = new HttpUtil(context);
        this.mUiHandler = new Handler(Looper.getMainLooper());
    }

    public void searchColumn(final String columnSlug) {
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
                                        mSearchV.onColumnFound(columnEntity);
                                    }
                                });

                            }
                        });
                    }

                    @Override
                    public void onFail(String statusCode) {
                        if (HttpUtil.ERROR_404_NOT_FOUND.equals(statusCode)) {
                            mSearchV.onColumnNotFound();
                        }
                    }
                });
    }

}
