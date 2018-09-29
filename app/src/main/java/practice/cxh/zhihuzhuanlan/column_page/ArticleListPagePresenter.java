package practice.cxh.zhihuzhuanlan.column_page;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import practice.cxh.zhihuzhuanlan.bean.Article;
import practice.cxh.zhihuzhuanlan.db.ArticleEntityDao;
import practice.cxh.zhihuzhuanlan.entity.ArticleEntity;
import practice.cxh.zhihuzhuanlan.entity.SubscribeEntity;
import practice.cxh.zhihuzhuanlan.event_pool.EventPool;
import practice.cxh.zhihuzhuanlan.event_pool.IEvent;
import practice.cxh.zhihuzhuanlan.event_pool.event.SubscribeEvent;
import practice.cxh.zhihuzhuanlan.util.AsyncUtil;
import practice.cxh.zhihuzhuanlan.util.DbUtil;
import practice.cxh.zhihuzhuanlan.util.HttpUtil;
import practice.cxh.zhihuzhuanlan.util.JsonUtil;

public class ArticleListPagePresenter {
    private Handler mUiHandler;
    private HttpUtil mHttpUtil;

    public ArticleListPagePresenter(Context context) {
        this.mHttpUtil = new HttpUtil(context);
        this.mUiHandler = new Handler(Looper.getMainLooper());
    }

    public void setSubscribe(final String columnSlug, final boolean subscribe) {
        AsyncUtil.executeAsync(new Runnable() {
            @Override
            public void run() {
                SubscribeEntity subscribeEntity = new SubscribeEntity(columnSlug, subscribe);
                DbUtil.getSubscribeEntityDao()
                        .insertOrReplace(subscribeEntity);
                IEvent event = new SubscribeEvent(columnSlug, subscribe);
                EventPool.getInstace().publishEvent(event);
            }
        });
    }
}
