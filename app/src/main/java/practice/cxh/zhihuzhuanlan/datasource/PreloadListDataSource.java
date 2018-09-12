package practice.cxh.zhihuzhuanlan.datasource;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

public abstract class PreloadListDataSource<T> extends ListDataSource<T> {

    private static final int DEFAULT_PRELOAD_COUNT = 10;

    private int preloadCount;
    private int preloadHalf;
    private int maxCount;

    public PreloadListDataSource(int maxCount, RecyclerView.Adapter adapter) {
        this(DEFAULT_PRELOAD_COUNT, maxCount, adapter);
    }

    public PreloadListDataSource(int preloadCount, int maxCount, RecyclerView.Adapter adapter) {
        super(adapter);
        preloadCount = preloadCount > 1? preloadCount: DEFAULT_PRELOAD_COUNT;
        this.preloadCount = preloadCount;
        this.preloadHalf = preloadCount >> 1;
        this.maxCount = maxCount;
    }

    protected void init() {
        preloadData(0, preloadCount);
    }

    @Override
    public T getDataAt(int position) {
        if (position % preloadHalf == 0) {
            preloadData(position, preloadCount);
        }
        return position <= mDataList.size()?
                mDataList.get(position): null;
    }

    private void preloadData(int start, int count) {
        if (count <= 0 || start + count - 1 <= mDataList.size()) {
            return;
        }
        Log.d("tag1", "preloadData: start=" + start + ", count=" + count);
        onPreloadData(start, Math.min(count, maxCount - start + 1));
    }

    protected abstract void onPreloadData(int start, int count);
}
