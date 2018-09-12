package practice.cxh.zhihuzhuanlan.datasource;

import java.util.List;

public abstract class PreloadDataSourceBase0<T> extends LruDataSourceBase<T> {
    private static final int DEFAULT_PRELOAD_COUNT = 10;
    private static final int DEFAULT_PRELOAD_HALF = DEFAULT_PRELOAD_COUNT >> 1;

    private int preloadHalf;

    public PreloadDataSourceBase0() {
        this(DEFAULT_PRELOAD_COUNT);
    }

    public PreloadDataSourceBase0(int preloadCount) {
        super();
        this.preloadHalf = preloadCount / 2;
    }

    public PreloadDataSourceBase0(int preloadCount, int cacheSize) {
        super(cacheSize);
        this.preloadHalf = preloadCount / 2;
    }

    @Override
    public T getDataAt(int position) {
        if (position % DEFAULT_PRELOAD_HALF == 0) {
            preload(position);
        }
        return super.getDataAt(position);
    }

    private void preload(int position) {
        List<T> dataList = null;
        int i = 0;
        dataList = getDataNoCached(position + 1, DEFAULT_PRELOAD_HALF);
        i = position + 1;
        for (T data : dataList) {
            storeData(i++, data);
        }
        dataList = getDataNoCached(position - DEFAULT_PRELOAD_HALF, DEFAULT_PRELOAD_HALF);
        i = position - DEFAULT_PRELOAD_HALF;
        for (T data : dataList) {
            storeData(i++, data);
        }
    }
}
