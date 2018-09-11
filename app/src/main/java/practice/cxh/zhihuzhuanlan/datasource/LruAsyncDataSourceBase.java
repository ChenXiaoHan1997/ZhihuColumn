package practice.cxh.zhihuzhuanlan.datasource;

import android.util.LruCache;

public abstract class LruAsyncDataSourceBase<T> implements AsyncDataSource<T> {

    private static final int DEFAULT_CACHE_SIZE = 50;

    private LruCache<Integer, T> mCache;

    public LruAsyncDataSourceBase() {
        this(DEFAULT_CACHE_SIZE);
    }

    public LruAsyncDataSourceBase(int cacheSize) {
        mCache = new LruCache<>(cacheSize);
    }

    @Override
    public void getDataAt(int positon, AsyncDataListener<T> listener) {
        T data = mCache.get(positon);
        if (data != null) {
            listener.onDataLoaded(data);
            return;
        }
        getDataNoCached(positon, 1, listener);
    }

    @Override
    public void getDataWithRange(int start, int count, AsyncDataListener<T> listener) {
        getDataNoCached(start, count, listener);
    }

    protected abstract void getDataNoCached(int start, int count, AsyncDataListener<T> listener);

}
