package practice.cxh.zhihuzhuanlan;

import android.util.LruCache;

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
        return getDataNoCached(position, 1);
    }

    public abstract T getDataNoCached(int start, int count);
}
