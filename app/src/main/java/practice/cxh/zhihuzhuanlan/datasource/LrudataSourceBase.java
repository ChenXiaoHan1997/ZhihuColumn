package practice.cxh.zhihuzhuanlan.datasource;

import android.util.LruCache;

import java.util.List;

public abstract class LrudataSourceBase<T> implements DataSource<T> {

    private static final int DEFAULT_CACHE_SIZE = 50;

    private LruCache<Integer, T> mCache;

    public LrudataSourceBase() {
        this(DEFAULT_CACHE_SIZE);
    }

    public LrudataSourceBase(int cacheSize) {
        mCache = new LruCache<>(cacheSize);
    }

    @Override
    public T getDataAt(int position) {
        T data = mCache.get(position);
        if (data != null) {
            return data;
        }
        return getDataNoCached(position, 1).get(0);
    }

    protected abstract List<T> getDataNoCached(int start, int count);

    protected void storeData(int position, T data) {
        mCache.put(position, data);
    }
}
