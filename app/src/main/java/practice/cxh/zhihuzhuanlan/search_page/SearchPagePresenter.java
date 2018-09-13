package practice.cxh.zhihuzhuanlan.search_page;

import android.os.Handler;
import android.os.Looper;

import practice.cxh.zhihuzhuanlan.bean.Column;
import practice.cxh.zhihuzhuanlan.entity.ColumnEntity;
import practice.cxh.zhihuzhuanlan.util.AsyncUtil;
import practice.cxh.zhihuzhuanlan.util.HttpUtil;
import practice.cxh.zhihuzhuanlan.util.JsonUtil;

public class SearchPagePresenter {

    private SearchV mSearchV;
    private Handler mUiHandler;

    public SearchPagePresenter(SearchV searchV) {
        this.mSearchV = searchV;
        this.mUiHandler = new Handler(Looper.getMainLooper());
    }

    public void searchColumn(String columnSlug) {
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
                                        mSearchV.onColumnFound(columnEntity);
                                    }
                                });

                            }
                        });
                    }

                    @Override
                    public void onFail(String statusCode) {
                        if (HttpUtil.ERROR_404_NOT_FOUND.equals(statusCode)) {

                        }
                    }
                });
    }

}
